package de.smeo.tools.exceptionmonitor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import de.smeo.tools.exceptionmonitor.SingleFileExceptionReport.ReportedExceptionOccurances;

public class MultiFileMonitor {
	private Timer fileCheckTimer = new Timer();
	
	private List<SingleFileMonitor> singleFileMonitors = new ArrayList<SingleFileMonitor>();
	private TimerTask checkFilesForExceptionsTask = null;
	
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
			SingleFileExceptionReport singleFileExceptionReport= currSingleFileMonitor.getExceptionsSinceLastUpdateAndReset();
			if (singleFileExceptionReport.getTotalNoOfExceptions() > 0){
				multiFileExceptionReport.addSingleFileReport(singleFileExceptionReport);
			}
			
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
	

	public void startToMonitor(){
		if ( checkFilesForExceptionsTask == null){
			checkFilesForExceptionsTask = new TimerTask() {
				
				@Override
				public void run() {
					try {
						checkFilesIfNecessary();
						MultiFileExceptionReport multiFileExceptionReport = createExceptionReportUpdate();
						sendEmailsIfNecessary(multiFileExceptionReport);
					} catch (Exception e){
						e.printStackTrace();
					}
				}
			};
		}
		fileCheckTimer.schedule(checkFilesForExceptionsTask, 1000*60*5);
	}

	protected void sendEmailsIfNecessary(MultiFileExceptionReport multiFileExceptionReport) {
		EmailOutBox emailOutBox = getNewEmailOutBox();
		multiFileExceptionReport.addMultiFileExceptionReport(multiFileExceptionReport);
		multiFileExceptionReport.prepareEmails(emailOutBox);
		emailOutBox.sendEmails();
	}

	protected EmailOutBox getNewEmailOutBox() {
		return new EmailOutBox();
	}

}
