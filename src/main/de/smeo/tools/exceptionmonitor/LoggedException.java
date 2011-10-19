package de.smeo.tools.exceptionmonitor;

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

}
