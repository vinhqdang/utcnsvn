package ivc;
import ivc.config.Configuration;
import ivc.config.HTMLLogger;
import ivc.data.exception.ConfigurationException;
import ivc.data.exception.Exceptions;
import ivc.data.exception.ServerException;
import ivc.rmi.ServerImpl;
import ivc.rmi.ServerIntf;

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
import java.util.Iterator;
import java.util.List;

public class ConnectionManager {
	
	private ConnectionManager manager;

	private static List<ServerIntf> peers;

	private ConnectionManager(){
		
	}
	
	public static void initiateConnections() throws ServerException {
		// read list of hosts with whom to communicate from config file
		List<String> peerHosts = Configuration.getInstance().getClientHosts();
		// if there is no host specified current instance is the initiator

		// register server for rmi connection in order to allow other users to
		// communicate with
		// after the first server exposes its interface the others can
		exposeInterface();

		// get a list of hosts to connect to
		// results in a list of ServerIntf objects
		Iterator<String> it = peerHosts.iterator();
		while (it.hasNext()) {
			String peerHost = it.next();
			ServerIntf peer = null;
			try {
				peer = connectToInterface(peerHost);
			} catch (ServerException e) {
				HTMLLogger.error("Unable to connect to peer host :" + peerHost);
				e.logError();
			}
			if (peer != null) {
				peers.add(peer);
			}
		}
		connectToInterface("test");

	}

	public static void exposeInterface() throws ServerException {
		try {
			ServerImpl server = new ServerImpl();
			InetAddress addr = null;
			Registry registry = LocateRegistry.getRegistry();
			try {
				addr = InetAddress.getLocalHost();
			} catch (UnknownHostException e1) {
				HTMLLogger.warn(Exceptions.UNABLE_TO_READ_HOST);
			}
			try {
				registry.bind("server_" + addr.getHostAddress(), server);
			} catch (AlreadyBoundException e) {
				HTMLLogger.warn(Exceptions.REGISTRY_ALREADY_BOUNDED, e);
			}
		} catch (RemoteException e) {
			HTMLLogger.error(e.getMessage());
			throw new ServerException(e.getMessage());
		}
	}

	public static ServerIntf connectToInterface(String hostAddress)
			throws ServerException {
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new RMISecurityManager());
		}
		try {
			ServerIntf server = (ServerIntf) Naming.lookup("rmi://"
					+ hostAddress + "/" + "server_" + hostAddress);
			return server;
		} catch (MalformedURLException e) {
			HTMLLogger.error(Exceptions.INVALID_URL + hostAddress);
			throw new ServerException(e.getMessage());
		} catch (RemoteException e) {
			HTMLLogger.error(Exceptions.UNABLE_TO_INITIATE_CONNECTION + " : "
					+ hostAddress);
			throw new ServerException(e.getMessage());
		} catch (NotBoundException e) {
			HTMLLogger.error(Exceptions.SERVER_UNBOUND + " : " + hostAddress);
			throw new ServerException(e.getMessage());
		}
	}
		
	public static void addHostToList(String host){
		try {
			List<String> peerHosts = Configuration.getInstance().getClientHosts();
			peerHosts.add(host);
			
		} catch (ConfigurationException e) {
			e.logError();
		}
	}
	
	public ConnectionManager getInstance(){
		if (manager == null){
			manager =  new ConnectionManager();
		}
		return manager;
	}
	
}

// Add Project : exposeIntf
// Watch Project : exposeIntf
//				   add host  you connected to hosts list in local config
//				   initiateConnections
//				   call peers intf to add the new host in their remote config