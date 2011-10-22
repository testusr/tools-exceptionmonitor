package de.smeo.tools.exceptionmonitor;

import java.util.ArrayList;
import java.util.List;

public class FileMonitor {
	private List<SingleFileMonitor> singleFileMonitors = new ArrayList<SingleFileMonitor>();
	
	public void addMonitoredLogFile(MonitoredFile monitoredFile) {
		singleFileMonitors.add(new SingleFileMonitor(monitoredFile));
	}
	
	public void checkFilesIfNecessary(){
		for (SingleFileMonitor currSingleFileMonitor : singleFileMonitors){
			currSingleFileMonitor.checkFileIfNecessary();
		}
	}
	
	public MultiFileExceptionReport createExceptionReportUpdate(){
		MultiFileExceptionReport multiFileExceptionReport = new MultiFileExceptionReport();
		for (SingleFileMonitor currSingleFileMonitor : singleFileMonitors){
			multiFileExceptionReport.addSingleFileReport(currSingleFileMonitor.getExceptionsSinceLastUpdateAndReset());
		}
		return multiFileExceptionReport;
	}
	
	public MultiFileExceptionReport createOverTheDayExecutionReport(){
		MultiFileExceptionReport multiFileExceptionReport = new MultiFileExceptionReport();
		for (SingleFileMonitor currSingleFileMonitor : singleFileMonitors){
			multiFileExceptionReport.addSingleFileReport(currSingleFileMonitor.getAllDayExceptionReport());
		}
		return multiFileExceptionReport;
		
	}
}
