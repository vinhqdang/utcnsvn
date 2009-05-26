/**
 * 
 */
package ivc.data.command;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;

import ivc.data.Result;
import ivc.data.exception.ServerException;
import ivc.rmi.client.ClientIntf;
import ivc.util.ConnectionManager;

/**
 * @author danielan
 *
 */
public class ConnectToPeerCommand implements CommandIntf ,Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String hostAddress;
	
	

	/* (non-Javadoc)
	 * @see ivc.command.CommandIntf#execute(ivc.command.CommandArgs)
	 */
	@Override
	public Result execute(CommandArgs args) {
//		hostAddress = (String)args.getArgumentValue("hostAddress");
//		// test if the host is already contained; and if so abort operation
//		ConnectionManager connManager = ConnectionManager.getInstance();
//		String localHostName = connManager.getHostAddress();
//		if (connManager.getPeerHosts().contains(hostAddress)) {
//			return new Result(true,"Host "+hostAddress+" already connected",null);
//		}
//		try {
//			ClientIntf remotePeer = connManager.connectToInterface(hostAddress);
//			// if connection succedded try to get the others to connect to me
//			if (remotePeer != null) {
//				try {
//					List<String> remoteHosts = remotePeer.getPeers();					
//					remoteHosts.add(hostAddress);
//					Iterator<String> it = remoteHosts.iterator();
//					while(it.hasNext()){
//						String host = it.next();
//						// if we have a connection to this peer; ask the peer to connect back
//						if (connManager.getPeerHosts().contains(host)){
//							ClientIntf peer = connManager.getPeerByAddress(host);
//							List<String> peerHosts = peer.getPeers();
//							if (! peerHosts.contains(localHostName)){
//								peer.connectToPeer(localHostName);
//							}
//						}
//						// if no
//						ClientIntf newPeer = connManager.connectToInterface(host);
//						newPeer.connectToPeer(host)	;					
//					}					
//				} catch (RemoteException e) {
////					HTMLLogger.error("Unable to get peer hosts :" + hostAddress);
////					HTMLLogger.error(e.getMessage());
//				}
//			}
//		} catch (ServerException e) {
//			HTMLLogger.error("Unable to connect to peer host :" + hostAddress);
//			e.logError();
//		}
		return  new Result(true, "Connections established" ,null);
	}

}
