/**
 * 
 */
package ivc.rmi.server;

import ivc.data.BaseVersion;
import ivc.data.TransformationHistory;
import ivc.data.TransformationHistoryList;
import ivc.data.exception.ServerException;
import ivc.rmi.client.ClientIntf;
import ivc.util.Constants;
import ivc.util.FileHandler;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author danielan
 * 
 */
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
	public BaseVersion returnBaseVersion(String projectPath)
			throws RemoteException {
		return (BaseVersion) FileHandler.readObjectFromFile(projectPath
				+ Constants.BaseVersionFile);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ivc.rmi.ServerIntf#getHeadVersion()
	 */
	@Override
	public TransformationHistoryList returnHeadVersion(String projectPath)
			throws RemoteException {
		TransformationHistoryList tranformations = new TransformationHistoryList();
		Object readObjectFromFile = FileHandler.readObjectFromFile(projectPath
				+ Constants.CommitedLog);
		if (readObjectFromFile != null) {
			tranformations = (TransformationHistoryList) readObjectFromFile;
		}
		return tranformations;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ivc.rmi.ServerIntf#exposeClientIntf()
	 */
	@Override
	public void exposeClientIntf(String hostAddress, ClientIntf client)
			throws RemoteException {
		try {
			// create registry
			Naming.rebind("rmi://" + ServerBusiness.getHostAddress() + ":"
					+ 1099 + "/" + "client_ivc_" + hostAddress, client);
			String peerFilePath = Constants.IvcFolder + "\\" + Constants.Peers;
			HashMap<String, String> peers = (HashMap<String, String>) FileHandler.readObjectFromFile(peerFilePath);
			if (peers == null) {
				peers = new HashMap<String, String>();
			}
			peers.put(hostAddress, Constants.CONNECTED);
			FileHandler.writeObjectToFile(peerFilePath, peers);
		} catch (Exception e) {
			if (e instanceof AlreadyBoundException) {
				e.printStackTrace();
			} else {
				e.printStackTrace();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ivc.rmi.ServerIntf#getClientIntf(java.lang.String)
	 */
	@Override
	public ClientIntf getClientIntf(String hostAddress) throws RemoteException {
		try {
			return (ClientIntf) Naming.lookup("rmi://"
					+ ServerBusiness.getHostAddress() + ":" + 1099 + "/"
					+ "client_ivc_" + hostAddress);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ivc.rmi.server.ServerIntf#receiveBaseVersion(ivc.data.BaseVersion)
	 */
	@Override
	public void receiveBaseVersion(String projectPath, BaseVersion bv)
			throws RemoteException {
		initRepository(projectPath);
		FileHandler.writeObjectToFile(projectPath + Constants.BaseVersionFile,
				bv);
		HashMap<String, Integer> cv = new HashMap<String, Integer>();
		List<String> files = (List<String>) bv.getFiles().keySet();
		for (Iterator<String> iterator = files.iterator(); iterator.hasNext();) {
			String file = iterator.next();
			cv.put(file, 0);
		}
		FileHandler.writeObjectToFile(projectPath
				+ Constants.CurrentVersionFile, cv);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ivc.rmi.server.ServerIntf#getClientHosts()
	 */
	@Override
	public List<String> getAllClientHosts() throws RemoteException {
		return (List<String>) ((Map<String,String>) FileHandler.readObjectFromFile(Constants.Peers)).keySet();
	}

	private void initRepository(String projectPath) throws RemoteException {
		File ivcfolder = new File(projectPath + Constants.IvcFolder);
		ivcfolder.mkdir();
		File bvFile = new File(projectPath + Constants.BaseVersionFile);
		try {

			bvFile.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		File cvFile = new File(projectPath + Constants.CurrentVersionFile);
		try {

			cvFile.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		File peersFile = new File(projectPath + Constants.Peers);
		try {
			peersFile.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		File rclFile = new File(projectPath + Constants.PendingRemoteCommitedLog);
		try {
			rclFile.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		File transFile = new File(projectPath + Constants.CommitedLog);
		try {
			transFile.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// BaseVersion bv =
		// (BaseVersion)FileHandler.readObjectFromFile(ServerBusiness.PROJECTPATH
		// + "\\.ivc\\.bv");
		// createFolderStructure(bv.getFolders());
		// createFileStructure(bv.getFiles());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ivc.rmi.server.ServerIntf#authenticateHost(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public boolean authenticateHost(String userName, String password)
			throws RemoteException {
		// TODO Auto-generated method stub
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ivc.rmi.server.ServerIntf#getVersionNumber()
	 */
	@Override
	public Map<String, Integer> getVersionNumber(String projectPath)
			throws RemoteException {
		return (Map<String, Integer>) FileHandler
				.readObjectFromFile(projectPath + Constants.CurrentVersionFile);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ivc.rmi.server.ServerIntf#updateHeadVersion()
	 */
	@Override
	public void updateHeadVersion(String projectPath,TransformationHistoryList thl) throws RemoteException {
		TransformationHistoryList oldThl = (TransformationHistoryList) FileHandler.readObjectFromFile(projectPath + Constants.CommitedLog);
		oldThl.appendTransformationHistoryList(thl);
		FileHandler.writeObjectToFile(projectPath + Constants.CommitedLog, thl);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ivc.rmi.server.ServerIntf#updateVersionNumber(java.lang.String)
	 */
	@Override
	public void updateVersionNumber(String projectPath,	HashMap<String, Integer> versionNumber) throws RemoteException {
		FileHandler.writeObjectToFile(projectPath
				+ Constants.CurrentVersionFile, versionNumber);
	}

	/* (non-Javadoc)
	 * @see ivc.rmi.server.ServerIntf#getConnectedClientHosts()
	 */
	@Override
	public List<String> getConnectedClientHosts() throws RemoteException {
		List<String> hosts = new ArrayList<String>();
		Map<String,String> allHosts = (Map<String,String>) FileHandler.readObjectFromFile(Constants.Peers); 
		if (allHosts != null){
			Iterator<String> it = allHosts.keySet().iterator();
			while(it.hasNext()){
				String hostAddress =  it.next();
				String hostStatus =  allHosts.get(hostAddress);
				if (hostStatus.equalsIgnoreCase(Constants.CONNECTED)){
					hosts.add(hostAddress);
				}
			}
		}
		return hosts;
	}

	/* (non-Javadoc)
	 * @see ivc.rmi.server.ServerIntf#updatePendingRCL(java.lang.String, ivc.data.TransformationHistoryList)
	 */
	@Override
	public void updatePendingRCL(String projectPath,List<String> hosts ,TransformationHistoryList thl) {
		if(hosts == null){
			return;
		}
		Iterator<String> it = hosts.iterator();
		while(it.hasNext()){
			String host = it.next();			
			TransformationHistoryList oldThl = (TransformationHistoryList) FileHandler.readObjectFromFile(projectPath + Constants.PendingRemoteCommitedLog+"_"+host);
			if (oldThl == null){
				oldThl = new TransformationHistoryList();
			}
			oldThl.appendTransformationHistoryList(thl);
			FileHandler.writeObjectToFile(projectPath + Constants.PendingRemoteCommitedLog+"_"+host, thl);
		}
		
	}

}
