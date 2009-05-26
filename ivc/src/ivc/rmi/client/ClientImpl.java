package ivc.rmi.client;

import ivc.data.Result;
import ivc.data.Transformation;
import ivc.data.TransformationHistoryList;
import ivc.data.command.CommandArgs;
import ivc.data.command.ConnectToPeerCommand;
import ivc.util.ConnectionManager;
import ivc.util.Constants;
import ivc.util.FileHandler;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class ClientImpl extends UnicastRemoteObject implements ClientIntf {
	
	/**
	 * default serial version
	 */
	private static final long serialVersionUID = 1L;

	public ClientImpl() throws RemoteException {
		super();
	}

	@Override
	public Result sendTransformation(Transformation transformation)
			throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ivc.rmi.client.ClientIntf#createRLUFile(java.lang.String)
	 */
	@Override
	public void createRLUFile(String host) throws RemoteException {
		File rlufile = new File("projectPath" + Constants.RemoteUnCommitedLog+"_"+host);
		try {
			rlufile.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	/* (non-Javadoc)
	 * @see ivc.rmi.client.ClientIntf#updateRCL(java.lang.String, ivc.data.TransformationHistoryList)
	 */
	@Override
	public void updateRCL(String projectName, TransformationHistoryList thl)
			throws RemoteException {
		TransformationHistoryList oldThl = (TransformationHistoryList) FileHandler.readObjectFromFile(projectName+Constants.RemoteCommitedLog);
		TransformationHistoryList newThl = oldThl.appendTransformationHistoryList(thl);
		FileHandler.writeObjectToFile(projectName+Constants.RemoteCommitedLog,newThl);
		
	}

}
