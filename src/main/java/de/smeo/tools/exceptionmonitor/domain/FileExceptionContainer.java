package de.smeo.tools.exceptionmonitor.domain;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;

import de.smeo.tools.exceptionmonitor.persistence.Identifiable;

@Entity
@Table(name = "EMON_EXPCONTAINER")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class FileExceptionContainer extends Identifiable implements Serializable {
	private static final long serialVersionUID = -8761095692865276022L;
	
	private String absFilePath;
	
	@Transient
	private Set<ExceptionChain> exceptionChains = new HashSet<ExceptionChain>();
	
	@OneToMany
	@Cascade({ org.hibernate.annotations.CascadeType.ALL})
	private List<ExceptionOccuranceRecord> exceptions = new ArrayList<ExceptionOccuranceRecord>();
	
	public FileExceptionContainer(String absolutePath) {
		super(absolutePath.hashCode());
		this.absFilePath = absolutePath;
	}
	
	private FileExceptionContainer() {}

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
