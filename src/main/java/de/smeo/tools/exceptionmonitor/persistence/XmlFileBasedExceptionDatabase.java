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
	
	private void saveToFile() {
		XmlUtils.writeObjectToXmlFile(exceptionDataBase, storageFile);
	}
	
	private void openOrCreateFile(String storageFileName) {
		storageFile = new File(storageFileName);
		if (!storageFile.exists()){
			try {
				storageFile.createNewFile();
				saveToFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void persist(List<FileExceptionContainer> exceptionDataBase) {
		saveToFile();
	}
}
