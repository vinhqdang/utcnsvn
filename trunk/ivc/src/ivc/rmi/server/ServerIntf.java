/**
 * 
 */
package ivc.rmi.server;

import ivc.data.BaseVersion;
import ivc.data.TransformationHistory;
import ivc.rmi.client.ClientIntf;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * @author danielan
 *
 */
public interface ServerIntf extends Remote {

	
	
	
	public BaseVersion getBaseVersion() throws RemoteException;
	
	public void receiveBaseVersion(BaseVersion bv) throws RemoteException;
	
	public List<TransformationHistory> getHeadVersion() throws RemoteException;
	
	// connection methods
	
	public void exposeClientIntf(String hostAddress,ClientIntf client) throws RemoteException ;
	
	public ClientIntf getClientIntf(String hostAddress) throws RemoteException;
	
	public List<String> getClientHosts() throws RemoteException;
	
	public boolean authenticateHost(String userName, String password) throws RemoteException;
	
}
    