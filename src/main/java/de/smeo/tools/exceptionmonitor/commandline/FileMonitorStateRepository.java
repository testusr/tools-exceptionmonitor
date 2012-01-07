package de.smeo.tools.exceptionmonitor.commandline;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.smeo.tools.exceptionmonitor.common.FileUtils;
import de.smeo.tools.exceptionmonitor.monitor.SingleFileMonitor.FileMonitorState;

/**
 * Storage of the current state of a SingleFileMonitors
 * @author smeo
 *
 */
public class FileMonitorStateRepository {
	private Map<String, FileMonitorState> filenameToMonitorState = new HashMap<String, FileMonitorState>();
	private File storageFile;
	
	public FileMonitorStateRepository(String configfile) {
		openOrCreateFile(configfile);
		loadFileMonitorStatesFromFile(configfile);
	}

	private void loadFileMonitorStatesFromFile(String configfile2) {
		this.filenameToMonitorState = (Map<String, FileMonitorState>) FileUtils.readObjectFromFile(storageFile);
	}

	public void saveToFile() {
		FileUtils.writeObjectToFile(filenameToMonitorState, storageFile);
	}
	
	private void openOrCreateFile(String storageFileName) {
		this.storageFile = new File(storageFileName);
		if (!storageFile.exists()){
			try {
				storageFile.createNewFile();
				saveToFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
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

	public void updateLogFilesMonitoringState(File logFile,
			FileMonitorState fileMonitorState) {
		filenameToMonitorState.put(logFile.getAbsolutePath(), fileMonitorState);
	}
}
