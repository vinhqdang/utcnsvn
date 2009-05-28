package ivc.connection;

import ivc.data.Peers;
import ivc.data.exception.Exceptions;
import ivc.data.exception.IVCException;
import ivc.rmi.client.ClientImpl;
import ivc.rmi.client.ClientIntf;
import ivc.rmi.server.ServerIntf;
import ivc.util.Constants;
import ivc.util.FileUtils;
import ivc.util.NetworkUtils;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
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

	private Map<String, ClientIntf> peers;

	private static Peers peersHosts;

	private static ServerIntf server;

	private static String serverAddress;

	private ConnectionManager() {
	}

	/**
	 * Called at startup
	 * 
	 * @throws IVCException
	 */
	public void initiateConnections() throws IVCException {
		// connect to server
		String serverAddress = (String) FileUtils.readObjectFromFile(Constants.ServerFile);
		server = connectToServer(serverAddress);

		// register server for rmi connection in order to allow other users to
		// communicate with
		// after the first server exposes its interface the others can connect
		// to it
		exposeInterface();

		// read list of hosts with whom to communicate from server file and
		// connect to them

		List<String> hosts;
		try {
			hosts = server.getConnectedClientHosts();
			if (hosts != null) {
				Iterator<String> it = hosts.iterator();
				while (it.hasNext()) {
					String peerHost = it.next();
					ClientIntf peer = null;
					try {
						peer = connectToInterface(peerHost);
						if (peer != null) {
							peers.put(peerHost, peer);
							peersHosts.addPeerHost(peerHost);
						}
					} catch (IVCException e) {
						// HTMLLogger.error("Unable to connect to peer host :" +
						// peerHost);
						e.logError();
					}
					if (peer != null) {
						peers.put(peerHost, peer);
					}
				}
			}
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public ServerIntf connectToServer(String serverAddress) throws IVCException  {
		this.serverAddress = serverAddress;
		try {
			server = (ServerIntf) Naming.lookup(serverAddress);
			return server;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new IVCException(Exceptions.SERVER_CONNECTION_FAILED);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new IVCException(Exceptions.SERVER_CONNECTION_FAILED);
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new IVCException(Exceptions.SERVER_CONNECTION_FAILED);
		}
	}

	public void exposeInterface() {
		try {
			server.exposeClientIntf(NetworkUtils.getHostAddress(), new ClientImpl());
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public ClientIntf connectToInterface(String hostAddress) throws IVCException {

		ClientIntf client;
		try {
			client = server.getClientIntf(hostAddress);
			if (client != null) {
				peers.put(hostAddress, client);
				peersHosts.addPeerHost(hostAddress);
				return client;
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// if connection succedded : add intf to list of peers and write to
		// config file
		return null;

	}

	/**
	 * Method for getting host interface by its address
	 * 
	 * @param host
	 */
	public ClientIntf getPeerByAddress(String hostAddress) {
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

	public ServerIntf getServer() {
		return server;
	}
	
	public List<ClientIntf> getPeers(){
		return (List<ClientIntf>)peers.values();
	}

	public static ConnectionManager getInstance() {
		if (manager == null) {
			manager = new ConnectionManager();
			manager.peers = new HashMap<String, ClientIntf>();
			manager.peersHosts = new Peers();
		}
		return manager;
	}

}
