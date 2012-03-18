package de.smeo.tools.exceptionmonitor.exceptionparser;

import java.util.Collections;
import java.util.List;

import de.smeo.tools.exceptionmonitor.persistence.Identifiable;

/**
 * Immutable exception chain.
 * @author smeo
 *
 */
public class ExceptionChain extends Identifiable {
	private final List<Exception> causedByChain;
	
	public ExceptionChain(List<Exception> causedByChain) {
		super(createId(causedByChain));
		this.causedByChain = Collections.unmodifiableList(causedByChain);
	}

	public List<Exception> getExceptions() {
		return causedByChain;
	}

	public String getFirstExceptionName() {
		return causedByChain.get(0).getExceptionClassName();
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
	
	/**
	 * Generates an id based on the exception chains source path. The same 
	 * source path will always result in the same id.
	 *  
	 * @param causedByChain
	 * @return
	 */
	private static String createId(List<Exception> causedByChain) {
		StringBuffer idString = new StringBuffer();
		for (Exception currException : causedByChain){
			idString.append(currException.getExceptionClassName());
			for (String currSourcePathEntry : currException.getSourcePath()){
				idString.append(currSourcePathEntry);
			}
		}
		return ""+idString.toString().hashCode();
	}



}
