package ivc.rmi.client;

import ivc.data.Result;
import ivc.data.Transformation;
import ivc.data.TransformationHistory;
import ivc.data.TransformationHistoryList;
import ivc.data.command.CommandArgs;

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
	public Result sendTransformation(Transformation transformation) throws RemoteException;
	
	public void createRLUFile(String host) throws RemoteException;
	
	public void updateRCL(String projectName,TransformationHistoryList thl )throws RemoteException;
	
	
	
}
