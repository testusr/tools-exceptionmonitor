package de.smeo.tools.exceptionmonitor.commandline;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.smeo.tools.exceptionmonitor.domain.ExceptionChain;
import de.smeo.tools.exceptionmonitor.domain.ExceptionOccuranceRecord;
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
		private ExceptionChain exceptionChain;
		private Integer occuranceCount;

		public NamedExceptionChain(ExceptionChain exceptionChain, int occuranceCount) {
			this.exceptionChain = exceptionChain;
			this.occuranceCount = occuranceCount;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getShortDescription() {
			String shortDescription = "Exception[" + name + "/ count: "+occuranceCount+"] - " + exceptionChain.getFirstExceptionName();
			if (exceptionOccuringTheFirstTime != null && exceptionOccuringTheFirstTime){
				shortDescription = "FIRST OCCURANCE - " + shortDescription; 
			}
			return shortDescription;
		}
		
		public String getFullDescription() {
			return "Exception[" + name + "/ count: "+ occuranceCount +"]\n" +
					exceptionChain.toString();
		}
		
		
		public int compareTo(NamedExceptionChain o) {
			if (o.exceptionOccuringTheFirstTime != exceptionOccuringTheFirstTime){
				if (o.exceptionOccuringTheFirstTime){
					return 1;
				}
				return 0;
			}
			int result = (o.occuranceCount).compareTo(occuranceCount);
			if (result == 0){
				(o.exceptionChain.getFirstExceptionName()).compareTo(exceptionChain.getFirstExceptionName());
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
		
			List<ExceptionOccuranceRecord> exceptionRecords = logFileExceptionParser.getExceptionOccuranceRecords();
			addExceptionChains(exceptionRecords);
			
			System.out.println(this.toString());

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	
	private void addExceptionChains(List<ExceptionOccuranceRecord> exceptionRecords) {
		Map<ExceptionChain, List<ExceptionOccuranceRecord>> recordsToChain = groupRecordsByExceptionChain(exceptionRecords);
		addExceptionContainers(recordsToChain, null);
	}


	private Map<ExceptionChain, List<ExceptionOccuranceRecord>> groupRecordsByExceptionChain(List<ExceptionOccuranceRecord> exceptionRecords) {
		Map<ExceptionChain, List<ExceptionOccuranceRecord>> groupedExceptionRecords = new HashMap<ExceptionChain, List<ExceptionOccuranceRecord>>();
		for (ExceptionOccuranceRecord currExceptionOccuranceRecord : exceptionRecords){
			ExceptionChain currExceptionChain = currExceptionOccuranceRecord.getExceptionChain();
			List<ExceptionOccuranceRecord> exceptionRecordsForExceptionChain = groupedExceptionRecords.get(currExceptionChain);
			if (exceptionRecordsForExceptionChain == null){
				exceptionRecordsForExceptionChain = new ArrayList<ExceptionOccuranceRecord>();
				groupedExceptionRecords.put(currExceptionChain, exceptionRecordsForExceptionChain);
			}
			exceptionRecordsForExceptionChain.add(currExceptionOccuranceRecord);
		}
		return groupedExceptionRecords;
	}


	public void addExceptionContainers(Map<ExceptionChain, List<ExceptionOccuranceRecord>> recordsToChain, Boolean  isFistOccurance) {
		for (Entry<ExceptionChain,  List<ExceptionOccuranceRecord>> currEqualCauseExceptionChainContainer : recordsToChain.entrySet()){
			NamedExceptionChain namedExceptionChain = new NamedExceptionChain(currEqualCauseExceptionChainContainer.getKey(), currEqualCauseExceptionChainContainer.getValue().size());
			namedExceptionChains.add(namedExceptionChain);
			if (isFistOccurance != null){
				namedExceptionChain.setExceptionOccursTheFirstTime(isFistOccurance);
			}
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
		stringBuffer.append("----------------------------------------\n");
		stringBuffer.append("Full Description\n");
		stringBuffer.append("----------------------------------------\n");
		stringBuffer.append("Note: the exception stacktraces present the\n");
		stringBuffer.append("first occuring exception, the comments for \n");
		stringBuffer.append("the other listed occurances can be different.\n");
		stringBuffer.append("----------------------------------------\n");
		for (NamedExceptionChain currExceptionChain : namedExceptionChains){
			stringBuffer.append("\n");
			stringBuffer.append(currExceptionChain.getFullDescription());
		}
		return stringBuffer.toString();
	}

	private String getShortDescription() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("----------------------------------------\n");
		stringBuffer.append("Short Description\n");
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
