package ivc.client.rmi;

import ivc.data.operation.Operation;
import ivc.data.operation.OperationHistoryList;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientIntf extends Remote {

	/**
	 * Creates a new remote uncommitted log file for the client that invokes it to the
	 * client this method belongs. Called whenever a new peer performs a checkout
	 * 
	 * @param projectServerPath
	 *            the path to the repository of the project on server
	 * @param host
	 *            the host for whom the file is created
	 * @throws RemoteException
	 */
	public void createRULFile(String projectServerPath, String host)
			throws RemoteException;

	/**
	 * Receives a list of operation and stores them in the remote committed log file .rcl.
	 * In the same time, the method deletes these operations from the remote uncommitted
	 * log of the user that commits them
	 * 
	 * @param projectServerPath
	 *            represents the path to the repository of the project on server
	 * @param sourceHost
	 *            the user which remote uncommitted log will be updated is specified by
	 *            the parameter
	 * @param thl
	 *            list of operation
	 * @throws RemoteException
	 */
	public void updateRCL(String projectServerPath, String sourceHost,
			OperationHistoryList thl) throws RemoteException;

	/**
	 * Adds the new operation list to the remote uncommitted log file
	 * 
	 * @param projectServerPath
	 *            the path to the repository of the project on server
	 * @param sourceHost
	 *            sourceHost the user which remote uncommitted log will be updated is
	 *            specified by the parameter
	 * @param thl
	 *            list of operation
	 * @throws RemoteException
	 */
	public void updateRUL(String projectServerPath, String sourceHost,
			OperationHistoryList thl) throws RemoteException;

	/**
	 * Connects current client to the new client and create an empty remote uncommitted
	 * log file for it
	 * 
	 * @param projectServerPath
	 *            the path to the repository of the project on server
	 * @param hostAddress
	 *            host address of the client that connects
	 * @throws RemoteException
	 */
	public void handleNewPeerConnected(String projectServerPath, String hostAddress)
			throws RemoteException;

	/**
	 * Removes the reference to the client
	 * 
	 * @param projectServerPath
	 *            the path to the repository of the project on server
	 * @param hostAddress
	 *            host address of the client that disconnects
	 * @throws RemoteException
	 */
	public void handleNewPeerDisconnected(String projectServerPath, String hostAddress)
			throws RemoteException;

}
