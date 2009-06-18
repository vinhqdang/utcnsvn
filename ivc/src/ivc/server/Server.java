/**
 * 
 */
package ivc.server;

import ivc.server.rmi.RMIUtils;
import ivc.util.NetworkUtils;

import java.rmi.RemoteException;



/**
 * @author danielan
 * 
 */
public class Server {

	/**
	 * Main method that starts the server application
	 * @param args
	 * @throws RemoteException
	 */
	public static void main(String[] args) throws RemoteException {
		RMIUtils.exposeServerInterface(NetworkUtils.getHostAddress());
		System.out.println("Server started at "+ NetworkUtils.getHostAddress());
	}

	

	
	
}
