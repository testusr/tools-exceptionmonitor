package de.smeo.tools.exceptionmonitor;

import java.util.List;

public class SingleFileMonitor {
	private long lastFileCheckTime = -1;
	private ExceptionParser exceptionParser = new ExceptionParser();
	private MonitoredFile monitoredFile;
	private SingleFileExceptionReport allDayExceptionReport = new SingleFileExceptionReport();
	private SingleFileExceptionReport exceptionReportSinceLastUpdate = new SingleFileExceptionReport();
	
	public SingleFileMonitor(MonitoredFile monitoredFile) {
		this.monitoredFile = monitoredFile;
	}

	public synchronized List<ExceptionCausedByChain> checkFile() 
	{
		lastFileCheckTime = getCurrTime();
		exceptionParser.clean();
		String nextLogFileChunk = null;
		while ( (nextLogFileChunk = getNextLogFileChunk()) != null){
			exceptionParser.parse(nextLogFileChunk);
		}
		List<EqualCauseExceptionChainContainer> exceptionChains = exceptionParser.getExceptionGroupedByRootCause();
		
		allDayExceptionReport.addExceptions(exceptionChains);
		exceptionReportSinceLastUpdate.addExceptions(exceptionChains);
		
		return exceptionParser.getExceptionChains();
	}
	
	protected String getNextLogFileChunk() {
		return null;
	}

	public synchronized void checkFileIfNecessary() {
		if ((lastFileCheckTime < 0) || (getFileCheckInterval() < 0) || (lastFileCheckTime + getFileCheckInterval()) <= getCurrTime()){
			checkFile();
		}
	}
	
	public SingleFileExceptionReport getExceptionsSinceLastUpdateAndReset() {
		SingleFileExceptionReport exceptionsSinceLastUpdate = this.exceptionReportSinceLastUpdate;
		this.exceptionReportSinceLastUpdate = new SingleFileExceptionReport();
		return exceptionsSinceLastUpdate;
	}
	
	
	private long getFileCheckInterval() {
		return monitoredFile.getCheckInterval();
	}

	public long getLastFileCheckTime() {
		return this.lastFileCheckTime;
	}
	
	protected long getCurrTime() {
		return System.currentTimeMillis();
	}
	
}
