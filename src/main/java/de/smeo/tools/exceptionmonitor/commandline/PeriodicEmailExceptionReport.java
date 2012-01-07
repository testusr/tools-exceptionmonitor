package de.smeo.tools.exceptionmonitor.commandline;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.smeo.tools.exceptionmonitor.commandline.ExceptionDatabase.CategorizedExceptions;
import de.smeo.tools.exceptionmonitor.exceptionparser.ExceptionCausedByChain;
import de.smeo.tools.exceptionmonitor.monitor.SingleFileMonitor;
import de.smeo.tools.exceptionmonitor.monitor.SingleFileMonitor.FileMonitorState;

public class PeriodicEmailExceptionReport {
	private static final String FILE_MONITOR_STATES = "FileMonitorStates.cfg";
	private static final String EXCEPTION_DATABASE = "ExceptionDataBase.cfg";
	
	private List<String> logFilesToParse = new ArrayList<String>();
	private List<CategorizedExceptions> allCategorizedExceptions = new ArrayList<CategorizedExceptions>();
	private String configDirectory;
	private FileMonitorStateRepository fileMonitorStates;
	private ExceptionDatabase exceptionDatabase;
	
	
	private void parseLogFiles() {
		for (String currLogFileToParsePath : logFilesToParse){
			File currLogFileToParse = new File(currLogFileToParsePath);
			FileMonitorState fileMonitorState = fileMonitorStates.loadFileMonitorState(currLogFileToParse);
			SingleFileMonitor singleFileMonitor = new SingleFileMonitor(currLogFileToParse);
			singleFileMonitor.setFileMonitorState(fileMonitorState);
			List<ExceptionCausedByChain> newExceptions = singleFileMonitor.parseNewFileEntriesAndReturnExceptions();
			fileMonitorStates.updateFileMonitorState(currLogFileToParse, singleFileMonitor.getFileMonitorState());
			CategorizedExceptions categorizedExceptions = exceptionDatabase.updateDatabaseAndCategorizeExceptions(currLogFileToParse, newExceptions);
			allCategorizedExceptions.add(categorizedExceptions);
		}
	}
	
	private void parseFileListCommand(String string) {
		// TODO Auto-generated method stub
		
	}

	private void createTemplateConfigsForNonExisting() {
		// TODO Auto-generated method stub
		
	}

	private boolean loadConfigs(String configDirectory) {
		this.configDirectory = configDirectory + File.separatorChar;
		this.fileMonitorStates = new FileMonitorStateRepository(configDirectory + FILE_MONITOR_STATES);
		this.exceptionDatabase = new ExceptionDatabase(configDirectory + EXCEPTION_DATABASE);
		return true;
	}

	private void sendEmailReports() {
		// TODO Auto-generated method stub
		
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
		PeriodicEmailExceptionReport periodicEmailExceptionReport = new PeriodicEmailExceptionReport();
		
		if (args.length != 2){
			PeriodicEmailExceptionReport.printHelpText();
		} else {
			boolean configsLoaded = periodicEmailExceptionReport.loadConfigs(args[1]);
			if (configsLoaded){
				periodicEmailExceptionReport.createTemplateConfigsForNonExisting();
			} else {
				periodicEmailExceptionReport.parseFileListCommand(args[0]);
				periodicEmailExceptionReport.parseLogFiles();
				periodicEmailExceptionReport.sendEmailReports();
			}
		}
		
	}
	
}
