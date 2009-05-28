/**
 * 
 */
package ivc.rmi.server;

import ivc.data.BaseVersion;
import ivc.data.TransformationHistory;
import ivc.data.TransformationHistoryList;
import ivc.rmi.client.ClientIntf;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author danielan
 *
 */
public interface ServerIntf extends Remote {

	
	// repository methods
	
	public BaseVersion returnBaseVersion(String projectPath) throws RemoteException;
	
	public void receiveBaseVersion(String projectPath, BaseVersion bv) throws RemoteException;
	
	public TransformationHistoryList returnHeadVersion(String projectPath) throws RemoteException;
	
	public void updateHeadVersion(String projectPath,TransformationHistoryList thl) throws RemoteException;
	
	public Map<String,Integer> getVersionNumber(String projectPath) throws RemoteException;
	
	public void updateVersionNumber(String projectPath, HashMap<String, Integer> versionNumber) throws RemoteException;
	
	public void updatePendingRCL(String projectPath,List<String> hosts, TransformationHistoryList thl)throws RemoteException;

	
	// client connection methods
	
	public void exposeClientIntf(String hostAddress,ClientIntf client) throws RemoteException ;
	
	public ClientIntf getClientIntf(String hostAddress) throws RemoteException;
	
	public List<String> getAllClientHosts() throws RemoteException;
	
	public List<String> getConnectedClientHosts() throws RemoteException;	
	
	public boolean authenticateHost(String userName, String password) throws RemoteException;
	
	public boolean checkProjectPath(String projectPath) throws RemoteException;
	 
	
}
    