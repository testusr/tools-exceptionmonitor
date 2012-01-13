package de.smeo.tools.exceptionmonitor.exceptionparser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.smeo.tools.exceptionmonitor.common.DateUtils;


/**
 * Exception chain is representing exceptions leading to exceptions, marked by the 
 * "Caused by" in the logged stacktraces
 * This class combines the unique exceptions to a single chain
 * @author smeo
 *
 */
public class ExceptionCausedByChain implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public final static String REGEXP_CAUSED_BY = "Caused by:";

	private List<LoggedException> causedByChain = new ArrayList<LoggedException>();

	private ExceptionOcurrance occurance = new ExceptionOcurrance(
			DateUtils.getDaysTimeStamp(),
			-1);

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

	public int size(){
		return causedByChain.size();
	}

	public String getFirstExceptionName() {
		return getExceptions().get(0).getExceptionClassName();
	}


	public ExceptionOcurrance getOccurance() {
		return occurance;
	}
	
	public long getTimelyOccurance() {
		return occurance.timelyOccurance;
	}

	public void setTimelyOccurance(long timelyOccurance) {
		this.occurance.timelyOccurance = timelyOccurance;
	}

	public void setLineNumber(long lineNumber) {
		this.occurance.lineNumber = lineNumber;
	}
	
	public long getLineNumber() {
		return occurance.lineNumber;
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

	
	public static class ExceptionOcurrance implements Serializable {
		private static final long serialVersionUID = -7161220471772061732L;
		public long timelyOccurance;
		public long lineNumber;

		public ExceptionOcurrance(long timelyOccurance, long lineNumber) {
			this.timelyOccurance = timelyOccurance;
			this.lineNumber = lineNumber;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + (int) (lineNumber ^ (lineNumber >>> 32));
			result = prime * result
					+ (int) (timelyOccurance ^ (timelyOccurance >>> 32));
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ExceptionOcurrance other = (ExceptionOcurrance) obj;
			if (lineNumber != other.lineNumber)
				return false;
			if (timelyOccurance != other.timelyOccurance)
				return false;
			return true;
		}
		
		
	}

}
