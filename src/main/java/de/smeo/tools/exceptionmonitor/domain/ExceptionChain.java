package de.smeo.tools.exceptionmonitor.domain;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

import de.smeo.tools.exceptionmonitor.exceptionparser.ExceptionChainCreator;
import de.smeo.tools.exceptionmonitor.persistence.Identifiable;

/**
 * Immutable exception chain.
 * @author smeo
 *
 */

@Entity
@Table(name = "EMON_EXCEPTIONCHAIN")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class ExceptionChain extends Identifiable implements Serializable  {
	private static final long serialVersionUID = 7729660909453755254L;
	
	@ManyToMany
	@Cascade({ org.hibernate.annotations.CascadeType.ALL})
	private List<Exception> causedByChain;
	
	public ExceptionChain(List<Exception> causedByChain) {
		super(createId(causedByChain));
		this.causedByChain = causedByChain;
	}
	
	private ExceptionChain(){}

	public List<Exception> getExceptions() {
		return Collections.unmodifiableList(causedByChain);
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

	public List<Exception> getCausedByChain() {
		return causedByChain;
	}

	public void setCausedByChain(List<Exception> causedByChain) {
		this.causedByChain = causedByChain;
	}


}
