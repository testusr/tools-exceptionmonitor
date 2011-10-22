package de.smeo.tools.exceptionmonitor;
 
import java.util.ArrayList;
import java.util.List;

import de.smeo.tools.exceptionmonitor.exceptionparser.EqualCauseExceptionChainContainer;
import de.smeo.tools.exceptionmonitor.exceptionparser.ExceptionCausedByChain;

public class SingleFileExceptionReport {
	private final MonitoredFile monitoredFile;
	private List<EqualCauseExceptionChainContainer> reportedExceptionsGroupedByRootCause = new ArrayList<EqualCauseExceptionChainContainer>();
	private List<ReportedExceptionOccurances> sightedExceptionsGroupedByRootCause;
	private List<ReportedExceptionOccurances> unsightedExceptionsGroupedByRootCause;
	private List<ReportedExceptionOccurances> unkownExceptionsGroupedByRootCause;
	
	public SingleFileExceptionReport(MonitoredFile monitoredFile) {
		this.monitoredFile = monitoredFile;
	}

	public List<ReportedException> separateSightedUnsightedAndReturnUnkownExceptions(List<ReportedException> alreadyReportedExceptions){
		sightedExceptionsGroupedByRootCause = new ArrayList<ReportedExceptionOccurances>();
		unsightedExceptionsGroupedByRootCause = new ArrayList<ReportedExceptionOccurances>();
		unkownExceptionsGroupedByRootCause = new ArrayList<ReportedExceptionOccurances>();
		
		List<ReportedException> unknownExceptions = new ArrayList<ReportedException>();
		for (EqualCauseExceptionChainContainer currExceptionContainer : reportedExceptionsGroupedByRootCause){
			ReportedException reportedException = getReportedExceptionForContainer(currExceptionContainer, alreadyReportedExceptions);
				if (reportedException != null){
					if (reportedException.isWasSighted()){
						sightedExceptionsGroupedByRootCause.add(new ReportedExceptionOccurances(reportedException, currExceptionContainer));
					} else {
						unsightedExceptionsGroupedByRootCause.add(new ReportedExceptionOccurances(reportedException, currExceptionContainer));
					}
				} else {
					ReportedException newReportedException = new ReportedException(currExceptionContainer.getSampleExceptionChain());
					unkownExceptionsGroupedByRootCause.add(new ReportedExceptionOccurances(newReportedException, currExceptionContainer));
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
	public List<ReportedExceptionOccurances> getUnSightedExceptions() {
		return unsightedExceptionsGroupedByRootCause;
	}

	public List<ReportedExceptionOccurances> getSightedExceptions() {
		return sightedExceptionsGroupedByRootCause;
	}
	
	public List<ReportedExceptionOccurances> getUnkownExceptions() {
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
	
	public static class ReportedExceptionOccurances {
		private final ReportedException reportedException;
		private final EqualCauseExceptionChainContainer occurances;
	
		public ReportedExceptionOccurances(ReportedException reportedException,
				EqualCauseExceptionChainContainer occurances) {
			super();
			this.reportedException = reportedException;
			this.occurances = occurances;
		}

		public int occuranceCount() {
			return occurances.size();
		}
		
		
	}

}
