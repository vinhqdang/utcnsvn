/**
 * Defines server API
 */
package ivc.server.rmi;

import ivc.client.rmi.ClientIntf;
import ivc.data.BaseVersion;
import ivc.data.Peer;
import ivc.data.operation.OperationHistoryList;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author danielan
 * 
 */
public interface ServerIntf extends Remote {

	/********************************** repository methods **********************************************/

	// base version
	public BaseVersion returnBaseVersion(String projectPath) throws RemoteException;

	public void receiveBaseVersion(String projectPath, BaseVersion bv)
			throws RemoteException;

	public StringBuffer getBaseVersionForFile(String projectPath, String filePath)
			throws RemoteException;

	// head version

	public OperationHistoryList returnHeadVersion(String projectPath)
			throws RemoteException;

	public void updateHeadVersion(String projectPath, OperationHistoryList thl)
			throws RemoteException;

	// version number

	public HashMap<String, Integer> getVersionNumber(String projectPath)
			throws RemoteException;

	public void updateVersionNumber(String projectPath,
			HashMap<String, Integer> versionNumber) throws RemoteException;

	// pending transfomations

	public void updatePendingRCL(String projectPath, List<String> hosts,
			OperationHistoryList thl) throws RemoteException;

	public OperationHistoryList returnPendingRCL(String projectPath, String hostAddress)
			throws RemoteException;

	public void updatePendingRUL(String projectPath, String sourceHost,
			List<String> hosts, OperationHistoryList thl) throws RemoteException;

	public Map<String, OperationHistoryList> returnPendingRUL(String projectPath,
			String hostAddress) throws RemoteException;

	/********************************** client connection methods **********************************************/

	public void exposeClientIntf(String hostAddress, String projectPath, ClientIntf client)
			throws RemoteException;

	public void disconnectHost(String hostAddress) throws RemoteException;

	public ClientIntf getClientIntf(String hostAddress) throws RemoteException;

	public List<Peer> getAllClientHosts(String projectPath) throws RemoteException;

	public List<Peer> getConnectedClientHosts(String projectPath) throws RemoteException;

	public void removePeerProject(String hostAddress, String projectPath)
			throws RemoteException;

	public boolean authenticateHost(String userName, String password)
			throws RemoteException;

	public boolean checkProjectPath(String projectPath) throws RemoteException;

}
