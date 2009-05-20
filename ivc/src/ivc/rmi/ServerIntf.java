/**
 * 
 */
package ivc.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author danielan
 *
 */
public interface ServerIntf extends Remote {

	
	public boolean connectPeerToPeer(String host1, String host2) throws RemoteException;
	
	public void getBaseVersion() throws RemoteException;
	
	public void getHeadVersion() throws RemoteException;
	
	public void exposeClientIntf(String hostAddress,ClientIntf client) throws RemoteException ;
}
    