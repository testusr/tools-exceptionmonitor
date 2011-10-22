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
		exceptionChains.add(sampleExceptionChain);
	}
	
	public boolean addExceptionIfHasEqualRootCause(ExceptionCausedByChain exceptionCausedByChain) {
		if (hasEqualRootCause(exceptionCausedByChain)){
			exceptionChains.add(exceptionCausedByChain);
			return true;
		}
		return false;
	}

	public boolean hasEqualRootCause(
			ExceptionCausedByChain exceptionCausedByChain) {
		return sampleExceptionChain.hasEqualRootCause(exceptionCausedByChain);
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
	
	public int size(){
		return exceptionChains.size();
	}

	public ExceptionCausedByChain getSampleExceptionChain() {
		return sampleExceptionChain;
	}

	public String getFirstExceptionName() {
		return getSampleExceptionChain().getExceptions().get(0).getExceptionClassName();
	}
}
