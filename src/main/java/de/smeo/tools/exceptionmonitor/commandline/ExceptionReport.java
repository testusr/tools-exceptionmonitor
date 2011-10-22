package de.smeo.tools.exceptionmonitor.commandline;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.smeo.tools.exceptionmonitor.exceptionparser.EqualCauseExceptionChainContainer;
import de.smeo.tools.exceptionmonitor.exceptionparser.EqualCauseExceptionContainerFactory;
import de.smeo.tools.exceptionmonitor.exceptionparser.ExceptionCausedByChain;
import de.smeo.tools.exceptionmonitor.exceptionparser.LogFileExceptionParser;

public class ExceptionReport {
	List<NamedExceptionChain> namedExceptionChains = new ArrayList<ExceptionReport.NamedExceptionChain>();
	
	private class NamedExceptionChain implements Comparable<NamedExceptionChain>{
		private String name;
		private EqualCauseExceptionChainContainer equalCauseExceptionChainContainer;

		public NamedExceptionChain(EqualCauseExceptionChainContainer exceptionChain) {
			this.equalCauseExceptionChainContainer = exceptionChain;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getShortDescription() {
			return "Exception[" + name + "/ count: "+equalCauseExceptionChainContainer.size()+"] - " + equalCauseExceptionChainContainer.getFirstExceptionName();
		}
		
		public String getFullDescription() {
			return "Exception[" + name + "/ count: "+equalCauseExceptionChainContainer.size()+"]\n" +
					equalCauseExceptionChainContainer.getSampleExceptionChain().toString();
		}
		
		public int compareTo(NamedExceptionChain o) {
			int result = (new Integer(o.equalCauseExceptionChainContainer.size()).compareTo(equalCauseExceptionChainContainer.size()));
			if (result == 0){
				(o.equalCauseExceptionChainContainer.getFirstExceptionName()).compareTo(equalCauseExceptionChainContainer.getFirstExceptionName());
			}
			return result;
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
			List<EqualCauseExceptionChainContainer> equalsCauseExceptionChainContainers = EqualCauseExceptionContainerFactory.createEqualCauseContainers(exceptionChains);
			for (EqualCauseExceptionChainContainer currEqualCauseExceptionChainContainer : equalsCauseExceptionChainContainers){
				namedExceptionChains.add(new NamedExceptionChain(currEqualCauseExceptionChainContainer));
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
		System.out.println("Note: the exception stacktraces present the");
		System.out.println("first occuring exception, the comments for ");
		System.out.println("the other listed occurances can be different.");
		System.out.println("----------------------------------------\n");
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
