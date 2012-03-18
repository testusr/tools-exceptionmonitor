package de.smeo.tools.exceptionmonitor.exceptionparser;

import java.util.List;

import de.smeo.tools.exceptionmonitor.persistence.Identifiable;

public class Exception extends Identifiable {
	private final ExceptionStackTrace stackTrace; 
	private final String exceptionClassName;
	private final String exceptionComment;
	
	public Exception(String exceptionClassName, String  exceptionComment, List<String> stackTraceLines){
		super(createId(exceptionClassName, exceptionComment, stackTraceLines));
		this.exceptionClassName = exceptionClassName;
		this.exceptionComment = exceptionComment;
		this.stackTrace = new ExceptionStackTrace(stackTraceLines);
	}

	public boolean hasEqualRootCause(ExceptionCreator loggedException) {
		if (loggedException.getExceptionClassName().equals(exceptionClassName)){
			return stackTrace.hasEqualCharacteristics(stackTrace);
		}
		return false;
	}
	
	public List<String> getSourcePath() {
		return stackTrace.getSourcePath();
	}

	public String getExceptionClassName() {
		return exceptionClassName;
	}

	@Override
	public String toString() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(exceptionClassName);
		if (exceptionComment != null){
		   stringBuffer.append(":");
		   stringBuffer.append(exceptionComment);
		}
  	    
		stringBuffer.append("\n");

  	    for (String currStackLine : stackTrace.getLines()){
			stringBuffer.append("\t");
			stringBuffer.append(currStackLine);
			stringBuffer.append("\n");
		}
		return stringBuffer.toString();
	}

	
	private static String createId(String exceptionClassName,
			String exceptionComment, List<String> stackTraceLines) {
		List<String> sourcePath = new ExceptionStackTrace(stackTraceLines).getSourcePath();
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(exceptionClassName);
		stringBuffer.append(exceptionComment);
		for (String currSourceEntry : sourcePath){
			stringBuffer.append(currSourceEntry);
		}
		return ""+stringBuffer.toString().hashCode();
	}
}
