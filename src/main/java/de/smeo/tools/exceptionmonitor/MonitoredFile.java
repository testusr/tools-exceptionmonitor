package de.smeo.tools.exceptionmonitor;

import java.util.HashSet;
import java.util.Set;

public class MonitoredFile {
	private boolean isMonitored = true;
	private long checkInterval;
	private String filename;
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
}