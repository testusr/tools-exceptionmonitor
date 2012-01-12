package de.smeo.tools.exceptionmonitor.common;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.smeo.tools.exceptionmonitor.common.ExceptionDatabase.CategorizedExceptions;
import de.smeo.tools.exceptionmonitor.exceptionparser.EqualCauseExceptionChainContainer;
import de.smeo.tools.exceptionmonitor.exceptionparser.ExceptionCausedByChain;
import de.smeo.tools.exceptionmonitor.exceptionparser.ExceptionParser;

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
			List<ExceptionCausedByChain> newExceptions) {
		
		for (ExceptionCausedByChain currExceptionCausedByChain : newExceptions){
			boolean isKnownException = false;
	
			FileExceptionContainer fileExceptionContainer = getOrCreateFileExceptionContainer(file);
			List<ExceptionCausedByChain> exceptionCausedByChainsForFile = fileExceptionContainer.getExceptionCauseByChains();
			
			isKnownException = listContainsExceptionWithSameRootCause(currExceptionCausedByChain, exceptionCausedByChainsForFile);
			if (!isKnownException){
				fileExceptionContainer.addException(currExceptionCausedByChain.toString());
			}
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
			List<ExceptionCausedByChain> newExceptions) {
		
		CategorizedExceptions categorizedExceptions = new CategorizedExceptions();
		
		for (ExceptionCausedByChain currExceptionCausedByChain : newExceptions){
			boolean isKnownException = false;
	
			List<ExceptionCausedByChain> exceptionCausedByChainsForFile = getOrCreateFileExceptionContainer(file).getExceptionCauseByChains();
			isKnownException = listContainsExceptionWithSameRootCause(currExceptionCausedByChain, exceptionCausedByChainsForFile);
			if (isKnownException){
				categorizedExceptions.addKnownException(currExceptionCausedByChain);
			} else {
				categorizedExceptions.addYetUnkownException(currExceptionCausedByChain);
			}
		}
		
		return categorizedExceptions;
	}

	private boolean listContainsExceptionWithSameRootCause(ExceptionCausedByChain exception, List<ExceptionCausedByChain> exceptionList){
		for (ExceptionCausedByChain currExceptionCausedByChain : exceptionList){
			if (currExceptionCausedByChain.hasEqualRootCause(exception)){
				return true;
			}
		}
		return false;
	}
		
	public static class CategorizedExceptions {
		private List<EqualCauseExceptionChainContainer> knownExceptions = new ArrayList<EqualCauseExceptionChainContainer>();
		private List<EqualCauseExceptionChainContainer> yetUnknownExceptions = new ArrayList<EqualCauseExceptionChainContainer>();
		
		public boolean hasYetUnkownExceptions(){
			return (yetUnknownExceptions.size() > 0);
		}
		public List<EqualCauseExceptionChainContainer> getKnownExceptions() {
			return knownExceptions;
		}

		public List<EqualCauseExceptionChainContainer> getYetUnknownExceptions() {
			return yetUnknownExceptions;
		}

		public void addKnownException(ExceptionCausedByChain knownException){
			addExceptionToExceptionChainContainerList(knownException, knownExceptions);
		}
		
		public void addYetUnkownException(ExceptionCausedByChain unkownException){
			addExceptionToExceptionChainContainerList(unkownException, yetUnknownExceptions);
		}
		
		private void addExceptionToExceptionChainContainerList(ExceptionCausedByChain exceptionCausedByChain, List<EqualCauseExceptionChainContainer> exceptionContainers){
			boolean exceptionAdded = false;
			for (int i=0; !exceptionAdded && (i < exceptionContainers.size()); i++){
				EqualCauseExceptionChainContainer equalCauseExceptionChainContainer = exceptionContainers.get(i);
				exceptionAdded = equalCauseExceptionChainContainer.addExceptionIfHasEqualRootCause(exceptionCausedByChain);
			}
			
			if (!exceptionAdded){
				exceptionContainers.add(new EqualCauseExceptionChainContainer(exceptionCausedByChain));
			}
		}
	}

	private static class FileExceptionContainer {
		private String absFilePath;
		private List<ExceptionDatabaseEntry> exceptions = new ArrayList<ExceptionDatabase.ExceptionDatabaseEntry>();
		
		public FileExceptionContainer(String absolutePath) {
			this.absFilePath = absolutePath;
		}

		public void addException(String exceptionString) {
			exceptions.add(new ExceptionDatabaseEntry(exceptionString));
		}

		public List<ExceptionCausedByChain> getExceptionCauseByChains() {
			List<ExceptionCausedByChain> exceptionCausedByChains = new ArrayList<ExceptionCausedByChain>();
			for (ExceptionDatabaseEntry currDatabaseEntry : exceptions){
				exceptionCausedByChains.add(currDatabaseEntry.getExceptionCausedByChain());
			}
			return exceptionCausedByChains;
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
	private static class ExceptionDatabaseEntry {
		private transient ExceptionCausedByChain exceptionCausedByChain;
		private String exceptionString;
		 
		public ExceptionDatabaseEntry(String exceptionString) {
			this.exceptionString = exceptionString;
		}

		public ExceptionCausedByChain getExceptionCausedByChain() {
			 if (exceptionCausedByChain == null){
				 ExceptionParser exceptionParser = new ExceptionParser();
				 exceptionParser.parseAndFlush(exceptionString);
				 exceptionCausedByChain = exceptionParser.getExceptionChains().get(0);
			 }
			return exceptionCausedByChain;
		}
	}
}
