package de.smeo.tools.exceptionmonitor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MonitoredFile {
	private boolean isMonitored = true;
	private long checkInterval;
	private String filename;
	private Set<ReportedException> knownExceptions = new HashSet<ReportedException>();
	private Set<EmailAdress> defaultEmailReceivers = new HashSet<EmailAdress>();
	
	
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
	
	public Set<ReportedException> getKnownExceptions() {
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

	public List<EmailAdress> getDefaultEmailAdresses() {
		// TODO Auto-generated method stub
		return null;
	}


	
	
}
