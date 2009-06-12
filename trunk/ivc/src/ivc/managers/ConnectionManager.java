package ivc.managers;

import ivc.data.Peer;
import ivc.data.exception.Exceptions;
import ivc.data.exception.IVCException;
import ivc.rmi.client.ClientImpl;
import ivc.rmi.client.ClientIntf;
import ivc.rmi.server.ServerIntf;
import ivc.util.NetworkUtils;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ConnectionManager implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Map<String, ConnectionManager> managers;

	private Map<String, ClientIntf> peers;

	private static List<String> peersHosts;

	private static ServerIntf server;

	private ConnectionManager() {
	}

	/**
	 * Called at startup
	 * 
	 * @throws IVCException
	 */
	public Map<String, ClientIntf> initiateConnections(String serverAddress, String projectPath) throws IVCException {
		// connect to server
		server = connectToServer(serverAddress);

		// register server for rmi connection in order to allow other users to
		// communicate with
		// after the first server exposes its interface the others can connect
		// to it
		exposeInterface(projectPath);

		// read list of hosts with whom to communicate from server file and
		// connect to them

		List<Peer> hosts;
		try {
			hosts = server.getConnectedClientHosts(projectPath);
			if (hosts != null) {
				Iterator<Peer> it = hosts.iterator();
				while (it.hasNext()) {
					Peer peerHost = it.next();
					ClientIntf peer = null;
					try {
						if (!peerHost.getHostAddress().equalsIgnoreCase(NetworkUtils.getHostAddress())) {
							peer = connectToInterface(peerHost.getHostAddress());
							if (peer != null) {
								peers.put(peerHost.getHostAddress(), peer);
								peersHosts.add(peerHost.getHostAddress());
								// notify the other peer that i'm awake now and it can communicate with me
								peer.handleNewPeerConnected(projectPath, NetworkUtils.getHostAddress());
							}
						}
					} catch (IVCException e) {
						e.printStackTrace();
					}
					if (peer != null) {
						peers.put(peerHost.getHostAddress(), peer);
					}
				}
			}
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return peers;
	}

	public static ServerIntf connectToServer(String serverAddress) throws IVCException {
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

	public void exposeInterface(String projectPath) {
		try {
			server.exposeClientIntf(NetworkUtils.getHostAddress(), projectPath, new ClientImpl());
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
				peersHosts.add(hostAddress);
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
		return new ArrayList<String>(peers.keySet());
	}

	public List<ClientIntf> getPeers() {
		return new ArrayList<ClientIntf>(peers.values());
	}

	public ServerIntf getServer() {
		return server;
	}

	public void disconnectFromServer() {
		try {
			server.disconnectHost(NetworkUtils.getHostAddress());
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void disconnectFromHost(String hostAddress) {
		peersHosts.remove(hostAddress);
		peers.remove(hostAddress);
	}

	public static ConnectionManager getInstance(String projectName) {
		if (managers == null) {
			managers = new HashMap<String, ConnectionManager>();
		}
		if (!managers.keySet().contains(projectName)) {
			ConnectionManager manager = new ConnectionManager();
			manager.peers = new HashMap<String, ClientIntf>();
			peersHosts = new ArrayList<String>();
			managers.put(projectName, manager);
		}
		return managers.get(projectName);
	}

}
