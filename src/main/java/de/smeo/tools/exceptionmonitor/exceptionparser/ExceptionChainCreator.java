package de.smeo.tools.exceptionmonitor.exceptionparser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Collect exceptions that are chained together by "cause by" and finally create an immutable
 * <code>ExceptionChain</code> object from it.
 * @author smeo
 *
 */
public class ExceptionChainCreator {
	
	public final static String REGEXP_CAUSED_BY = "Caused by:";

	private List<Exception> causedByChain = new ArrayList<Exception>();

	public ExceptionChainCreator(Exception firstException) {
		super();
		causedByChain.add(firstException);
	}

	public void addCausedBy(Exception loggedException){
		causedByChain.add(loggedException);
	}

	public List<Exception> getExceptions() {
		return causedByChain;
	}

	public int getExceptionCount() {
		return causedByChain.size();
	}
	
	public ExceptionChain createExceptionChain(){
		return new ExceptionChain(causedByChain);
	}
	
	public boolean hasEqualRootCause(ExceptionChainCreator exceptionCausedByChain) {
		List<Exception> exceptionToCompare = exceptionCausedByChain.getExceptions();
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

	@Override
	public String toString() {
		StringBuffer stringBuffer = new StringBuffer();
		for (int i=0; i < causedByChain.size(); i++){
			if (i != 0){
				stringBuffer.append(ExceptionChainCreator.REGEXP_CAUSED_BY);
				stringBuffer.append(" ");
			}
			stringBuffer.append(causedByChain.get(i).toString());
		}
		return stringBuffer.toString();
	}
}
