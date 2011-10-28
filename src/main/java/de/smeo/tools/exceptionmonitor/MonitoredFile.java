package de.smeo.tools.exceptionmonitor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MonitoredFile {
	private String filename;
	private boolean isMonitored = true;
	private long checkInterval;
	private Set<EmailAdress> defaultEmailReceivers = new HashSet<EmailAdress>();
	private List<ReportedException> knownExceptions = new ArrayList<ReportedException>();
	
	
	public MonitoredFile(long checkInterval, String filename) {
		super();
		this.checkInterval = checkInterval;
		this.filename = filename;
	}

	public boolean isMonitored() {
		return isMonitored;
	}

	public void setMonitored(boolean isMonitored) {
		this.isMonitored = isMonitored;
	}

	public long getCheckInterval() {
		return checkInterval;
	}

	public void setCheckInterval(long checkInterval) {
		this.checkInterval = checkInterval;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	public List<ReportedException> getKnownExceptions() {
		return knownExceptions;
	}

	public void addNewExceptions(List<ReportedException> newExceptions) {
		knownExceptions.addAll(newExceptions); 
	}

	@Override
	public String toString() {
		return "MonitoredFile [isMonitored=" + isMonitored + ", filename="
				+ filename + "]";
	}

	public Set<EmailAdress> getDefaultEmailAdresses() {
		return defaultEmailReceivers;
	}


	public int getIndexForException(ReportedException reportedException){
		int index=-1;
		for (ReportedException currException : knownExceptions){
			index++;
			if (currException.hasEqualRootCause(reportedException)){
				return index;
			}
		}
		return index;
	}
	
	public int addKnownException(ReportedException reportedException) {
		int  index = getIndexForException(reportedException);
		if (index == -1){
			knownExceptions.add(reportedException);
			return knownExceptions.size()-1;
		}
		return index;
	}

	public void addDefaultEmailAdress(EmailAdress emailAdress) {
		defaultEmailReceivers.add(emailAdress);
	}

	public ReportedException getExceptionWithIndex(int index) {
		return knownExceptions.get(index);
	}


	
	
}
