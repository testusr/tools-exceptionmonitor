package de.smeo.tools.exceptionmonitor.exceptionparser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.smeo.tools.exceptionmonitor.domain.Exception;
import de.smeo.tools.exceptionmonitor.domain.ExceptionChain;


/**
 * Collect exceptions that are chained together by "cause by" and finally create an immutable
 * <code>ExceptionChain</code> object from it.
 * @author smeo
 *
 */
public class ExceptionChainCreator {
	
	public final static String REGEXP_CAUSED_BY = "Caused by:";

	private List<Exception> causedByChain = new ArrayList<Exception>();

	public void addCausedBy(Exception loggedException){
		causedByChain.add(loggedException);
	}

	public int getExceptionCount() {
		return causedByChain.size();
	}
	
	public ExceptionChain createExceptionChain(){
		return new ExceptionChain(causedByChain);
	}
	

	public int size(){
		return causedByChain.size();
	}

	public String getFirstExceptionName() {
		return causedByChain.get(0).getExceptionClassName();
	}

	@Override
	public String toString() {
		return createExceptionChain().toString();
	}
}
