package de.smeo.tools.exceptionmonitor;

import java.util.List;

public class LoggedException {
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
		return exceptionClassName;
	}
}
