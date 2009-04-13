import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import config.HTMLLogger;

import rmi.ServerImpl;
import data.exception.Exceptions;
import data.exception.ServerException;


public class Main {
	
	public static void main(String args[]) throws ServerException{
		
		// register server for rmi connection
		try {
			ServerImpl server = new ServerImpl();
			Registry registry =  LocateRegistry.getRegistry();
			try {
				registry.bind("server", server);
			} catch (AlreadyBoundException e) {
				HTMLLogger.warn(Exceptions.REGISTRY_ALREADY_BOUNDED, e);
			}
		} catch (RemoteException e) {
			HTMLLogger.error(e.getMessage());
			throw new ServerException(e.getMessage());
		}
		
		
	}
	
	
	
	
/* client side	
	private ServerIntf server;

	public void connect(){
		if (System.getSecurityManager() == null){
			System.setSecurityManager(new RMISecurityManager());
		}
		try {
			 server = (ServerIntf) Naming.lookup("rmi://localhost/Server");
		} catch (MalformedURLException e) {
			System.out.println("URL is invalid");
		} catch (RemoteException e) {
			System.out.println("Error initiating server connection");
		} catch (NotBoundException e) {
			System.out.println("Server is not bound");
		}
	}
*/


}
