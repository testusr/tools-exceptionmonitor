package de.smeo.tools.exceptionmonitor.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.Proxy;

import de.smeo.tools.exceptionmonitor.persistence.Identifiable;

/**
 * Collection of lines containing the stack trace of the exception. Capable of 
 * extracting only the parts where source files and line position are specified. This information
 * is later used to compare stack traces and to find out if the exceptions have the same root cause.
 * @author smeo
 *
 */

@Entity
@Table(name = "EMON_EXPSTRACE")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class ExceptionStackTrace extends Identifiable implements Serializable {
	private static final long serialVersionUID = 1417805941439871079L;

	@CollectionOfElements
	@Cascade({ org.hibernate.annotations.CascadeType.ALL})
	private List<String> lines;

	public ExceptionStackTrace(List<String> stackTraceLines) {
		super(createId(stackTraceLines));
		this.lines = stackTraceLines;
	}
	
	private ExceptionStackTrace() {}
	
	

	public void addLine(String stackTraceMemberLine) {
		lines.add(stackTraceMemberLine);
	}

	public boolean hasEqualCharacteristics(ExceptionStackTrace stackTrace) {
		return true;
	}

	public List<String> getSourcePath() {
		return getSourceEntries(lines);
	}


	List<String> getLines() {
		return Collections.unmodifiableList(lines);
	}

	@Override
	public String toString() {
		return lines.toString();
	}

	private static List<String> getSourceEntries(List<String> lines) {
		List<String> sourcePath = new ArrayList<String>();
		for (String currStackTraceLine : lines){
			sourcePath.add(exctractSourceEntry(currStackTraceLine));
		}
		return sourcePath;
	}

	private static String exctractSourceEntry(String stackTraceLine) {
		if (stackTraceLine.contains("(") && stackTraceLine.contains(")")){
			return stackTraceLine.substring((stackTraceLine.indexOf("(")+1), stackTraceLine.indexOf(")"));
		}
		return "";
	}

	private static String createId(List<String> elements) {
		StringBuffer stringBuffer = new StringBuffer();
		for (String currElement : getSourceEntries(elements)){
			stringBuffer.append(currElement);
		}
		return ""+stringBuffer.toString().hashCode();
	}

	public void setLines(List<String> lines) {
		this.lines = lines;
	}

	
	

}
