package de.smeo.tools.exceptionmonitor.persistence;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.smeo.tools.exceptionmonitor.exceptionparser.ExceptionChain;
import de.smeo.tools.exceptionmonitor.exceptionparser.ExceptionOccuranceRecord;

public class FileExceptionContainer extends Identifiable {
	private String absFilePath;
	private Set<ExceptionChain> exceptionChains = new HashSet<ExceptionChain>();
	private List<ExceptionOccuranceRecord> exceptions = new ArrayList<ExceptionOccuranceRecord>();
	
	public FileExceptionContainer(String absolutePath) {
		super(absolutePath.hashCode());
		this.absFilePath = absolutePath;
	}
	
	public FileExceptionContainer() {}

	public void addExceptionRecord(ExceptionOccuranceRecord exceptionRecord) {
		exceptions.add(exceptionRecord);
		exceptionChains.add(exceptionRecord.getExceptionChain());
	}

	public Set<ExceptionChain> getExceptionChains() {
		return exceptionChains;
	}

	public File getFile(){
		return new File(absFilePath);
	}

	public Object getAbsoluteFilePath() {
		return absFilePath;
	}

	public String getAbsFilePath() {
		return absFilePath;
	}

	public void setAbsFilePath(String absFilePath) {
		this.absFilePath = absFilePath;
	}

	public List<ExceptionOccuranceRecord> getExceptions() {
		return exceptions;
	}

	public void setExceptions(List<ExceptionOccuranceRecord> exceptions) {
		this.exceptions = exceptions;
	}

	public void setExceptionChains(Set<ExceptionChain> exceptionChains) {
		this.exceptionChains = exceptionChains;
	}
	
	
}
