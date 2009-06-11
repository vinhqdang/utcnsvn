/**
 * 
 */
package ivc.rmi.server;

import ivc.data.BaseVersion;
import ivc.data.Peer;
import ivc.data.operation.OperationHistoryList;
import ivc.rmi.client.ClientIntf;
import ivc.util.Constants;
import ivc.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author danielan
 * 
 */
final class Users {
	public static HashMap<String, String> users;
	static {
		users = new HashMap<String, String>();
		users.put("a", "a");
	}

	public static boolean authenticate(String username, String password) {
		if (!users.containsKey(username.toLowerCase()))
			return false;
		return (users.get(username).equals(password));
	}
}

public class ServerImpl extends UnicastRemoteObject implements ServerIntf {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @throws RemoteException
	 */
	protected ServerImpl() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ivc.rmi.ServerIntf#getBaseVersion()
	 */
	@Override
	public BaseVersion returnBaseVersion(String projectPath) throws RemoteException {
		return (BaseVersion) FileUtils.readObjectFromFile(Constants.RepositoryFolder + projectPath + Constants.BaseVersionFile);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ivc.rmi.ServerIntf#getHeadVersion()
	 */
	@Override
	public OperationHistoryList returnHeadVersion(String projectPath) throws RemoteException {
		OperationHistoryList tranformations = new OperationHistoryList();
		Object readObjectFromFile = FileUtils.readObjectFromFile(Constants.RepositoryFolder + projectPath + Constants.CommitedLog);
		if (readObjectFromFile != null && readObjectFromFile instanceof OperationHistoryList) {
			tranformations = (OperationHistoryList) readObjectFromFile;
		}
		return tranformations;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ivc.rmi.ServerIntf#exposeClientIntf()
	 */
	@Override
	public void exposeClientIntf(String hostAddress, String projectPath, ClientIntf client) throws RemoteException {
		try {
			RMIUtils.exposeClientInterface(hostAddress, client);
			String peerFilePath = Constants.RepositoryFolder + Constants.Peers;
			ArrayList<Peer> peers = null;
			File f = new File(peerFilePath);
			if (!f.exists()) {
				f.createNewFile();
			} else {
				try {
					peers = (ArrayList<Peer>) FileUtils.readObjectFromFile(peerFilePath);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (peers == null) {
				peers = new ArrayList<Peer>();
			}
			Iterator<Peer> it = peers.iterator();
			boolean contains = false;
			while (it.hasNext()) {
				Peer peer = it.next();
				if (peer.getHostAddress().equalsIgnoreCase(hostAddress)) {
					contains = true;
					if (!peer.getProjectPaths().contains(projectPath)) {
						peer.getProjectPaths().add(projectPath);
						peer.setConnectionStatus(Constants.CONNECTED);
					}
				}
			}
			if (!contains) {
				List<String> projectPaths = new ArrayList<String>();
				projectPaths.add(projectPath);
				peers.add(new Peer(hostAddress, projectPaths, Constants.CONNECTED));
			}
			FileUtils.writeObjectToFile(peerFilePath, peers);
			System.out.println("Peer connected from address:" + hostAddress);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ivc.rmi.ServerIntf#getClientIntf(java.lang.String)
	 */
	@Override
	public ClientIntf getClientIntf(String hostAddress) throws RemoteException {
		return RMIUtils.getClientIntf(hostAddress);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ivc.rmi.server.ServerIntf#receiveBaseVersion(ivc.data.BaseVersion)
	 */
	@Override
	public void receiveBaseVersion(String projectPath, BaseVersion bv) throws RemoteException {
		RepositoryUtils.initRepository(projectPath);
		FileUtils.writeObjectToFile(Constants.RepositoryFolder + projectPath + Constants.BaseVersionFile, bv);
		HashMap<String, Integer> cv = new HashMap<String, Integer>();
		Set<String> files = (Set<String>) bv.getFiles().keySet();
		for (Iterator<String> iterator = files.iterator(); iterator.hasNext();) {
			String file = iterator.next();
			cv.put(file, 1);
		}
		cv.put(projectPath, 1);
		FileUtils.writeObjectToFile(Constants.RepositoryFolder + projectPath + Constants.CurrentVersionFile, cv);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ivc.rmi.server.ServerIntf#getClientHosts()
	 */
	@Override
	public List<Peer> getAllClientHosts(String projectPath) throws RemoteException {
		List<Peer> hosts = new ArrayList<Peer>();
		List<Peer> allHosts = (List<Peer>) FileUtils.readObjectFromFile(Constants.RepositoryFolder + Constants.Peers);
		if (allHosts != null) {
			Iterator<Peer> it = allHosts.iterator();
			while (it.hasNext()) {
				Peer peer = it.next();
				if (peer.getProjectPaths().contains(projectPath)) {
					hosts.add(peer);
				}
			}
		}
		return hosts;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ivc.rmi.server.ServerIntf#authenticateHost(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean authenticateHost(String username, String password) throws RemoteException {
		return Users.authenticate(username, password);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ivc.rmi.server.ServerIntf#getVersionNumber()
	 */
	@Override
	public HashMap<String, Integer> getVersionNumber(String projectPath) throws RemoteException {
		return (HashMap<String, Integer>) FileUtils.readObjectFromFile(Constants.RepositoryFolder + projectPath + Constants.CurrentVersionFile);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ivc.rmi.server.ServerIntf#updateHeadVersion()
	 */
	@Override
	public void updateHeadVersion(String projectPath, OperationHistoryList thl) throws RemoteException {
		Object obj = FileUtils.readObjectFromFile(Constants.RepositoryFolder + projectPath + Constants.CommitedLog);
		OperationHistoryList oldThl = new OperationHistoryList();
		if (obj != null && obj instanceof OperationHistoryList) {
			oldThl = (OperationHistoryList) obj;
		}
		oldThl.appendOperationHistoryList(thl);
		FileUtils.writeObjectToFile(Constants.RepositoryFolder + projectPath + Constants.CommitedLog, thl);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ivc.rmi.server.ServerIntf#updateVersionNumber(java.lang.String)
	 */
	@Override
	public void updateVersionNumber(String projectPath, HashMap<String, Integer> versionNumber) throws RemoteException {
		FileUtils.writeObjectToFile(Constants.RepositoryFolder + projectPath + Constants.CurrentVersionFile, versionNumber);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ivc.rmi.server.ServerIntf#getConnectedClientHosts()
	 */
	@Override
	public List<Peer> getConnectedClientHosts(String projectPath) throws RemoteException {
		List<Peer> hosts = new ArrayList<Peer>();
		List<Peer> allHosts = (List<Peer>) FileUtils.readObjectFromFile(Constants.RepositoryFolder + Constants.Peers);
		if (allHosts != null) {
			Iterator<Peer> it = allHosts.iterator();
			while (it.hasNext()) {
				Peer peer = it.next();
				if (peer.getConnectionStatus().equalsIgnoreCase(Constants.CONNECTED) && peer.getProjectPaths().contains(projectPath)) {
					hosts.add(peer);
				}
			}
		}
		return hosts;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ivc.rmi.server.ServerIntf#updatePendingRCL(java.lang.String, ivc.data.OperationHistoryList)
	 */
	@Override
	public void updatePendingRCL(String projectPath, List<String> hosts, OperationHistoryList thl) throws RemoteException {
		if (hosts == null) {
			return;
		}
		Iterator<String> it = hosts.iterator();
		while (it.hasNext()) {
			String host = it.next();
			File f = new File(Constants.RepositoryFolder + projectPath + Constants.PendingRemoteCommitedLog + "_" + host.replaceAll("\\.", "_"));
			if (!f.exists()) {
				try {
					f.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			OperationHistoryList oldThl = new OperationHistoryList();
			if (FileUtils.readObjectFromFile(f.getAbsolutePath()) != null
					&& FileUtils.readObjectFromFile(f.getAbsolutePath()) instanceof OperationHistoryList) {
				oldThl = (OperationHistoryList) FileUtils.readObjectFromFile(f.getAbsolutePath());
			}

			oldThl.appendOperationHistoryList(thl);
			FileUtils.writeObjectToFile(f.getAbsolutePath(), thl);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ivc.rmi.server.ServerIntf#checkProjectPath(java.lang.String)
	 */
	@Override
	public boolean checkProjectPath(String projectPath) throws RemoteException {
		return RepositoryUtils.checkProjectPath(projectPath);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ivc.rmi.server.ServerIntf#returnPendingRCL(java.lang.String, java.lang.String)
	 */
	@Override
	public OperationHistoryList returnPendingRCL(String projectPath, String hostAddress) throws RemoteException {
		OperationHistoryList thl = null;
		File f = new File(Constants.RepositoryFolder + projectPath + Constants.PendingRemoteCommitedLog + "_" + hostAddress.replaceAll("\\.", "_"));
		if (f.exists()) {
			thl = (OperationHistoryList) FileUtils.readObjectFromFile(f.getAbsolutePath());
			f.delete();
		}
		return thl;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ivc.rmi.server.ServerIntf#returnPendingRUL(java.lang.String, java.lang.String)
	 */
	@Override
	public Map<String, OperationHistoryList> returnPendingRUL(String projectPath, String hostAddress) throws RemoteException {
		Map<String, OperationHistoryList> thl = new HashMap<String, OperationHistoryList>();
		File f = new File(Constants.RepositoryFolder + projectPath + Constants.PendingRemoteUncommitedLog + "_" + hostAddress.replaceAll("\\.", "_"));
		if (f.exists()) {
			Object objectFromFile = FileUtils.readObjectFromFile(f.getAbsolutePath());
			if (objectFromFile != null && objectFromFile instanceof Map) {
				thl = (Map<String, OperationHistoryList>) objectFromFile;
			}
			f.delete();
		}
		return thl;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ivc.rmi.server.ServerIntf#updatePendingRUL(java.lang.String, java.lang.String, java.util.List, ivc.data.OperationHistoryList)
	 */
	@Override
	public void updatePendingRUL(String projectPath, String sourceHost, List<String> hosts, OperationHistoryList thl) throws RemoteException {
		if (hosts == null) {
			return;
		}
		Iterator<String> it = hosts.iterator();
		while (it.hasNext()) {
			String hostAddress = it.next();
			File f = new File(Constants.RepositoryFolder + projectPath + Constants.PendingRemoteUncommitedLog + "_"
					+ hostAddress.replaceAll("\\.", "_"));
			if (!f.exists()) {
				try {
					f.createNewFile();
					HashMap<String, OperationHistoryList> mthl = new HashMap<String, OperationHistoryList>();
					mthl.put(sourceHost, thl);
					FileUtils.writeObjectToFile(f.getAbsolutePath(), mthl);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				Object objFromFile = FileUtils.readObjectFromFile(f.getAbsolutePath());
				if (objFromFile != null && objFromFile instanceof Map) {
					HashMap<String, OperationHistoryList> mthl = (HashMap<String, OperationHistoryList>) objFromFile;
					OperationHistoryList currentThl = mthl.get(sourceHost);
					if (currentThl == null) {
						currentThl = new OperationHistoryList();
					}
					OperationHistoryList newThl = currentThl.appendOperationHistoryList(thl);
					mthl.put(sourceHost, newThl);
					FileUtils.writeObjectToFile(f.getAbsolutePath(), mthl);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ivc.rmi.server.ServerIntf#disconnectHost(java.lang.String)
	 */
	@Override
	public void disconnectHost(String hostAddress) throws RemoteException {
		RMIUtils.disconnectHost(hostAddress);
		ArrayList<Peer> allHosts = (ArrayList<Peer>) FileUtils.readObjectFromFile(Constants.RepositoryFolder + Constants.Peers);
		if (allHosts != null) {
			Iterator<Peer> it = allHosts.iterator();
			while (it.hasNext()) {
				Peer peer = it.next();
				if (peer.getHostAddress().equalsIgnoreCase(hostAddress) && peer.getConnectionStatus().equalsIgnoreCase(Constants.CONNECTED)) {
					peer.setConnectionStatus(Constants.DISCONNECTED);
				}
			}
		}
		FileUtils.writeObjectToFile(Constants.RepositoryFolder + Constants.Peers, allHosts);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ivc.rmi.server.ServerIntf#getBaseVersionForFile(java.lang.String, java.lang.String)
	 */
	@Override
	public StringBuffer getBaseVersionForFile(String projectPath, String filePath) throws RemoteException {
		Object obj = FileUtils.readObjectFromFile(Constants.RepositoryFolder + projectPath + Constants.BaseVersionFile);
		if (obj != null) {
			BaseVersion bv = (BaseVersion) FileUtils.readObjectFromFile(Constants.RepositoryFolder + projectPath + Constants.BaseVersionFile);
			return bv.getFileContent(filePath);
		}
		return null;
	}

}
