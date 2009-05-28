/**
 * 
 */
package ivc.rmi.server;

import ivc.data.BaseVersion;
import ivc.data.exception.IVCException;
import ivc.rmi.client.ClientImpl;
import ivc.util.Constants;
import ivc.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/**
 * @author danielan
 * 
 */
public class ServerBusiness {
	

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
				FileUtils.writeStringBufferToFile(filePath, files.get(filePath));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private static void exposeClientInterface(String hostAddress) {		
		try {
			ClientImpl client = new ClientImpl();
			// create registry
			try {
				Naming.rebind("rmi://" + hostAddress + ":" + 1099 + "/"	+ "client_ivc", client);
			} catch (Exception e) {
				if (e instanceof AlreadyBoundException) {
					e.printStackTrace();
				} else {
					String msg = e.getMessage();
					e.printStackTrace();
					throw new IVCException(e.getMessage());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void connectToInterface(String hostAddress) {

		try {
			ServerIntf server = (ServerIntf) Naming.lookup("rmi://"
					+ hostAddress + ":" + 1099 + "/" + "server_ivc");
			// if connection succedded : add intf to list of peers and write to
			// config file
			if (server != null) {
				System.out.println("Connection to Server...OK");
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}
}
