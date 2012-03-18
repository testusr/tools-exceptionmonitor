package de.smeo.tools.exceptionmonitor.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import de.smeo.tools.exceptionmonitor.persistence.Identifiable;

/**
 * The unique occurance of a exception chain within a log file
 *
 */

@Entity
@Table(name = "EMON_EXPOCCURANCE")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class ExceptionOccuranceRecord extends Identifiable implements Serializable {
	private static final long serialVersionUID = 1549328823650420153L;

	private ExceptionChain exceptionChain;

	private String filename;
	private long filePosition;
	private long time = -1;
	
	public ExceptionOccuranceRecord(String filename, long filePosition, ExceptionChain exceptionChain) {
		this.filename = filename;
		this.filePosition = filePosition;
		this.exceptionChain = exceptionChain;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public long getStartPosition() {
		return filePosition;
	}

	public void setStartPosition(int startPosition) {
		this.filePosition = startPosition;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public ExceptionChain getExceptionChain() {
		return exceptionChain;
	}

	public long getFilePosition() {
		return filePosition;
	}

	public void setFilePosition(long filePosition) {
		this.filePosition = filePosition;
	}

	public void setExceptionChain(ExceptionChain exceptionChain) {
		this.exceptionChain = exceptionChain;
	}
	
	
	
}
