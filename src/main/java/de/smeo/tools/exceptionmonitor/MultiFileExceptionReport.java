package de.smeo.tools.exceptionmonitor;

import java.util.ArrayList;
import java.util.List;

public class MultiFileExceptionReport {
	private List<SingleFileExceptionReport> singleFileExecutionReports = new ArrayList<SingleFileExceptionReport>();

	public void addSingleFileReport(SingleFileExceptionReport singleFileExceptionReport) {
		singleFileExecutionReports.add(singleFileExceptionReport);		 
	}

	public boolean isEmpty() {
		for (SingleFileExceptionReport currSingleFileExceptionReport : singleFileExecutionReports){
			if (currSingleFileExceptionReport.getTotalNoOfExceptions() > 0){
				return false;
			}
		}
		return true;
	}

	public SingleFileExceptionReport getSingleFileReportForFilename(
			String logFilenName) {
		for (SingleFileExceptionReport currSingleFileExceptionReport : singleFileExecutionReports){
			if (currSingleFileExceptionReport.getMonitoredFile().getFilename().equals(logFilenName)){
				return currSingleFileExceptionReport;
			}
		}
		return null;
	}

	public List<SingleFileExceptionReport>  getSingleFileExceptionReports() {
		return singleFileExecutionReports;
	}

	public void prepareEmails(EmailOutBox emailOutBox) {
		for (SingleFileExceptionReport currSingleFileExceptionReport : singleFileExecutionReports){
			currSingleFileExceptionReport.prepareEmails(emailOutBox);
		}
	}
} 
  