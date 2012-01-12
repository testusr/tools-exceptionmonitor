package de.smeo.tools.exceptionmonitor.common;

import java.io.File;
import java.util.List;

import com.thoughtworks.xstream.XStream;

public class XmlUtils {
	public static String objectToXml(Object object){
		XStream xstream = new XStream();
		return xstream.toXML(object);
	}
	
	public static Object xmlToObject(String xmlString){
		XStream xstream = new XStream();
		return xstream.fromXML(xmlString);
	}

	public static Object loadObjectFromXmlFile(File file){
		String xmlString = (String)FileUtils.readObjectFromFile(file);
		if (xmlString != null && !xmlString.isEmpty()){
			return xmlToObject(xmlString);
		}
		return null;
	}

	public static void writeObjectToXmlFile(
			Object object, File file) {
		FileUtils.writeObjectToFile(objectToXml(object), file);		
	}
}
