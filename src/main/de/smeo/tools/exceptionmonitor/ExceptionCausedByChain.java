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

	public int getExceptionCount() {
		return causedByChain.size();
	}
	
	public boolean hasEqualRootCause(ExceptionCausedByChain exceptionCausedByChain) {
		List<LoggedException> exceptionToCompare = exceptionCausedByChain.getExceptions();
		if (exceptionToCompare.size() == causedByChain.size()){
			for (int i=0; i < exceptionToCompare.size(); i++){
				if (!causedByChain.get(i).hasEqualRootCause(exceptionToCompare.get(i))){
					return false;
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		StringBuffer stringBuffer = new StringBuffer();
		for (LoggedException currException : causedByChain){
			stringBuffer.append(currException.toString());
		}
		return stringBuffer.toString();
	}




}
