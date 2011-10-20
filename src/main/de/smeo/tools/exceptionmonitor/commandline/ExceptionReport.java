package de.smeo.tools.exceptionmonitor.commandline;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.smeo.tools.exceptionmonitor.ExceptionCausedByChain;
import de.smeo.tools.exceptionmonitor.LogFileExceptionParser;

public class ExceptionReport {
	List<NamedExceptionChain> namedExceptionChains = new ArrayList<ExceptionReport.NamedExceptionChain>();
	
	private class NamedExceptionChain implements Comparable<NamedExceptionChain>{
		private String name;
		private ExceptionCausedByChain exceptionChain;

		public NamedExceptionChain(ExceptionCausedByChain exceptionChain) {
			this.exceptionChain = exceptionChain;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getShortDescription() {
			return "Exception[" + name + "/ count: "+exceptionChain.getExceptionCount()+"] - " + exceptionChain.getExceptions().get(0).getExceptionClassName();
		}
		
		public String getFullDescription() {
			return "Exception[" + name + "]\n" +
					exceptionChain.toString();
		}
		
		public int compareTo(NamedExceptionChain o) {
			return (new Integer(o.exceptionChain.getExceptions().size()).compareTo(exceptionChain.getExceptions().size()));
		}
	}
	
	public void createReport(String logfilename){
		LogFileExceptionParser logFileExceptionParser = new LogFileExceptionParser();
		try {
			long start = System.currentTimeMillis();
			System.out.println("start parsing logfile: '"+logfilename+"'");
			logFileExceptionParser.parseFile(logfilename);
			System.out.println("finished parsing logfile, took "+ (System.currentTimeMillis() - start) +" ms");
		
			List<ExceptionCausedByChain> exceptionChains = logFileExceptionParser.getExceptionChains();
			for (ExceptionCausedByChain currExceptionChain : exceptionChains){
				namedExceptionChains.add(new NamedExceptionChain(currExceptionChain));
			}
			
			sortNamedChainsAndSetNames();
			printShortDescription();
			System.out.println("\n\n\n");
			printFullDescription();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	private void sortNamedChainsAndSetNames(){
		int i=1;
		Collections.sort(namedExceptionChains);
		for (NamedExceptionChain currExceptionChain : namedExceptionChains){
			currExceptionChain.setName("No." + i++);
		}
	}

	private void printFullDescription() {
		System.out.println("----------------------------------------");
		System.out.println("Full Description");
		System.out.println("----------------------------------------");
		for (NamedExceptionChain currExceptionChain : namedExceptionChains){
			System.out.println(currExceptionChain.getFullDescription());
		}
	}

	private void printShortDescription() {
		System.out.println("----------------------------------------");
		System.out.println("Short Description");
		System.out.println("----------------------------------------");
		for (NamedExceptionChain currExceptionChain : namedExceptionChains){
			System.out.println(currExceptionChain.getShortDescription());
		}
	}
	
	public static void main(String[] args) {
		String logfilename = args[0];
		
		ExceptionReport exceptionReport = new ExceptionReport();
		exceptionReport.createReport(logfilename);
	}
}
