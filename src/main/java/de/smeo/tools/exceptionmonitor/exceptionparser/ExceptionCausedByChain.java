package de.smeo.tools.exceptionmonitor.exceptionparser;

import java.util.ArrayList;
import java.util.List;


/**
 * Exception chain is representing exceptions leasing to exceptions, marked by the 
 * "Caused by" in the logged stacktraces
 * @author smeo
 *
 */
public class ExceptionCausedByChain {
	public final static String REGEXP_CAUSED_BY = "Caused by:";

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
		for (int i=0; i < causedByChain.size(); i++){
			if (i != 0){
				stringBuffer.append(ExceptionCausedByChain.REGEXP_CAUSED_BY);
				stringBuffer.append(" ");
			}
			stringBuffer.append(causedByChain.get(i).toString());
		}
		return stringBuffer.toString();
	}

	public int size(){
		return causedByChain.size();
	}


}
