package de.smeo.tools.exceptionmonitor.exceptionparser;

import java.io.Serializable;
import java.util.List;


public class LoggedException implements Serializable {
	private ExceptionStackTrace stackTrace = new ExceptionStackTrace();
	private String exceptionClassName;
	private String comment;

	public LoggedException(String exceptionClassName) {
		this.exceptionClassName = exceptionClassName;
	}

	public String getExceptionClassName() {
		return exceptionClassName;
	}

	public ExceptionStackTrace getStackTrace() {
		return stackTrace;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public boolean hasEqualRootCause(LoggedException loggedException) {
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
		if (comment != null){
		   stringBuffer.append(":");
		   stringBuffer.append(comment);
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
