/**
 * 
 */
package ivc.rmi;

import ivc.data.exception.ServerException;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * @author danielan
 *
 */
public class ServerImpl  extends UnicastRemoteObject implements ServerIntf {

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

	/* (non-Javadoc)
	 * @see ivc.rmi.ServerIntf#connectPeerToPeer(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean connectPeerToPeer(String host1, String host2)
			throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see ivc.rmi.ServerIntf#getBaseVersion()
	 */
	@Override
	public void getBaseVersion() throws RemoteException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ivc.rmi.ServerIntf#getHeadVersion()
	 */
	@Override
	public void getHeadVersion() throws RemoteException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ivc.rmi.ServerIntf#exposeClientIntf()
	 */
	@Override
	public void exposeClientIntf(String hostAddress,ClientIntf client) throws RemoteException {
		// TODO Auto-generated method stub
		try {
			// create registry
			try {
				String hostAddress = "localhost";
				InetAddress addr = null;
				try {
					addr = InetAddress.getLocalHost();
				} catch (UnknownHostException e1) {
					e1.printStackTrace();
				}
				if (addr != null) {
					hostAddress = addr.getHostAddress();
				}
				Naming.rebind("rmi://" + addr.getHostAddress() + ":" + 1099 + "/"
						+ "client_ivc", client);
			} catch (Exception e) {
				if (e instanceof AlreadyBoundException) {
					e.printStackTrace();
				} else {
					String msg = e.getMessage();
					e.printStackTrace();
					throw new ServerException(e.getMessage());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
