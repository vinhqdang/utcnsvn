package ivc.rmi.client;

import ivc.data.Transformation;
import ivc.data.TransformationHistory;
import ivc.data.TransformationHistoryList;
import ivc.data.commands.CommandArgs;
import ivc.data.commands.Result;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ClientIntf extends Remote {
	

	/**
	 * Notifies all workspaces that a transform took place. If the client that executes 
	 * this method is the sender the notification is ignored, otherwise it refreshes the 
	 * log files and updates annotations.
	 * 
	 * @param transformation
	 * @return
	 * @throws RemoteException
	 */
	public void receiveTransformation(Transformation transformation) throws RemoteException;
	
	
	/**
	 * Called whenever a new peer performs a checkout 
	 * @param projectServerPath
	 * @param host
	 * @throws RemoteException
	 */
	public void createRULFile(String projectServerPath,String host) throws RemoteException;
	
	
	/**
	 * Called at checkout
	 * @param projectName
	 * @param thl
	 * @throws RemoteException
	 */
	public void updateRCL(String projectName,TransformationHistoryList thl )throws RemoteException;
	
	/**
	 * Called when a peer exposes its interface
	 * @param projectName
	 * @param hostAddress
	 * @throws RemoteException
	 */
	public void handleNewPeerConnected (String projectName, String hostAddress) throws RemoteException;
	
}
