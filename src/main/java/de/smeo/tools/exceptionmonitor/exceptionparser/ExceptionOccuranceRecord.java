package de.smeo.tools.exceptionmonitor.exceptionparser;

import de.smeo.tools.exceptionmonitor.persistence.Identifiable;

/**
 * The unique occurance of a exception chain within a log file
 *
 */
public class ExceptionOccuranceRecord extends Identifiable {
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
