package de.smeo.tools.exceptionmonitor.exceptionparser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import de.smeo.tools.exceptionmonitor.domain.Exception;
import de.smeo.tools.exceptionmonitor.domain.ExceptionChain;
import de.smeo.tools.exceptionmonitor.domain.ExceptionOccuranceRecord;


/**
 * Is parsing a text file or a string and extracts the java exceptions with its stack traces.
 * 
 * @author smeo
 *
 */
public class ExceptionParser {
	public final static String REGEXP_TEXT = "\\([A-Za-z ]+\\)"; // i have chosen this as there are unusuals like "(Unknown Source)", "(Native Method)".. there might be more
	public final static String REGEXP_AT = "at";
	public final static String REGEXP_CLASSNAME_OR_PATHSEGMENT = "[a-zA-Z_$][a-zA-Z0-9\\d_$]*";
	public final static String REGEXP_CLASSNAME = "(" + REGEXP_CLASSNAME_OR_PATHSEGMENT + "\\.)+"+ REGEXP_CLASSNAME_OR_PATHSEGMENT;
	public final static String REGEXP_EXCEPTION_COMMENT = ":.*";
	public final static String REGEXP_INIT = "<init>";
	public final static String REGEXP_SOURCE_LINE = "\\([a-zA-Z_$][a-zA-Z\\d_$]*\\.java:[0-9]+\\)";
	public final static String REGEXP_EXCEPTION_START = REGEXP_CLASSNAME + "(" + REGEXP_EXCEPTION_COMMENT + ")?";
	public final static String REGEXP_EXCEPTION_START_CAUSED_BY = ExceptionChainCreator.REGEXP_CAUSED_BY + " " + REGEXP_EXCEPTION_START;
	public final static String REGEXP_SOURCE_LINE_OR_UNKNOWN = REGEXP_SOURCE_LINE + "|" + REGEXP_TEXT;
	public final static String REGEXP_STACKTRACE_MEMBER_AT = REGEXP_AT + " " + REGEXP_CLASSNAME + "(\\." + REGEXP_INIT + "){0,1}" + "("	+ REGEXP_SOURCE_LINE_OR_UNKNOWN + ")?";
	
	private Map<String, ExceptionChain> exceptionChainToIdMap = new HashMap<String, ExceptionChain>();
	private Map<String, Exception> collectedExceptions = new HashMap<String, Exception>();
	
	private Set<ExceptionChainCreator> exceptionChains = new HashSet<ExceptionChainCreator>();
	private List<ExceptionOccuranceRecord> exceptionOccurances = new ArrayList<ExceptionOccuranceRecord>();
	

	private long currExceptionStartFileIndex = -1;
	private ExceptionCreator currExceptionCreator = null;
	private ExceptionChainCreator currExceptionCausedByChainCreator = null;
	private String previousLine = null;
	
	private long currLineIndex = 0;
	private String filename;
	
	public List<Exception> getExceptions() {
		List<Exception> exceptionList = new ArrayList<Exception>();
		exceptionList.addAll(collectedExceptions.values());
		return exceptionList;
	}

	public ExceptionParser parseAndFlush(String exceptionText) {
		parse(exceptionText);
		flush();
		return this;
	}

	public void parse(String exceptionText) {
		String[] textSplitToLines = exceptionText.split("\n");
		
		for (String currLine : textSplitToLines){
			parseLine(currLine);
		}
	} 
	
	protected ExceptionOccuranceRecord parseLine(String currLine) {
		ExceptionOccuranceRecord exceptionOccuranceRecord = null;
		currLineIndex++;
		String currLineTrimmed = currLine.trim();
		if (previousLine != null){
			if (firstLineMarksStartOfExceptionTrace(previousLine, currLineTrimmed)){
				currExceptionCreator = createNewExceptionCreator(previousLine);
				currExceptionStartFileIndex = currLineIndex-1;
				currExceptionCausedByChainCreator = new ExceptionChainCreator();
			}
			
			if (isCurrentlyFillingException()){
				if (firstLineIsPartOfCurrentException(previousLine, currLineTrimmed)){
					currExceptionCreator.addLine(previousLine);
				} else 
					if (firstLineIstStartsCausedByExceptionTrace(previousLine, currLineTrimmed)){
						flushCurrException();
						currExceptionCreator = createNewExceptionCreator(removeCausedBy(previousLine));
				} else 
					if(firstLineMarksEOFExceptionTrace(previousLine, currLineTrimmed)){
						exceptionOccuranceRecord = flush();
				}
			}
		}
			
		previousLine = currLineTrimmed;
		return exceptionOccuranceRecord;
	}

