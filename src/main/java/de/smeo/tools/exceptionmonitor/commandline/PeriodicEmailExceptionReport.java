package de.smeo.tools.exceptionmonitor.commandline;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import de.smeo.tools.exceptionmonitor.commandline.ExceptionDatabase.CategorizedExceptions;
import de.smeo.tools.exceptionmonitor.exceptionparser.ExceptionCausedByChain;
import de.smeo.tools.exceptionmonitor.monitor.SingleFileMonitor;
import de.smeo.tools.exceptionmonitor.monitor.SingleFileMonitor.FileMonitorState;

public class PeriodicEmailExceptionReport {
	private static final String FILE_MONITOR_STATES = "FileMonitorStates.cfg";
	private static final String EXCEPTION_DATABASE = "ExceptionDataBase.cfg";
	private static final String EMAIL_SERVER = "EmailSettings.properties";
	
	private List<String> logFilesToParse = new ArrayList<String>();
	private Map<File, CategorizedExceptions> foundExceptionsToLogfile = new HashMap<File, CategorizedExceptions>();
	private String configDirectory;
	private FileMonitorStateRepository fileMonitorStates;
	private ExceptionDatabase exceptionDatabase;
	private EmailDispatcher emailDispatcher;
	private String targetEmailAdress;
	
	

	public PeriodicEmailExceptionReport(String configDirectory) {
		loadConfigs(configDirectory);
	}
	
	
	private void parseLogFiles() {
		for (String currLogFileToParsePath : logFilesToParse){
			parseLogFile(new File(currLogFileToParsePath));
		}
	}
	
	private void parseFileListCommand(String commaSeparatedLogFileList) {
		String[] filenames = commaSeparatedLogFileList.split(",");
		
		for (String currFilename : filenames){
			logFilesToParse.add(currFilename);
		}
		
	}
	
	private void parseLogFile(File logFile){
		if (logFile.exists()){
			if (!foundExceptionsToLogfile.containsKey(logFile)){
				SingleFileMonitor singleFileMonitor = new SingleFileMonitor(logFile);
				singleFileMonitor.setFileMonitorState(fileMonitorStates.loadFileMonitorState(logFile));
				List<ExceptionCausedByChain> foundExceptions = singleFileMonitor.parseNewFileEntriesAndReturnExceptions();
				CategorizedExceptions foundExeptions = exceptionDatabase.updateDatabaseAndCategorizeExceptions(logFile, foundExceptions);
				foundExceptionsToLogfile.put(logFile, foundExeptions);
			} 
		}
	}

	private boolean loadConfigs(String configDirectory) {
		this.configDirectory = configDirectory + File.separatorChar;
		this.fileMonitorStates = new FileMonitorStateRepository(configDirectory + File.separator +  FILE_MONITOR_STATES);
		this.exceptionDatabase = new ExceptionDatabase(configDirectory + File.separator + EXCEPTION_DATABASE);
		this.emailDispatcher = new EmailDispatcher(configDirectory + File.separator + EMAIL_SERVER);
		return true;
	}
	
	private void saveCurrentStateToFiles() {
		this.fileMonitorStates.saveToFile();
		this.exceptionDatabase.saveStorageToFile();
	}

	private void sendEmailReports() throws MessagingException {
		for (File currLogFile : foundExceptionsToLogfile.keySet()){
			CategorizedExceptions categorizedExceptions = foundExceptionsToLogfile.get(currLogFile);
			ExceptionReport newExceptionReport = new ExceptionReport();
			newExceptionReport.addExceptionContainers(categorizedExceptions.getYetUnknownExceptions(), true);
			newExceptionReport.addExceptionContainers(categorizedExceptions.getKnownExceptions(), false);
			String subject = "ExceptionReport: " + currLogFile.getName();
			if (categorizedExceptions.hasYetUnkownExceptions()){
				subject += "YET UNKOWN EXCEPTIONS in " + subject;
			}
			emailDispatcher.sendEmail(targetEmailAdress, subject, newExceptionReport.toString());
		}
	}
	
	
	public static void printHelpText(){
		System.out.println(
				"ShortDescription:\n" +
				"The tool is mean to be called periodically, manually or by a cron script. The tool will\n" +
				"parse the specified logfiles, create rejection reports end send out emails depending on its \n" +
				"configuration. (see config directory section)\n" +
				"\n" +
				"Syntax:\n" +
				"PeriodicEmailExceptionReport <files> <config directory>\n" +
				"\n" +
				"files - files is a comma separated list of files to, wildcard * is supported\n" +
				"config directory - the directory where the tool will store its databases and config files\n" +
				"       there are configurations that are meant to be adapted by the user.\n" +
				"		With the first call of the tool it will not perform any logfile parsing but creating " +
		"		logfile templates.\n\n");
	}
	
	public static void main(String[] args) {
		
		if (args.length != 2){
			PeriodicEmailExceptionReport.printHelpText();
		} else {
				PeriodicEmailExceptionReport periodicEmailExceptionReport = new PeriodicEmailExceptionReport(args[0]);
				periodicEmailExceptionReport.parseFileListCommand(args[1]);
				periodicEmailExceptionReport.parseLogFiles();
				try {
					periodicEmailExceptionReport.sendEmailReports();
					
					// only save states if emails where send successfully
					periodicEmailExceptionReport.saveCurrentStateToFiles();
				} catch (MessagingException e) {
					e.printStackTrace();
				}
			}
		}
}
