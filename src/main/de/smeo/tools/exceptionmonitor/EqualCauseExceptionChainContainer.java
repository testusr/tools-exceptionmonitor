package de.smeo.tools.exceptionmonitor;

import java.util.ArrayList;
import java.util.List;

/**
 * A Container for exceptions sharing the same root cause /
 * sharing the same stack trace characteristics
 *
 */
public class EqualCauseExceptionChainContainer {
	private ExceptionCausedByChain sampleExceptionChain;
	private List<ExceptionCausedByChain> exceptionChains = new ArrayList<ExceptionCausedByChain>();
	
	public EqualCauseExceptionChainContainer(ExceptionCausedByChain sampleExceptionChain) {
		this.sampleExceptionChain = sampleExceptionChain;
	}
	
	public boolean addExceptionIfHasEqualRootCause(ExceptionCausedByChain exceptionCausedByChain) {
		if (sampleExceptionChain.hasEqualRootCause(exceptionCausedByChain)){
			exceptionChains.add(exceptionCausedByChain);
			return true;
		}
		return false;
	}
	
	public List<ExceptionCausedByChain> getExceptionChains() {
		return exceptionChains;
	}

	public List<String> getSourcePath() {
		List<String> sourcePath = new ArrayList<String>();
		
		for (LoggedException currException : sampleExceptionChain.getExceptions()){
			sourcePath.addAll(currException.getSourcePath());
		}
		return sourcePath;
	}

}
