package de.smeo.tools.exceptionmonitor.persistence;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.smeo.tools.exceptionmonitor.exceptionparser.ExceptionChain;
import de.smeo.tools.exceptionmonitor.exceptionparser.ExceptionOccuranceRecord;

public abstract class ExceptionDatabase {
	protected List<FileExceptionContainer> exceptionDataBase = new ArrayList<FileExceptionContainer>();
	
	abstract void saveStorageToFile();
	
	abstract void updateDatabase(List<ExceptionOccuranceRecord> newExceptions) ;
	
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
}
