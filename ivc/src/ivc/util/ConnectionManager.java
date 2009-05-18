package ivc.util;

import ivc.data.Peers;
import ivc.data.exception.ConfigurationException;
import ivc.data.exception.Exceptions;
import ivc.data.exception.ServerException;
import ivc.rmi.ServerImpl;
import ivc.rmi.ServerIntf;

import java.io.Serializable;
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
import java.security.AllPermission;
import java.security.Permission;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ConnectionManager implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static ConnectionManager manager;

	private Map<String, ServerIntf> peers;
	
	private static Peers peersHosts;

	private ConnectionManager() {
	}

	/**
	 * Called at startup
	 * 
	 * @throws ServerException
	 */
	public void initiateConnections() throws ServerException {
		// read list of hosts with whom to communicate from config file
		// if there is no host specified current instance is the initiator

		// register server for rmi connection in order to allow other users to
		// communicate with
		// after the first server exposes its interface the others can connect
		// to it
		exposeInterface();

		// get a list of hosts to connect to
		// results in a list of ServerIntf objects
		Iterator<String> it = peersHosts.getPeers().iterator();
		while (it.hasNext()) {
			String peerHost = it.next();
			ServerIntf peer = null;
			try {
				peer = connectToInterface(peerHost);
				if (peer != null) {
					peers.put(peerHost, peer);
					peersHosts.addPeerHost(peerHost);
				}
			} catch (ServerException e) {
			//	HTMLLogger.error("Unable to connect to peer host :" + peerHost);
				e.logError();
			}
			if (peer != null) {
				peers.put(peerHost, peer);
			}
		}

	}

	public void exposeInterface() throws ServerException {
		try {
			ServerImpl server = new ServerImpl();			
			  // create registry
	        LocateRegistry.createRegistry(1099);
//	        
//	        Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
	        System.setSecurityManager(new IVCSecurityManager());
			try {				
				
				 Naming.rebind("rmi://" +  getHostAddress() + ":"+ 1099 + "/" + "server_ivc", server);
			} catch (Exception e) {
				if (e instanceof AlreadyBoundException) {
//					HTMLLogger.warn(Exceptions.REGISTRY_ALREADY_BOUNDED, e);
				} else {
					String msg = e.getMessage();
					throw new ServerException(e.getMessage());
				}
			}
		} catch (Exception e) {
//			HTMLLogger.getLogger();
//			HTMLLogger.error(e.getMessage());
			throw new ServerException(e.getMessage());
		}
	}

	public ServerIntf connectToInterface(String hostAddress)
			throws ServerException {
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new RMISecurityManager());
		}
		try {
			ServerIntf server = (ServerIntf) Naming.lookup("rmi://"
					+ hostAddress + ":"+ 1099 + "/" + "server_" + hostAddress);
			// if connection succedded : add intf to list of peers and write to
			// config file
			if (server != null) {
				peers.put(hostAddress, server);
				peersHosts.addPeerHost(hostAddress);
			}
			return server;
		} catch (MalformedURLException e) {
//			HTMLLogger.error(Exceptions.INVALID_URL + hostAddress);
			throw new ServerException(e.getMessage());
		} catch (RemoteException e) {
//			HTMLLogger.error(Exceptions.UNABLE_TO_INITIATE_CONNECTION + " : "
//					+ hostAddress);
			throw new ServerException(e.getMessage());
		} catch (NotBoundException e) {
//			HTMLLogger.error(Exceptions.SERVER_UNBOUND + " : " + hostAddress);
			throw new ServerException(e.getMessage());
		}
	}

	public String getHostAddress() {
		InetAddress addr = null;
		try {
			addr = InetAddress.getLocalHost();
		} catch (UnknownHostException e1) {
//			HTMLLogger.warn(Exceptions.UNABLE_TO_READ_HOST);
		}
		if (addr != null) {
			return addr.getHostAddress();
		}
		return "localhost";
	}

	/**
	 * Method for getting host interface by its address
	 * 
	 * @param host
	 */
	public ServerIntf getPeerByAddress(String hostAddress) {
		return peers.get(hostAddress);
	}

	/**
	 * Returns list of host names this workspace is currently connected to
	 * 
	 * @return
	 */

	public List<String> getPeerHosts() {
		return (List<String>) peers.keySet();
	}

	public static ConnectionManager getInstance() {
		if (manager == null) {
			manager = new ConnectionManager();
			manager.peers = new HashMap<String, ServerIntf>();
			manager.peersHosts =  new Peers();
		}
		return manager;
	}

}

// Add Project : exposeIntf
// Watch Project : exposeIntf
// add host you connected to hosts list in local config
// initiateConnections
// call peers intf to add the new host in their remote config