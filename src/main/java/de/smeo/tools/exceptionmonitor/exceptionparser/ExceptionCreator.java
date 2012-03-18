package de.smeo.tools.exceptionmonitor.exceptionparser;

import java.util.ArrayList;
import java.util.List;

import de.smeo.tools.exceptionmonitor.domain.Exception;


public class ExceptionCreator {
	private List<String> stackTraceLines = new ArrayList<String>();
	private String exceptionClassName;
	private String exceptionComment;

	public ExceptionCreator(String exceptionClassName) {
		this.exceptionClassName = exceptionClassName;
	}

	public String getExceptionClassName() {
		return exceptionClassName;
	}
	public void setExceptionComment(String comment) {
		this.exceptionComment = comment;
	}
	
	public Exception createException(){
		return new Exception(exceptionClassName, exceptionComment, stackTraceLines);
	}

	public void addLine(String stackTraceLine) {
		stackTraceLines.add(stackTraceLine);
	}

	@Override
	public String toString() {
		return createException().toString();
	}
}
