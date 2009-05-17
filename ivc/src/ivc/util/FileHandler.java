/**
 * 
 */
package ivc.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author danielan
 *
 */
public class FileHandler {
	
	public static void writeObjectToFile(String filePath, Serializable o){
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(filePath);
		} catch (FileNotFoundException e) {
			java.io.File f=  new java.io.File(filePath);
			try {
				f.createNewFile();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				fos =  new FileOutputStream(filePath);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
			ObjectOutputStream oos;
			try {
				fos = new FileOutputStream(filePath);				
				oos = new ObjectOutputStream(fos);
				oos.writeObject(o);
				oos.close();
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	public static Object readObjectFromFile(String filePath){
		Object obj = new Object();
		FileInputStream fis;
		try {
			fis = new FileInputStream(filePath);
			try {
				ObjectInputStream ois =  new ObjectInputStream(fis);
				try {
					return ois.readObject();					
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
	}

}
