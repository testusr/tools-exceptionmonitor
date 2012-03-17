package de.smeo.tools.exceptionmonitor.common;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.smeo.tools.exceptionmonitor.exceptionparser.ExceptionChain;
import de.smeo.tools.exceptionmonitor.exceptionparser.ExceptionOccuranceRecord;

/**
 * Storage for know exceptions and source for statistics etc
 * @author smeo
 */
public class ExceptionDatabase {
	private List<FileExceptionContainer> exceptionDataBase = new ArrayList<ExceptionDatabase.FileExceptionContainer>();
	private File storageFile;
	
	public ExceptionDatabase(String storageFile) {
		openOrCreateFile(storageFile);
		loadStorageFromFile(); 
	}

	private void loadStorageFromFile() {
		exceptionDataBase = (List<FileExceptionContainer>) XmlUtils.loadObjectFromXmlFile(storageFile);
		if (exceptionDataBase == null){
			exceptionDataBase = new ArrayList<ExceptionDatabase.FileExceptionContainer>();
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

	public void updateDatabase(
			File file,
			List<ExceptionOccuranceRecord> newExceptions) {
		
		for (ExceptionOccuranceRecord currExceptionRecord : newExceptions){
			FileExceptionContainer fileExceptionContainer = getOrCreateFileExceptionContainer(file);
			fileExceptionContainer.addExceptionRecord(currExceptionRecord);
		}
	}
	
	FileExceptionContainer getOrCreateFileExceptionContainer(File file){
		for (FileExceptionContainer currExceptionContainer : exceptionDataBase){
			if (currExceptionContainer.getAbsoluteFilePath().equals(file.getAbsolutePath())){
				return currExceptionContainer;
			}
		}
		FileExceptionContainer newFileExceptionContainer = new FileExceptionContainer(file.getAbsolutePath());
		exceptionDataBase.add(newFileExceptionContainer);
		return newFileExceptionContainer;
	}

	public CategorizedExceptions categorizeExceptions(
			File file,
			List<ExceptionOccuranceRecord> newExceptionRecords) {
		
		CategorizedExceptions categorizedExceptions = new CategorizedExceptions();
		
		for (ExceptionOccuranceRecord currExceptionRecord : newExceptionRecords){
			boolean isKnownException = false;
	
			Set<ExceptionChain> exceptionCausedByChainsForFile = getOrCreateFileExceptionContainer(file).getExceptionChains();
			isKnownException = exceptionCausedByChainsForFile.contains(currExceptionRecord.getExceptionChain());
			if (isKnownException){
				categorizedExceptions.addKnownException(currExceptionRecord);
			} else {
				categorizedExceptions.addYetUnkownException(currExceptionRecord);
			}
		}
		
		return categorizedExceptions;
	}

	public static class CategorizedExceptions {
		private Map<ExceptionChain, List<ExceptionOccuranceRecord>> knownExceptions = new HashMap<ExceptionChain, List<ExceptionOccuranceRecord>>();
		private Map<ExceptionChain, List<ExceptionOccuranceRecord>> yetUnknownExceptions = new HashMap<ExceptionChain, List<ExceptionOccuranceRecord>>();
		
		public boolean hasYetUnkownExceptions(){
			return (yetUnknownExceptions.size() > 0);
		} 
		public Map<ExceptionChain, List<ExceptionOccuranceRecord>> getKnownExceptions() {
			return knownExceptions;
		}

		public Map<ExceptionChain, List<ExceptionOccuranceRecord>> getYetUnknownExceptions() {
			return yetUnknownExceptions;
		}

		public void addKnownException(ExceptionOccuranceRecord exceptionRecord){
			 List<ExceptionOccuranceRecord> occurancesForExceptionChain = knownExceptions.get(exceptionRecord.getExceptionChain());
			 if (occurancesForExceptionChain == null){
				 occurancesForExceptionChain = new ArrayList<ExceptionOccuranceRecord>();
				 knownExceptions.put(exceptionRecord.getExceptionChain(), occurancesForExceptionChain);
			 }
			 occurancesForExceptionChain.add(exceptionRecord);
		}
		
		public void addYetUnkownException(ExceptionOccuranceRecord exceptionRecord){
			 List<ExceptionOccuranceRecord> occurancesForExceptionChain = yetUnknownExceptions.get(exceptionRecord.getExceptionChain());
			 if (occurancesForExceptionChain == null){
				 occurancesForExceptionChain = new ArrayList<ExceptionOccuranceRecord>();
				 knownExceptions.put(exceptionRecord.getExceptionChain(), occurancesForExceptionChain);
			 }
			 occurancesForExceptionChain.add(exceptionRecord);
		}
		
	}

	private static class FileExceptionContainer {
		private String absFilePath;
		private Set<ExceptionChain> exceptionChains = new HashSet<ExceptionChain>();
		private List<ExceptionOccuranceRecord> exceptions = new ArrayList<ExceptionOccuranceRecord>();
		
		public FileExceptionContainer(String absolutePath) {
			this.absFilePath = absolutePath;
		}

		public void addExceptionRecord(ExceptionOccuranceRecord exceptionRecord) {
			exceptions.add(exceptionRecord);
			exceptionChains.add(exceptionRecord.getExceptionChain());
		}

		public Set<ExceptionChain> getExceptionChains() {
			return exceptionChains;
		}

		public File getFile(){
			return new File(absFilePath);
		}

		public Object getAbsoluteFilePath() {
			return absFilePath;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((absFilePath == null) ? 0 : absFilePath.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			FileExceptionContainer other = (FileExceptionContainer) obj;
			if (absFilePath == null) {
				if (other.absFilePath != null)
					return false;
			} else if (!absFilePath.equals(other.absFilePath))
				return false;
			return true;
		}
	}
}
