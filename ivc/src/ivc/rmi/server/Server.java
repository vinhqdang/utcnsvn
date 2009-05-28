/**
 * 
 */
package ivc.rmi.server;

import ivc.data.exception.IVCException;
import ivc.rmi.client.ClientImpl;
import ivc.rmi.client.ClientIntf;
import ivc.util.Constants;
import ivc.util.NetworkUtils;

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
		exposeServerInterface(NetworkUtils.getHostAddress());
		System.out.println("Server started at "+ NetworkUtils.getHostAddress());
	}

	private static void exposeServerInterface(String hostAddress) {
		try {
			ServerImpl server = new ServerImpl();
			// create registry
			// Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
			// System.setSecurityManager(new IVCSecurityManager());
			LocateRegistry.createRegistry(1099);
			try {
				Naming.rebind("rmi://" + hostAddress + ":" + 1099 + "/"	+ Constants.BIND_SERVER, server);
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

	
	
}
