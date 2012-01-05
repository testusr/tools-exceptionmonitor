package de.smeo.tools.exceptionmonitor.exceptionparser;

import java.util.ArrayList;
import java.util.List;

/**
 * Collection of lines containing the stack trace of the exception. Capable of 
 * extracting only the parts where source files and line position are specified. This information
 * is later used to compare stack traces and to find out if the exceptions have the same root cause.
 * @author smeo
 *
 */
public class ExceptionStackTrace {
	private List<String> lines = new ArrayList<String>();

	public void addLine(String stackTraceMemberLine) {
		lines.add(stackTraceMemberLine);
	}

	public boolean hasEqualCharacteristics(ExceptionStackTrace stackTrace) {
		return true;
	}

	public List<String> getSourcePath() {
		List<String> sourcePath = new ArrayList<String>();
		for (String currStackTraceLine : lines){
			sourcePath.add(exctractSourceEntry(currStackTraceLine));
		}
		return sourcePath;
	}

	private String exctractSourceEntry(String stackTraceLine) {
		if (stackTraceLine.contains("(") && stackTraceLine.contains(")")){
			return stackTraceLine.substring((stackTraceLine.indexOf("(")+1), stackTraceLine.indexOf(")"));
		}
		return "";
	}

	List<String> getLines() {
		return lines;
	}

	@Override
	public String toString() {
		return lines.toString();
	}


}
