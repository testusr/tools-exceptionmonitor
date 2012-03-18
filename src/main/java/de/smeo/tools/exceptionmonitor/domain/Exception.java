package de.smeo.tools.exceptionmonitor.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import de.smeo.tools.exceptionmonitor.exceptionparser.ExceptionCreator;
import de.smeo.tools.exceptionmonitor.persistence.Identifiable;

@Entity
@Table(name = "EMON_EXCEPTION")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Exception extends Identifiable implements Serializable  {
	private static final long serialVersionUID = 7881699626118705339L;
	private ExceptionStackTrace stackTrace; 
	private String exceptionClassName;
	private String exceptionComment;
	
	public Exception(String exceptionClassName, String  exceptionComment, List<String> stackTraceLines){
		super(createId(exceptionClassName, exceptionComment, stackTraceLines));
		this.exceptionClassName = exceptionClassName;
		this.exceptionComment = exceptionComment;
		this.stackTrace = new ExceptionStackTrace(stackTraceLines);
	}

	private Exception(){};
	
	public boolean hasEqualRootCause(ExceptionCreator loggedException) {
		if (loggedException.getExceptionClassName().equals(exceptionClassName)){
			return stackTrace.hasEqualCharacteristics(stackTrace);
		}
		return false;
	}
	
	public List<String> getSourcePath() {
		return stackTrace.getSourcePath();
	}

	public String getExceptionClassName() {
		return exceptionClassName;
	}
	

	public ExceptionStackTrace getStackTrace() {
		return stackTrace;
	}

	public void setStackTrace(ExceptionStackTrace stackTrace) {
		this.stackTrace = stackTrace;
	}

	public String getExceptionComment() {
		return exceptionComment;
	}

	public void setExceptionComment(String exceptionComment) {
		this.exceptionComment = exceptionComment;
	}

	public void setExceptionClassName(String exceptionClassName) {
		this.exceptionClassName = exceptionClassName;
	}

	@Override
	public String toString() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(exceptionClassName);
		if (exceptionComment != null){
		   stringBuffer.append(":");
		   stringBuffer.append(exceptionComment);
		}
  	    
		stringBuffer.append("\n");

  	    for (String currStackLine : stackTrace.getLines()){
			stringBuffer.append("\t");
			stringBuffer.append(currStackLine);
			stringBuffer.append("\n");
		}
		return stringBuffer.toString();
	}

	
	private static String createId(String exceptionClassName,
			String exceptionComment, List<String> stackTraceLines) {
		List<String> sourcePath = new ExceptionStackTrace(stackTraceLines).getSourcePath();
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(exceptionClassName);
		stringBuffer.append(exceptionComment);
		for (String currSourceEntry : sourcePath){
			stringBuffer.append(currSourceEntry);
		}
		return ""+stringBuffer.toString().hashCode();
	}
	
	
}
