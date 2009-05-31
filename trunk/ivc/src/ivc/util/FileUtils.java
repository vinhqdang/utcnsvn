/**
 * 
 */
package ivc.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author danielan
 * 
 */
public class FileUtils {

	public static void writeObjectToFile(String filePath, Serializable o) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(filePath);
		} catch (FileNotFoundException e) {
			java.io.File f = new java.io.File(filePath);
			try {
				f.createNewFile();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				fos = new FileOutputStream(filePath);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		ObjectOutputStream oos;
		try {

			oos = new ObjectOutputStream(fos);
			oos.writeObject(o);
			oos.close();
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Object readObjectFromFile(String filePath) {
		Object obj = new Object();
		FileInputStream fis;
		try {
			fis = new FileInputStream(filePath);
			try {
				if (fis.available() > 0) {
					try {
						ObjectInputStream ois = new ObjectInputStream(fis);

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

	public static StringBuffer InputStreamToStringBuffer(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuffer sb = new StringBuffer();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb;
	}

	public static void writeStringBufferToFile(String filePath, StringBuffer buf) {
		BufferedWriter bw;
		try {
			bw = new BufferedWriter(new FileWriter(filePath));
			bw.write(buf.toString());
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
