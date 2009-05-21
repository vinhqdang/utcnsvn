/**
 * 
 */
package ivc.rmi.server;

import ivc.data.BaseVersion;
import ivc.data.exception.ServerException;
import ivc.util.Constants;
import ivc.util.FileHandler;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/**
 * @author danielan
 * 
 */
public class ServerBusiness {
	
	public static final String PROJECTPATH = "D:\\Temp";

	public static String getHostAddress() {
		String hostAddress = "localhost";
		InetAddress addr = null;
		try {
			addr = InetAddress.getLocalHost();
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
		if (addr != null) {
			hostAddress = addr.getHostAddress();
		}
		return hostAddress;
	}

	public static void exposeServerInterface(String hostAddress) {
		try {
			ServerImpl server = new ServerImpl();
			try {
				Naming.rebind("rmi://" + hostAddress + ":" + 1099 + "/"	+ "server_ivc", server);
			} catch (Exception e) {

				if (e instanceof AlreadyBoundException) {
					e.printStackTrace();
				} else {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	private static void createFolderStructure(LinkedList<String> folders){
		Iterator<String> it  = folders.iterator();
		while (it.hasNext()){
			File f  = new File(it.next());
			f.mkdirs();
		}
	}
	
	private static void createFileStructure(Map<String,StringBuffer> files){
		Iterator<String> it = files.keySet().iterator();
		while(it.hasNext()){
			String filePath = it.next();
			File f = new File(filePath);
			try {
				f.createNewFile();
				FileHandler.writeStringBufferToFile(filePath, files.get(filePath));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
