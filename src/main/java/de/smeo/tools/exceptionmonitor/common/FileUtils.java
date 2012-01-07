package de.smeo.tools.exceptionmonitor.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Properties;

public class FileUtils {
	public static void writeObjectToFile(Object object, File file) {
		try {
			FileOutputStream fout = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(object);
			oos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Object readObjectFromFile(File file) {
		try {
			FileInputStream fin = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fin);
			Object readObject = ois.readObject();
			ois.close();
			return readObject;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
    public static Properties loadPropertiesFromFile(File propsFile) throws IOException {
        Properties props = new Properties();
        FileInputStream fis = new FileInputStream(propsFile);
        props.load(fis);    
        fis.close();
        return props;
    }
}
