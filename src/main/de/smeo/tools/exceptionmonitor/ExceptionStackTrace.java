package de.smeo.tools.exceptionmonitor;

import java.util.ArrayList;
import java.util.List;

public class ExceptionStackTrace {
	private List<String> lines = new ArrayList<String>();

	public void addLine(String stackTraceMemberLine) {
		lines.add(stackTraceMemberLine);
	}

	@Override
	public String toString() {
		return lines.toString();
	}
}
