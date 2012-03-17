package de.smeo.tools.exceptionmonitor.exceptionparser;

import java.util.List;

/**
 * Immutable exception chain.
 * @author smeo
 *
 */
public class ExceptionChain {
	private final String id;
	private final List<Exception> causedByChain;
	
	public ExceptionChain(List<Exception> causedByChain) {
		this.causedByChain = causedByChain;
		this.id = createId(causedByChain);
	}
	
	public String getId() {
		return id;
	}

	public List<Exception> getExceptions() {
		return causedByChain;
	}

	public String getFirstExceptionName() {
		return causedByChain.get(0).getExceptionClassName();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		ExceptionChain other = (ExceptionChain) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
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
