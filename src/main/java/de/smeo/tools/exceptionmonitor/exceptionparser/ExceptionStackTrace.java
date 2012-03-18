package de.smeo.tools.exceptionmonitor.exceptionparser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hibernate.annotations.Entity;

import de.smeo.tools.exceptionmonitor.persistence.Identifiable;

/**
 * Collection of lines containing the stack trace of the exception. Capable of 
 * extracting only the parts where source files and line position are specified. This information
 * is later used to compare stack traces and to find out if the exceptions have the same root cause.
 * @author smeo
 *
 */

public class ExceptionStackTrace extends Identifiable {
	private List<String> lines = new ArrayList<String>();

	public ExceptionStackTrace(List<String> stackTraceLines) {
		super(createId(stackTraceLines));
		this.lines = stackTraceLines;
	}
	
	public ExceptionStackTrace() {}

	public void addLine(String stackTraceMemberLine) {
		lines.add(stackTraceMemberLine);
	}

	public boolean hasEqualCharacteristics(ExceptionStackTrace stackTrace) {
		return true;
	}

	public List<String> getSourcePath() {
		return getSourceEntries(lines);
	}


	List<String> getLines() {
		return Collections.unmodifiableList(lines);
	}

	@Override
	public String toString() {
		return lines.toString();
	}

	private static List<String> getSourceEntries(List<String> lines) {
		List<String> sourcePath = new ArrayList<String>();
		for (String currStackTraceLine : lines){
			sourcePath.add(exctractSourceEntry(currStackTraceLine));
		}
		return sourcePath;
	}

	private static String exctractSourceEntry(String stackTraceLine) {
		if (stackTraceLine.contains("(") && stackTraceLine.contains(")")){
			return stackTraceLine.substring((stackTraceLine.indexOf("(")+1), stackTraceLine.indexOf(")"));
		}
		return "";
	}

	private static String createId(List<String> elements) {
		StringBuffer stringBuffer = new StringBuffer();
		for (String currElement : getSourceEntries(elements)){
			stringBuffer.append(currElement);
		}
		return ""+stringBuffer.toString().hashCode();
	}


}
