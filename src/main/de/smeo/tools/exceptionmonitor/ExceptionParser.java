package de.smeo.tools.exceptionmonitor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Is parsing a text file or a string and extracts the java exceptions with its stack traces.
 * 
 * @author smeo
 *
 */
public class ExceptionParser {
	private List<LoggedException> collectedExceptions = new ArrayList<LoggedException>();
	private List<ExceptionCausedByChain> exceptionChains = new ArrayList<ExceptionCausedByChain>();
	private Set<StackTraceChain> stackTraceChains = new HashSet<StackTraceChain>();
	
	private LoggedException currException = null;
	private ExceptionCausedByChain currExceptionCausedByChain = null; 
	
	private final static String REGEXP_CAUSED_BY = "Caused by:";
	private final static String REGEXP_UNKNOWN_SOURCE = "\\(Unknown Source\\)";
	private final static String REGEXP_AT = "at";
	private final static String REGEXP_CLASSNAME_OR_PATHSEGMENT = "[a-zA-Z_$][a-zA-Z0-9\\d_$]*";
	private final static String REGEXP_CLASSNAME = "(" + REGEXP_CLASSNAME_OR_PATHSEGMENT+ "\\.)+" + REGEXP_CLASSNAME_OR_PATHSEGMENT;
	private final static String REGEXP_EXCEPTION_COMMENT = ":.*";
	protected final static String REGEXP_SOURCE_LINE = "\\([a-zA-Z_$][a-zA-Z\\d_$]*\\.java:[0-9]+\\)";

	protected final static String REGEXP_EXCEPTION_START = REGEXP_CLASSNAME + "("+REGEXP_EXCEPTION_COMMENT+")?";
	protected final static String REGEXP_EXCEPTION_START_CAUSED_BY = REGEXP_CAUSED_BY + " " + REGEXP_EXCEPTION_START;
	protected final static String REGEXP_SOURCE_LINE_OR_UNKNOWN = REGEXP_SOURCE_LINE + "|" + REGEXP_UNKNOWN_SOURCE;
	protected final static String REGEXP_STACKTRACE_MEMBER_AT = REGEXP_AT + " " + REGEXP_CLASSNAME + "("+REGEXP_SOURCE_LINE_OR_UNKNOWN+")?"; 
	
	
	public List<ExceptionCausedByChain> getExceptionChains() {
		return exceptionChains;
	}

	public List<LoggedException> getExceptions() {
		return collectedExceptions;
	}

	public void parse(String exceptionText) {
		String previousLine = null;
		String[] textSplitToLines = exceptionText.split("\n");
		
		for (String currLine : textSplitToLines){
			previousLine = parseLine(currLine, previousLine);
		}
		flush();
	} 

	private String parseLine(String currLine, String previousLine) {
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
			
		return (currLineTrimmed);
	}

	private void flush() {
		moveCurrExceptionToExceptionList();
		exceptionChains.add(currExceptionCausedByChain);
		currExceptionCausedByChain = null;
	}
	
	private static String removeCausedBy(String line) {
		return line.replace(REGEXP_CAUSED_BY, "").trim();
	}

	private void moveCurrExceptionToExceptionList() {
		collectedExceptions.add(currException);
		currException = null;
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

}
