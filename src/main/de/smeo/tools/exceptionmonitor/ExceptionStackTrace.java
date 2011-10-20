package de.smeo.tools.exceptionmonitor;

import java.util.ArrayList;
import java.util.List;

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
