package ivc.rmi.client;

import ivc.connection.ConnectionManager;
import ivc.data.Transformation;
import ivc.data.TransformationHistoryList;
import ivc.data.commands.CommandArgs;
import ivc.data.commands.ConnectToPeerCommand;
import ivc.data.commands.Result;
import ivc.manager.ProjectsManager;
import ivc.util.Constants;
import ivc.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import org.eclipse.core.resources.IProject;

public class ClientImpl extends UnicastRemoteObject implements ClientIntf {

	/**
	 * default serial version
	 */
	private static final long serialVersionUID = 1L;

	public ClientImpl() throws RemoteException {
		super();
	}

	@Override
	public Result sendTransformation(Transformation transformation) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ivc.rmi.client.ClientIntf#createRLUFile(java.lang.String)
	 */
	@Override
	public void createRLUFile(String projectServerPath, String host) throws RemoteException {
		String projPath = ProjectsManager.instance().getProjectPath(projectServerPath);
		if (projPath != null) {
			File rlufile = new File(projPath + Constants.IvcFolder + Constants.RemoteUnCommitedLog + "_" + host.replaceAll(".", "_"));
			try {
				rlufile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ivc.rmi.client.ClientIntf#updateRCL(java.lang.String, ivc.data.TransformationHistoryList)
	 */
	@Override
	public void updateRCL(String projectName, TransformationHistoryList thl) throws RemoteException {
		TransformationHistoryList oldThl = (TransformationHistoryList) FileUtils.readObjectFromFile(projectName + Constants.RemoteCommitedLog);
		TransformationHistoryList newThl = oldThl.appendTransformationHistoryList(thl);
		FileUtils.writeObjectToFile(projectName + Constants.RemoteCommitedLog, newThl);

	}

}
