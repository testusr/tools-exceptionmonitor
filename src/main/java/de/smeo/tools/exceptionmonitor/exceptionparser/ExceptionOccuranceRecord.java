package de.smeo.tools.exceptionmonitor.exceptionparser;

/**
 * The unique occurance of a exception chain within a log file
 *
 */
public class ExceptionOccuranceRecord {
	private final ExceptionChain exceptionChain;

	private String filename;
	private long filePosition;
	private long time = -1;
	
	public ExceptionOccuranceRecord(String filename, long filePosition, ExceptionChain exceptionChain) {
		super();
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
	
}
