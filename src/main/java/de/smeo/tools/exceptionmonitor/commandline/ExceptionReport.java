package de.smeo.tools.exceptionmonitor.commandline;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import de.smeo.tools.exceptionmonitor.exceptionparser.EqualCauseExceptionChainContainer;
import de.smeo.tools.exceptionmonitor.exceptionparser.EqualCauseExceptionContainerFactory;
import de.smeo.tools.exceptionmonitor.exceptionparser.ExceptionCausedByChain;
import de.smeo.tools.exceptionmonitor.exceptionparser.LogFileExceptionParser;

/**
 * Quick and dirty command line tool to get an overview about the exceptions within a logfile. 
 * The different types of exceptions are printed together with the occurance count, sorted by the 
 * number of occurances. 
 * 
 * @author smeo
 *
 */
public class ExceptionReport {
	List<NamedExceptionChain> namedExceptionChains = new ArrayList<ExceptionReport.NamedExceptionChain>();
	
	private class NamedExceptionChain implements Comparable<NamedExceptionChain>{
		private Boolean exceptionOccuringTheFirstTime;
		private String name;
		private EqualCauseExceptionChainContainer equalCauseExceptionChainContainer;

		public NamedExceptionChain(EqualCauseExceptionChainContainer exceptionChain) {
			this.equalCauseExceptionChainContainer = exceptionChain;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getShortDescription() {
			String shortDescription = "Exception[" + name + "/ count: "+equalCauseExceptionChainContainer.size()+"] - " + equalCauseExceptionChainContainer.getFirstExceptionName();
			if (exceptionOccuringTheFirstTime != null && exceptionOccuringTheFirstTime){
				shortDescription = "FIRST OCCURANCE - " + shortDescription; 
			}
			return shortDescription;
		}
		
		public String getFullDescription() {
			return "Exception[" + name + "/ count: "+equalCauseExceptionChainContainer.size()+"]\n" +
					equalCauseExceptionChainContainer.getSampleExceptionChain().toString();
		}
		
		
		public int compareTo(NamedExceptionChain o) {
			if (o.exceptionOccuringTheFirstTime != exceptionOccuringTheFirstTime){
				if (o.exceptionOccuringTheFirstTime){
					return 1;
				}
				return 0;
			}
			int result = (new Integer(o.equalCauseExceptionChainContainer.size()).compareTo(equalCauseExceptionChainContainer.size()));
			if (result == 0){
				(o.equalCauseExceptionChainContainer.getFirstExceptionName()).compareTo(equalCauseExceptionChainContainer.getFirstExceptionName());
			}
			return result;
		}

		public void setExceptionOccursTheFirstTime(boolean isExceptionsOccuringFirstTime) {
			this.exceptionOccuringTheFirstTime = isExceptionsOccuringFirstTime;
		}
	}
	
	public void createAndPrintReport(String logfilename){
		LogFileExceptionParser logFileExceptionParser = new LogFileExceptionParser();
		try {
			long start = System.currentTimeMillis();
			System.out.println("start parsing logfile: '"+logfilename+"'");
			logFileExceptionParser.parseFile(logfilename);
			System.out.println("finished parsing logfile, took "+ (System.currentTimeMillis() - start) +" ms");
		
			List<ExceptionCausedByChain> exceptionChains = logFileExceptionParser.getExceptionChains();
			addExceptionChains(exceptionChains);
			
			System.out.println(this.toString());

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	
	private void addExceptionChains(List<ExceptionCausedByChain> exceptionChains) {
		List<EqualCauseExceptionChainContainer> equalsCauseExceptionChainContainers = EqualCauseExceptionContainerFactory.createEqualCauseContainers(exceptionChains);
		addExceptionContainers(equalsCauseExceptionChainContainers, null);
	}


	public void addExceptionContainers(Collection<EqualCauseExceptionChainContainer> equalsCauseExceptionChainContainers, Boolean isExceptionsOccuringFirstTime) {
		for (EqualCauseExceptionChainContainer currEqualCauseExceptionChainContainer : equalsCauseExceptionChainContainers){
			NamedExceptionChain namedExceptionChain = new NamedExceptionChain(currEqualCauseExceptionChainContainer);
			if (isExceptionsOccuringFirstTime != null){
				namedExceptionChain.setExceptionOccursTheFirstTime(isExceptionsOccuringFirstTime);
			}
			namedExceptionChains.add(namedExceptionChain);
		}
		sortNamedChainsAndSetNames();
	}
	
	private void sortNamedChainsAndSetNames(){
		int i=1;
		Collections.sort(namedExceptionChains);
		for (NamedExceptionChain currExceptionChain : namedExceptionChains){
			currExceptionChain.setName("No." + i++);
		}
	}

	private String getFullDescription() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("----------------------------------------");
		stringBuffer.append("Full Description");
		stringBuffer.append("----------------------------------------");
		stringBuffer.append("Note: the exception stacktraces present the");
		stringBuffer.append("first occuring exception, the comments for ");
		stringBuffer.append("the other listed occurances can be different.");
		stringBuffer.append("----------------------------------------\n");
		for (NamedExceptionChain currExceptionChain : namedExceptionChains){
			stringBuffer.append(currExceptionChain.getFullDescription());
		}
		return stringBuffer.toString();
	}

	private String getShortDescription() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("----------------------------------------");
		stringBuffer.append("Short Description");
		stringBuffer.append("----------------------------------------\n");
		for (NamedExceptionChain currExceptionChain : namedExceptionChains){
			stringBuffer.append(currExceptionChain.getShortDescription());
			stringBuffer.append("\n");
		}
		return stringBuffer.toString();
	}
	
	@Override
	public String toString() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(getShortDescription());
		stringBuffer.append("\n\n\n");
		stringBuffer.append(getFullDescription());
		return stringBuffer.toString();
	}
	
	public static void main(String[] args) {
		String logfilename = args[0];
		
		ExceptionReport exceptionReport = new ExceptionReport();
		exceptionReport.createAndPrintReport(logfilename);
	}
}
