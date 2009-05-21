/**
 * 
 */
package ivc.rmi.server;

import ivc.data.exception.ServerException;
import ivc.rmi.client.ClientImpl;
import ivc.rmi.client.ClientIntf;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

/**
 * @author danielan
 * 
 */
public class Server {

	/**
	 * @param args
	 * @throws RemoteException
	 */
	public static void main(String[] args) throws RemoteException {
		Path path = new Path("d:\\temp\\ivc\\test\\test.txt");
		
		File f = new File ("d:\\temp\\ivc\\test\\test.txt");
		try {
			f.mkdirs();
			f.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		LocateRegistry.createRegistry(1099);
//		exposeServerInterface(ServerBusiness.getHostAddress());
//		System.out.println("Server OK...");
//		//exposeClientInterface(hostAddress);
//		System.out.println("Client OK...");
//		connectToInterface(ServerBusiness.getHostAddress()); 
//		
//		try {
//			ClientIntf client = (ClientIntf) Naming.lookup("rmi://"
//					+ ServerBusiness.getHostAddress() + ":" + 1099 + "/" + "client_ivc");
//		
//			//client.test("SERVER");
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (NotBoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}

	private static void exposeServerInterface(String hostAddress) {
		
		try {
			ServerImpl server = new ServerImpl();
			// create registry
			// Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
			// System.setSecurityManager(new IVCSecurityManager());
			try {
				Naming.rebind("rmi://" + hostAddress + ":" + 1099 + "/"
						+ "server_ivc", server);
			} catch (Exception e) {

				if (e instanceof AlreadyBoundException) {
					e.printStackTrace();
				} else {
					String msg = e.getMessage();
					e.printStackTrace();
					throw new ServerException(e.getMessage());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void exposeClientInterface(String hostAddress) {		
		try {
			ClientImpl client = new ClientImpl();
			// create registry
			try {
				Naming.rebind("rmi://" + hostAddress + ":" + 1099 + "/"
						+ "client_ivc", client);
			} catch (Exception e) {
				if (e instanceof AlreadyBoundException) {
					e.printStackTrace();
				} else {
					String msg = e.getMessage();
					e.printStackTrace();
					throw new ServerException(e.getMessage());
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
