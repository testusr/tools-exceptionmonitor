package de.smeo.tools.exceptionmonitor;

import java.util.ArrayList;
import java.util.List;

/**
 * Exception chain is representing exceptions leasing to exceptions, marked by the 
 * "Caused by" in the logged stacktraces
 * @author smeo
 *
 */
public class ExceptionCausedByChain {
	private List<LoggedException> causedByChain = new ArrayList<LoggedException>();
	
	public ExceptionCausedByChain(LoggedException exception) {
		super();
		causedByChain.add(exception);
	}

	public void addCausedBy(LoggedException loggedException){
		causedByChain.add(loggedException);
	}

	public List<LoggedException> getExceptions() {
		return causedByChain;
	}

	@Override
	public String toString() {
		return causedByChain.toString();
	}
}
