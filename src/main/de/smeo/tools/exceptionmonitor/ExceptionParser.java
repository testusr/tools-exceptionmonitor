package de.smeo.tools.exceptionmonitor;

import java.util.List;

public class ExceptionParser {
	private String previousLine = null;
	
	public ExceptionParser() {
		// TODO Auto-generated constructor stub
	}

	public void parse() {
		// TODO Auto-generated method stub
		
	}

	public List<ExceptionChain> getExceptionChains() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<LoggedException> getExceptions() {
		// TODO Auto-generated method stub
		return null;
	}

	public void parse(String exceptionText) {
		String[] textSplitToLines = exceptionText.split("\n");
		for (String currLine : textSplitToLines){
			parseLine(currLine);
		}
	}

	private void parseLine(String currLine) {
		String trimmedLine = currLine.trim();
		
	}

}
