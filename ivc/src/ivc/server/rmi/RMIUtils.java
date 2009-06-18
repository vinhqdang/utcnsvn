/**
 * 
 */
package ivc.server.rmi;

import ivc.client.rmi.ClientIntf;
import ivc.data.exception.IVCException;
import ivc.util.Constants;
import ivc.util.NetworkUtils;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;


/**
 * @author danielan
 * 
 */
public class RMIUtils {

	public static void exposeServerInterface(String hostAddress) {
		try {
			ServerImpl server = new ServerImpl();
			// create registry
			// Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
			// System.setSecurityManager(new IVCSecurityManager());
			LocateRegistry.createRegistry(1099);
			try {
				Naming.rebind("rmi://" + hostAddress + ":" + 1099 + "/" + Constants.BIND_SERVER, server);
			} catch (Exception e) {
				if (e instanceof AlreadyBoundException) {
					e.printStackTrace();
				} else {
					e.printStackTrace();
					throw new IVCException(e.getMessage());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void exposeClientInterface(String hostAddress, ClientIntf client) {
		// create registry
		try {
			Naming.rebind("rmi://" + NetworkUtils.getHostAddress() + ":" + 1099 + "/" + Constants.BIND_CLIENT + hostAddress, client);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	public static ClientIntf getClientIntf(String hostAddress) throws RemoteException {
		try {
			return (ClientIntf) Naming.lookup("rmi://" + NetworkUtils.getHostAddress() + ":" + 1099 + "/" + Constants.BIND_CLIENT + hostAddress);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void disconnectHost(String hostAddress) throws RemoteException {
		try {
			Naming.unbind("rmi://" + NetworkUtils.getHostAddress() + ":" + 1099 + "/" + Constants.BIND_CLIENT + hostAddress);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}

}
