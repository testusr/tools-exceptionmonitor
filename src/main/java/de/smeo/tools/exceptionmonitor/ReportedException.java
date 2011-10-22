package de.smeo.tools.exceptionmonitor;

import java.util.List;

import de.smeo.tools.exceptionmonitor.exceptionparser.ExceptionCausedByChain;

public class ReportedException {
	private boolean wasSighted = false;
	private boolean wasFixed = false;
	private boolean canBeIgnored = false;
	private boolean doInformDefaultEmailAdress = true;
	private String comment;
	private List<EmailAdress> emailAdressesToInform;
	private final ExceptionCausedByChain sampleExceptionCausedByChain;
	
	public ReportedException(ExceptionCausedByChain sampleExceptionCausedByChain) {
		super();
		this.sampleExceptionCausedByChain = sampleExceptionCausedByChain;
	}
	
	public boolean isWasSighted() {
		return wasSighted;
	}
	public void setWasSighted(boolean wasSighted) {
		this.wasSighted = wasSighted;
	}
	public boolean isWasFixed() {
		return wasFixed;
	}
	public void setWasFixed(boolean wasFixed) {
		this.wasFixed = wasFixed;
	}
	public boolean isCanBeIgnored() {
		return canBeIgnored;
	}
	public void setCanBeIgnored(boolean canBeIgnored) {
		this.canBeIgnored = canBeIgnored;
	}
	public boolean isDoInformDefaultEmailAdress() {
		return doInformDefaultEmailAdress;
	}
	public void setDoInformDefaultEmailAdress(boolean doInformDefaultEmailAdress) {
		this.doInformDefaultEmailAdress = doInformDefaultEmailAdress;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}

	public ExceptionCausedByChain getSampleExceptionChain() {
		return sampleExceptionCausedByChain;
	}
	
	
}
