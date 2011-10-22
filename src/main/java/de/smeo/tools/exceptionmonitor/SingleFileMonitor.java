package de.smeo.tools.exceptionmonitor;

import java.util.List;

public class SingleFileMonitor {
	private long lastFileCheckTime = -1;
	private ExceptionParser exceptionParser = new ExceptionParser();
	private long fileCheckIntervalMs = -1;

	public List<ExceptionCausedByChain> checkFile() 
	{
		lastFileCheckTime = getCurrTime();
		exceptionParser.clean();
		String nextLogFileChunk = null;
		while ( (nextLogFileChunk = getNextLogFileChunk()) != null){
			exceptionParser.parse(nextLogFileChunk);
		}
		
		return exceptionParser.getExceptionChains();
	}
	
	protected String getNextLogFileChunk() {
		return null;
	}

	public void checkFileIfNecessary() {
		if (lastFileCheckTime < 0 || (lastFileCheckTime + getFileCheckInterval()) <= getCurrTime()){
			checkFile();
		}
	}
	
	public void setFileCheckIntervalMs(long fileCheckIntervalMs) {
		this.fileCheckIntervalMs = fileCheckIntervalMs;
	}

	private long getFileCheckInterval() {
		return fileCheckIntervalMs;
	}

	public long getLastFileCheckTime() {
		return this.lastFileCheckTime;
	}
	
	protected long getCurrTime() {
		return System.currentTimeMillis();
	}
	
	
}
