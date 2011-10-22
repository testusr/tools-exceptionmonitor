package de.smeo.tools.exceptionmonitor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

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
	public final static String REGEXP_EXCEPTION_START_CAUSED_BY = ExceptionCausedByChain.REGEXP_CAUSED_BY + " " + REGEXP_EXCEPTION_START;
	public final static String REGEXP_SOURCE_LINE_OR_UNKNOWN = REGEXP_SOURCE_LINE + "|" + REGEXP_TEXT;
	public final static String REGEXP_STACKTRACE_MEMBER_AT = REGEXP_AT + " " + REGEXP_CLASSNAME + "(\\." + REGEXP_INIT + "){0,1}" + "("	+ REGEXP_SOURCE_LINE_OR_UNKNOWN + ")?";
	
	private List<LoggedException> collectedExceptions = new ArrayList<LoggedException>();
	private List<ExceptionCausedByChain> exceptionChains = new ArrayList<ExceptionCausedByChain>();
	
	private LoggedException currException = null;
	private ExceptionCausedByChain currExceptionCausedByChain = null; 
	private String previousLine = null;
	
	
	
	public List<ExceptionCausedByChain> getExceptionChains() {
		return exceptionChains;
	}

	public List<LoggedException> getExceptions() {
		return collectedExceptions;
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
	
	protected void parseLine(String currLine) {
		String currLineTrimmed = currLine.trim();
		if (previousLine != null){
			if (firstLineMarksStartOfExceptionTrace(previousLine, currLineTrimmed)){
				currException = createNewException(previousLine);
				currExceptionCausedByChain = new ExceptionCausedByChain(currException);
			}
			
			if (isCurrentlyFillingException()){
				if (firstLineIsPartOfCurrentException(previousLine, currLineTrimmed)){
					currException.getStackTrace().addLine(previousLine);
				} else 
					if (firstLineIstStartsCausedByExceptionTrace(previousLine, currLineTrimmed)){
						LoggedException newCausedByException = createNewException(removeCausedBy(previousLine));
						
						currExceptionCausedByChain.addCausedBy(newCausedByException);
						moveCurrExceptionToExceptionList();
						currException = newCausedByException;
				} else 
					if(firstLineMarksEOFExceptionTrace(previousLine, currLineTrimmed)){
						flush();
				}
			}
		}
			
		previousLine = currLineTrimmed;
	}

	public void flush() {
		moveCurrExceptionToExceptionList();
		if (currExceptionCausedByChain != null){
			exceptionChains.add(currExceptionCausedByChain);
			currExceptionCausedByChain = null;
		}
		previousLine = null;
	}
	
	private static String removeCausedBy(String line) {
		return line.replace(ExceptionCausedByChain.REGEXP_CAUSED_BY, "").trim();
	}

	private void moveCurrExceptionToExceptionList() {
		if (currException != null){
			collectedExceptions.add(currException);
			currException = null;
		}
	}

	private static LoggedException createNewException(String exceptionStartLine) {
		LoggedException newException = new LoggedException(extractClassNameFromExceptionStart(exceptionStartLine));
		newException.setComment(extractCommentFromExceptionStart(exceptionStartLine));
		return newException;
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
		return (currException != null);
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

	public void clean() {
		this.collectedExceptions.clear();
		this.exceptionChains.clear();
	}

}
