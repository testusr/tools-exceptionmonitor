package de.smeo.tools.exceptionmonitor.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.smeo.tools.exceptionmonitor.common.XmlUtils;
import de.smeo.tools.exceptionmonitor.exceptionparser.ExceptionChain;
import de.smeo.tools.exceptionmonitor.exceptionparser.ExceptionOccuranceRecord;

/**
 * Storage for know exceptions and source for statistics etc
 * @author smeo
 */
public class XmlFileBasedExceptionDatabase extends ExceptionDatabase {
	private File storageFile;
	
	public XmlFileBasedExceptionDatabase(String storageFile) {
		openOrCreateFile(storageFile);
		loadStorageFromFile(); 
	}

	private void loadStorageFromFile() {
		exceptionDataBase = (List<FileExceptionContainer>) XmlUtils.loadObjectFromXmlFile(storageFile);
		if (exceptionDataBase == null){
			exceptionDataBase = new ArrayList<FileExceptionContainer>();
		}
	}
	
	public void saveStorageToFile() {
		XmlUtils.writeObjectToXmlFile(exceptionDataBase, storageFile);
	}
	
	private void openOrCreateFile(String storageFileName) {
		storageFile = new File(storageFileName);
		if (!storageFile.exists()){
			try {
				storageFile.createNewFile();
				saveStorageToFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void updateDatabase(List<ExceptionOccuranceRecord> newExceptions) {
		for (ExceptionOccuranceRecord currExceptionRecord : newExceptions){
			FileExceptionContainer fileExceptionContainer = getOrCreateFileExceptionContainer(currExceptionRecord.getFilename());
			fileExceptionContainer.addExceptionRecord(currExceptionRecord);
		}
	}
	
	FileExceptionContainer getOrCreateFileExceptionContainer(String filename){
		for (FileExceptionContainer currExceptionContainer : exceptionDataBase){
			if (currExceptionContainer.getAbsoluteFilePath().equals(filename)){
				return currExceptionContainer;
			}
		}
		FileExceptionContainer newFileExceptionContainer = new FileExceptionContainer(filename);
		exceptionDataBase.add(newFileExceptionContainer);
		return newFileExceptionContainer;
	}

}
