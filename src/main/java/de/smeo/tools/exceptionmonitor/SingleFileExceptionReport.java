package de.smeo.tools.exceptionmonitor;

import java.util.ArrayList;
import java.util.List;

public class SingleFileExceptionReport {
	public List<EqualCauseExceptionChainContainer> reportedExceptionsGroupedByRootCause = new ArrayList<EqualCauseExceptionChainContainer>();
	public List<EqualCauseExceptionChainContainer> sightedExceptionsGroupedByRootCause;
	public List<EqualCauseExceptionChainContainer> unsightedExceptionsGroupedByRootCause;
	public List<EqualCauseExceptionChainContainer> unkownExceptionsGroupedByRootCause;
	
	public List<ReportedException> separateSightedUnsightedAndReturnUnkownExceptions(List<ReportedException> alreadyReportedExceptions){
		sightedExceptionsGroupedByRootCause = new ArrayList<EqualCauseExceptionChainContainer>();
		unsightedExceptionsGroupedByRootCause = new ArrayList<EqualCauseExceptionChainContainer>();
		unkownExceptionsGroupedByRootCause = new ArrayList<EqualCauseExceptionChainContainer>();
		
		List<ReportedException> unknownExceptions = new ArrayList<ReportedException>();
		for (EqualCauseExceptionChainContainer currExceptionContainer : reportedExceptionsGroupedByRootCause){
			ReportedException reportedException = getReportedExceptionForContainer(currExceptionContainer, alreadyReportedExceptions);
				if (reportedException != null){
					if (reportedException.isWasSighted()){
						sightedExceptionsGroupedByRootCause.add(currExceptionContainer);
					} else {
						unsightedExceptionsGroupedByRootCause.add(currExceptionContainer);
					}
				} else {
					unkownExceptionsGroupedByRootCause.add(currExceptionContainer);
					unknownExceptions.add(new ReportedException(currExceptionContainer.getSampleExceptionChain()));
				}
		}
		return unknownExceptions;
	}
	
	private static ReportedException getReportedExceptionForContainer(EqualCauseExceptionChainContainer currExceptionContainer,
			List<ReportedException> alreadyReportedExceptions) {
		for (ReportedException currReportedException : alreadyReportedExceptions){
			if (currExceptionContainer.hasEqualRootCause(currReportedException.getSampleExceptionChain())){
				return currReportedException;
			}
		}
		return null;
	}
	public List<EqualCauseExceptionChainContainer> getUnSightedExceptions() {
		return unsightedExceptionsGroupedByRootCause;
	}

	public List<EqualCauseExceptionChainContainer> getSightedExceptions() {
		return sightedExceptionsGroupedByRootCause;
	}
	
	public List<EqualCauseExceptionChainContainer> getUnkownExceptions() {
		return unkownExceptionsGroupedByRootCause;
	}

	public void addExceptions(List<EqualCauseExceptionChainContainer> newExceptionsGroupedByRootCause) {
		for (EqualCauseExceptionChainContainer currRootCauseContainer : newExceptionsGroupedByRootCause){
			EqualCauseExceptionChainContainer existingContainer = getExceptionContainerWithEqualRootCause(currRootCauseContainer.getSampleExceptionChain());
			if (existingContainer != null){
				for (ExceptionCausedByChain currExceptionChain : currRootCauseContainer.getExceptionChains()){
					assert(existingContainer.addExceptionIfHasEqualRootCause(currExceptionChain));
				}
			} else {
				reportedExceptionsGroupedByRootCause.add(currRootCauseContainer);
			}
		}
	}
	
	private EqualCauseExceptionChainContainer getExceptionContainerWithEqualRootCause(ExceptionCausedByChain exceptionCausedByChain){
		for (EqualCauseExceptionChainContainer currEqualRootCauseContainer : reportedExceptionsGroupedByRootCause){
			if (currEqualRootCauseContainer.hasEqualRootCause(exceptionCausedByChain)){
				return currEqualRootCauseContainer;
			}
		}
		return null;
	}

}
