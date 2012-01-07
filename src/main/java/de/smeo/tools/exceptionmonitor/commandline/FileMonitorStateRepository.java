package de.smeo.tools.exceptionmonitor.commandline;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import de.smeo.tools.exceptionmonitor.monitor.SingleFileMonitor.FileMonitorState;

/**
 * Storage of the current state of a SingleFileMonitors
 * @author smeo
 *
 */
public class FileMonitorStateRepository {
	private Map<String, FileMonitorState> filenameToMonitorState = new HashMap<String, FileMonitorState>();

	public FileMonitorStateRepository(String string) {
		// TODO Auto-generated constructor stub
	}

	public FileMonitorState loadFileMonitorState(File logFile){
		return loadFileMonitorState(logFile.getAbsolutePath());
	}
	
	public FileMonitorState loadFileMonitorState(String logFileAbsolutePath) {
		FileMonitorState fileMonitorState = filenameToMonitorState.get(logFileAbsolutePath);
		if (fileMonitorState == null){
			fileMonitorState = new FileMonitorState();
			filenameToMonitorState.put(logFileAbsolutePath, fileMonitorState);
		}
		return fileMonitorState;
	}

	public void updateFileMonitorState(File logFile,
			FileMonitorState fileMonitorState) {
		filenameToMonitorState.put(logFile.getAbsolutePath(), fileMonitorState);
	}

}