	private void flushCurrException() {
		Exception currException = currExceptionCreator.createException();
		if (!collectedExceptions.containsKey(currException.getId())){
			collectedExceptions.put(currException.getId(), currException);
		} else {
			currException = collectedExceptions.get(currException.getId());
		}
		currExceptionCausedByChainCreator.addCausedBy(currException);
		currExceptionCreator = null;
	}

	public ExceptionOccuranceRecord flush() {
		ExceptionOccuranceRecord newExceptionOccuranceRecord = null;
		flushCurrException();
		if (currExceptionCausedByChainCreator.size() > 0){
			ExceptionChain newExceptionChain = currExceptionCausedByChainCreator.createExceptionChain();
			if (exceptionChainToIdMap.containsKey(newExceptionChain.getId())){
				newExceptionChain = exceptionChainToIdMap.get(newExceptionChain.getId());
			} else {
				exceptionChainToIdMap.put(newExceptionChain.getId(), newExceptionChain);
			}
			newExceptionOccuranceRecord = new ExceptionOccuranceRecord(filename, System.currentTimeMillis(), currExceptionStartFileIndex, newExceptionChain); 
			exceptionOccurances.add(newExceptionOccuranceRecord);
			currExceptionCausedByChainCreator = null;
		}
		previousLine = null;
		currExceptionCreator = null;
		return newExceptionOccuranceRecord;
	}
	
	private static String removeCausedBy(String line) {
		return line.replace(ExceptionChainCreator.REGEXP_CAUSED_BY, "").trim();
	}

	private static ExceptionCreator createNewExceptionCreator(String exceptionStartLine) {
		ExceptionCreator newExceptionCreator = new ExceptionCreator(extractClassNameFromExceptionStart(exceptionStartLine));
		newExceptionCreator.setExceptionComment(extractCommentFromExceptionStart(exceptionStartLine));
		return newExceptionCreator;
	}

	private static String extractCommentFromExceptionStart(String exceptionStartLine) {
		String[] classNameAndComment = getClassNameAndComment(exceptionStartLine);
		if (classNameAndComment.length == 2){
			return classNameAndComment[1];
		}
		return null;
	}

	private static String extractClassNameFromExceptionStart(
			String exceptionStartLine) {
		String[] classNameAndComment = getClassNameAndComment(exceptionStartLine);
		return classNameAndComment[0];
	}

	private static String[] getClassNameAndComment(String exceptionStartLine) {
		String[] classNameAndComment = exceptionStartLine.split(":");
		assert(classNameAndComment.length <= 2);
		assert(classNameAndComment[0].matches(REGEXP_CLASSNAME));
		return classNameAndComment;
	}

	
	private boolean isCurrentlyFillingException() {
		return (currExceptionCreator != null);
	}

	protected boolean firstLineMarksStartOfExceptionTrace(String firstLine, String secondLine){
		return (matchesRegexp(REGEXP_EXCEPTION_START, firstLine) && matchesRegexp(REGEXP_STACKTRACE_MEMBER_AT, secondLine));
	}



	protected boolean firstLineIsPartOfCurrentException(String firstLine, String secondLine) {
		if (matchesRegexp(REGEXP_STACKTRACE_MEMBER_AT, firstLine)){
			return true;
		}
		return false;
	}
	
	protected boolean firstLineIstStartsCausedByExceptionTrace(String firstLine, String secondLine) {
		return (matchesRegexp(REGEXP_EXCEPTION_START_CAUSED_BY, firstLine) && (matchesRegexp(REGEXP_STACKTRACE_MEMBER_AT, secondLine)));
	}

	protected boolean firstLineMarksEOFExceptionTrace(String firstLine, String secondLine) {
		if (secondLine == null){
			return true;
		}
		return (!matchesRegexp(REGEXP_STACKTRACE_MEMBER_AT, secondLine) && !matchesRegexp(REGEXP_EXCEPTION_START_CAUSED_BY, secondLine));
	}
	
	private boolean matchesRegexp(String regexp, String line) {
		Pattern pattern = Pattern.compile(regexp);
		return pattern.matcher(line).matches();
	}
	
	public void setCurrLineIndex(long currLineIndex) {
		this.currLineIndex = currLineIndex;
	}

	public long getCurrLineIndex() {
		return currLineIndex;
	}
	
	public void clean() {
		this.collectedExceptions.clear();
		this.exceptionChains.clear();
	}

	public List<ExceptionOccuranceRecord> getExceptionOccuranceRecords() {
		return exceptionOccurances;
	}

	public void setFilename(String absolutePath) {
		this.filename = absolutePath;
	}

}
