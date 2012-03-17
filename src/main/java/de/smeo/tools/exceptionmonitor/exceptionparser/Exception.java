package de.smeo.tools.exceptionmonitor.exceptionparser;

import java.io.Serializable;
import java.util.List;


public class Exception implements Serializable {
	private ExceptionStackTrace stackTrace = new ExceptionStackTrace();
	private String exceptionClassName;
	private String exceptionComment;

	public Exception(String exceptionClassName) {
		this.exceptionClassName = exceptionClassName;
	}

	public String getExceptionClassName() {
		return exceptionClassName;
	}
	public void setExceptionComment(String comment) {
		this.exceptionComment = comment;
	}
	
	public ExceptionStackTrace getStackTrace() {
		return stackTrace;
	}

	public boolean hasEqualRootCause(Exception loggedException) {
		if (loggedException.getExceptionClassName().equals(exceptionClassName)){
			return stackTrace.hasEqualCharacteristics(loggedException.stackTrace);
		}
		return false;
	}
	
	public List<String> getSourcePath() {
		return stackTrace.getSourcePath();
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
}
