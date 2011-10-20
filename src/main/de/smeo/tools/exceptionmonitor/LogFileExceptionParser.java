package de.smeo.tools.exceptionmonitor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LogFileExceptionParser extends ExceptionParser {
	
	LogFileExceptionParser parseFile(String logfilename) throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader(logfilename));
				
		String nextLine = null;
		while ((nextLine = reader.readLine()) != null) {
				parseLine(nextLine);
		}
				
		reader.close();
		flush();
		return this;
	}
}
