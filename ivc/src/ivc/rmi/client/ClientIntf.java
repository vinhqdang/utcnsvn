package ivc.rmi.client;

import ivc.data.Result;
import ivc.data.Transformation;
import ivc.data.command.CommandArgs;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ClientIntf extends Remote {
	
	
	/**
	 * Checks out the project from a node which address is known and has a connection with it;
	 * This node will have to let the others know that there is a new player in the entire game
	 * The sources the new workspace will have consists of the later version or each file
	 * @param args
	 * @return
	 * @throws RemoteException
	 */
	public Result checkout(CommandArgs args)throws RemoteException;
	
	/**
	 * 
	 * @param args
	 * @return
	 * @throws RemoteException
	 */
	public Result update(CommandArgs args) throws RemoteException;
	
	/**
	 * 
	 * @param args
	 * @return Result of the action 
	 * @throws RemoteException
	 */
	public Result commit(CommandArgs args) throws RemoteException;

	/**
	 * Notifies all workspaces that a transform took place. If the client that executes 
	 * this method is the sender the notification is ignored, otherwise it refreshes the 
	 * log files and updates annotations.
	 * 
	 * @param transformation
	 * @return
	 * @throws RemoteException
	 */
	public Result sendTransformation(Transformation transformation) throws RemoteException;
	
	
	/**
	 * Connects to a new host
	 * 
	 * @param hostName
	 * @return
	 * @throws RemoteException
	 */
	public Result connectToPeer(String hostAddress) throws RemoteException;
	
	/**
	 * 
	 * @return List of hostNames this workspace is connected to
	 * @throws RemoteException
	 */
	public List<String> getPeers() throws RemoteException;
	
	
	public void test(String who,String path) throws RemoteException;
	
	public void createRLUFile(String host) throws RemoteException;
	
	
	
}
