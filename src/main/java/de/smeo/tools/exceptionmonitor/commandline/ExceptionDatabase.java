package de.smeo.tools.exceptionmonitor.commandline;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.smeo.tools.exceptionmonitor.exceptionparser.EqualCauseExceptionChainContainer;
import de.smeo.tools.exceptionmonitor.exceptionparser.ExceptionCausedByChain;

/**
 * Storage for know exceptions and source for statistics etc
 * @author smeo
 */
public class ExceptionDatabase {
	private Map<String, List<ExceptionCausedByChain>> absFilenameToknownExceptionSamples = new HashMap<String, List<ExceptionCausedByChain>>();

	
	public ExceptionDatabase(String filename) {
		
	}

	public CategorizedExceptions updateDatabaseAndCategorizeExceptions(
			File file,
			List<ExceptionCausedByChain> newExceptions) {
		
		CategorizedExceptions categorizedExceptions = new CategorizedExceptions();
		
		for (ExceptionCausedByChain currExceptionCausedByChain : newExceptions){
			boolean isKnownException = false;
	
			List<ExceptionCausedByChain> exceptionCausedByChainsForFile = absFilenameToknownExceptionSamples.get(file.getAbsolutePath());
			if (exceptionCausedByChainsForFile == null){
				exceptionCausedByChainsForFile = new ArrayList<ExceptionCausedByChain>();
				absFilenameToknownExceptionSamples.put(file.getAbsolutePath(), exceptionCausedByChainsForFile);
			}
			
			isKnownException = listContainsExceptionWithSameRootCause(currExceptionCausedByChain, exceptionCausedByChainsForFile);
			if (isKnownException){
				categorizedExceptions.addKnownException(currExceptionCausedByChain);
			} else {
				categorizedExceptions.addYetUnkownException(currExceptionCausedByChain);
				exceptionCausedByChainsForFile.add(currExceptionCausedByChain);
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

	
}
