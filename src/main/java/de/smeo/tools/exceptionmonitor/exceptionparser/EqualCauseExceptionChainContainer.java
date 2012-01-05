package de.smeo.tools.exceptionmonitor.exceptionparser;

import java.util.ArrayList;
import java.util.List;


/**
 * A Container for exceptions sharing the same root cause /
 * sharing the same stack trace characteristics. The sample exception chain is an example of a exception
 * of this type.
 * All other exception chains contain the same exception combination but could contain different individual error messages etc
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
		return getSampleExceptionChain().getFirstExceptionName();
	}
}
