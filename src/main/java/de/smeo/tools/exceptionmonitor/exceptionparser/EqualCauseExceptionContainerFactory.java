package de.smeo.tools.exceptionmonitor.exceptionparser;

import java.util.ArrayList;
import java.util.List;


public class EqualCauseExceptionContainerFactory {
	
	public static List<EqualCauseExceptionChainContainer> createEqualCauseContainers(
			List<ExceptionCausedByChain> exceptionChains) {
		List<EqualCauseExceptionChainContainer> equalCauseExceptionContainers = new ArrayList<EqualCauseExceptionChainContainer>();
		
		for (ExceptionCausedByChain currExceptionChain : exceptionChains){
			findOrCreateEqualCauseContainerAndAddChain(currExceptionChain, equalCauseExceptionContainers);
		}
		return equalCauseExceptionContainers;
	}

	private static void findOrCreateEqualCauseContainerAndAddChain(
			ExceptionCausedByChain exceptionCausedByChain,
			List<EqualCauseExceptionChainContainer> equalCauseExceptionContainers) {
		for (EqualCauseExceptionChainContainer currEqualCauseExceptionContainer : equalCauseExceptionContainers){
			if (currEqualCauseExceptionContainer.addExceptionIfHasEqualRootCause(exceptionCausedByChain)){
				return;
			}
		}
		equalCauseExceptionContainers.add(new EqualCauseExceptionChainContainer(exceptionCausedByChain));
	}
	
	

}
