/**
 * 
 */
package ivc.rmi.server;

import ivc.data.BaseVersion;
import ivc.data.TransformationHistory;
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
import java.util.List;

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
	public BaseVersion getBaseVersion() throws RemoteException {
		return (BaseVersion) FileHandler.readObjectFromFile(ServerBusiness.PROJECTPATH +Constants.BaseVersionFile);	
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ivc.rmi.ServerIntf#getHeadVersion()
	 */
	@Override
	public List<TransformationHistory> getHeadVersion() throws RemoteException {
		List<TransformationHistory> tranformations = new ArrayList<TransformationHistory>(); 
		Object readObjectFromFile = FileHandler.readObjectFromFile(ServerBusiness.PROJECTPATH +Constants.CommitedLog);
		if (readObjectFromFile != null){
			 tranformations  = (ArrayList<TransformationHistory>) readObjectFromFile;
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
			Naming.rebind("rmi://" + ServerBusiness.getHostAddress() + ":" + 1099 + "/" + "client_ivc_" + hostAddress, client);
			String peerFilePath = Constants.IvcFolder+"\\"+Constants.Peers;
			ArrayList<String> peers  = (ArrayList<String>) FileHandler.readObjectFromFile(peerFilePath);
			if (peers == null){
				peers  = new ArrayList<String>();
			}
			 if (!peers.contains(hostAddress)){
				 peers.add(hostAddress);
				 FileHandler.writeObjectToFile(peerFilePath,peers);
			 }
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
					+ ServerBusiness.getHostAddress() + ":" + 1099 + "/" + "client_ivc_" + hostAddress);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see ivc.rmi.server.ServerIntf#receiveBaseVersion(ivc.data.BaseVersion)
	 */
	@Override
	public void receiveBaseVersion(BaseVersion bv) throws RemoteException {
		initRepository(ServerBusiness.PROJECTPATH);
		FileHandler.writeObjectToFile(ServerBusiness.PROJECTPATH + Constants.BaseVersionFile,bv);		
	}

	/* (non-Javadoc)
	 * @see ivc.rmi.server.ServerIntf#getClientHosts()
	 */
	@Override
	public List<String> getClientHosts() throws RemoteException {
		return (List<String>) FileHandler.readObjectFromFile(ServerBusiness.PROJECTPATH +Constants.Peers);
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
		File peersFile = new File(projectPath + Constants.Peers);
		try {
			peersFile.createNewFile();
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
//		BaseVersion bv = (BaseVersion)FileHandler.readObjectFromFile(ServerBusiness.PROJECTPATH + "\\.ivc\\.bv");
//		createFolderStructure(bv.getFolders());
//		createFileStructure(bv.getFiles());
	}

}
