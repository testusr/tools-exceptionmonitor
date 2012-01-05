package de.smeo.tools.exceptionmonitor.reporting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.smeo.tools.exceptionmonitor.exceptionparser.ExceptionCausedByChain;
import de.smeo.tools.exceptionmonitor.reporting.SingleFileExceptionReport.ReportedExceptionOccurances;

public class ExceptionReport {
	private List<NamedException> namedExceptionChains = new ArrayList<NamedException>();
	
	
	public void addReportedExceptionOccurances(ReportedExceptionOccurances reportedExceptionOccurances){
		this.namedExceptionChains.add(new NamedException(reportedExceptionOccurances));
	}
	
	@Override
	public String toString() {
		sortNamedChainsAndSetNames();
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(getShortDescriptions());
		stringBuffer.append("\n\n\n");
		stringBuffer.append(getFullDescriptions());
		return stringBuffer.toString();
	}
	
	
	private String getFullDescriptions() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("----------------------------------------");
		stringBuffer.append("Full Description");
		stringBuffer.append("----------------------------------------");
		stringBuffer.append("Note: the exception stacktraces present the");
		stringBuffer.append("first occuring exception, the comments for ");
		stringBuffer.append("the other listed occurances can be different.");
		stringBuffer.append("----------------------------------------\n");
		for (NamedException currExceptionChain : namedExceptionChains){
			stringBuffer.append(currExceptionChain.getFullDescription());
		}
		return stringBuffer.toString();
	}

	private String getShortDescriptions() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("----------------------------------------");
		stringBuffer.append("Short Description");
		stringBuffer.append("----------------------------------------");
		for (NamedException currExceptionChain : namedExceptionChains){
			stringBuffer.append(currExceptionChain.getShortDescription());
		}
		return stringBuffer.toString();
	}

	private void sortNamedChainsAndSetNames(){
		int i=1;
		Collections.sort(namedExceptionChains);
		for (NamedException currExceptionChain : namedExceptionChains){
			currExceptionChain.setName("No." + i++);
		}
	}
	
	private static class NamedException implements Comparable<NamedException>{
		private String name;
		private ReportedExceptionOccurances reportedExceptionOccurances;
		
		public NamedException(
				ReportedExceptionOccurances reportedExceptionOccurances) {
			this.reportedExceptionOccurances = reportedExceptionOccurances;
		}

		public void setName(String name) {
			this.name = name;
		}

		private ExceptionCausedByChain getSampleExceptionChain() {
			return reportedExceptionOccurances.getReportedException().getSampleExceptionChain();
		}
		
		public String getShortDescription(){
			return getSampleExceptionChain().getFirstExceptionName();
		}
		
		public String getFullDescription(){
			return "Exception[" + name + "/ count: "+reportedExceptionOccurances.occuranceCount() +"]\n" +
					getSampleExceptionChain().toString();
		}
		
		public int compareTo(NamedException o) {
			int result = (new Integer(o.reportedExceptionOccurances.occuranceCount()).compareTo(o.reportedExceptionOccurances.occuranceCount()));
			if (result == 0){
				(o.getSampleExceptionChain().getFirstExceptionName()).compareTo(getSampleExceptionChain().getFirstExceptionName());
			}
			return result;
		}
	}
}
