package ivc.rmi.client;

import ivc.connection.ConnectionManager;
import ivc.data.IVCProject;
import ivc.data.Transformation;
import ivc.data.TransformationHistoryList;
import ivc.data.commands.CommandArgs;
import ivc.data.commands.ConnectToPeerCommand;
import ivc.data.commands.Result;
import ivc.data.exception.IVCException;
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
	public void receiveTransformation(Transformation transformation) throws RemoteException {
		// TODO 1.receive transformation
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ivc.rmi.client.ClientIntf#createRLUFile(java.lang.String)
	 */
	@Override
	public void createRULFile(String projectServerPath, String host) throws RemoteException {
		IVCProject project = ProjectsManager.instance().getIVCProjectByServerPath(projectServerPath);		
		String projPath = project.getProject().getLocation().toOSString();
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
	public void updateRCL(String projectServerPath, String sourceHost, TransformationHistoryList thl) throws RemoteException {		
		IVCProject project = ProjectsManager.instance().getIVCProjectByServerPath(projectServerPath);		
		TransformationHistoryList oldThl = (TransformationHistoryList) FileUtils.readObjectFromFile(project.getProject().getLocation().toOSString() + Constants.RemoteCommitedLog);
		TransformationHistoryList newThl = oldThl.appendTransformationHistoryList(thl);
		FileUtils.writeObjectToFile(project.getProject().getLocation().toOSString() + Constants.RemoteCommitedLog, newThl);
		File f  = new File(project.getProject().getLocation().toOSString() +Constants.IvcFolder + Constants.RemoteUnCommitedLog+"_" + sourceHost.replaceAll(".", "_"));
		if (!f.exists()){
			try {
				f.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Object o = FileUtils.readObjectFromFile(f.getAbsolutePath());
		if (o == null){
			return;
		}
		if (o instanceof TransformationHistoryList){
			TransformationHistoryList rul = (TransformationHistoryList) o;
			TransformationHistoryList newrul = rul.removeTransformationHistList(thl);
			FileUtils.writeObjectToFile(f.getAbsolutePath(),newrul);
		}
	}

	/* (non-Javadoc)
	 * @see ivc.rmi.client.ClientIntf#handleNewPeerConnected(java.lang.String, java.lang.String)
	 */
	@Override
	public void handleNewPeerConnected(String projectServerPath, String hostAddress) throws RemoteException {
		IVCProject project = ProjectsManager.instance().getIVCProjectByServerPath(projectServerPath);	
		ConnectionManager connManager = ConnectionManager.getInstance(project.getName());
		try {
			connManager.connectToInterface(hostAddress);
		} catch (IVCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		//if don't have rul for them ... create one
		File f  = new File(project.getProject().getLocation().toOSString() + Constants.IvcFolder + Constants.RemoteUnCommitedLog+"_" + hostAddress.replaceAll(".", "_"));
		if (!f.exists()){
			try {
				f.createNewFile();
				FileUtils.writeObjectToFile(f.getAbsolutePath(), new TransformationHistoryList());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/* (non-Javadoc)
	 * @see ivc.rmi.client.ClientIntf#updateRUL(java.lang.String, java.lang.String, ivc.data.TransformationHistoryList)
	 */
	@Override
	public void updateRUL(String projectServerPath, String sourceHost, TransformationHistoryList thl) throws RemoteException {
		IVCProject project = ProjectsManager.instance().getIVCProjectByServerPath(projectServerPath);	
		File f  = new File(project.getProject().getLocation().toOSString() +Constants.IvcFolder + Constants.RemoteUnCommitedLog+"_" + sourceHost.replaceAll(".", "_"));
		if (!f.exists()){
			try {
				f.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Object ofromFile = FileUtils.readObjectFromFile(f.getAbsolutePath());
		if (ofromFile == null){
			ofromFile = new TransformationHistoryList();
		}
		TransformationHistoryList currentThl = (TransformationHistoryList) ofromFile;
		TransformationHistoryList newThl = currentThl.appendTransformationHistoryList(thl);
		FileUtils.writeObjectToFile(f.getAbsolutePath(), newThl);
		
	}

	/* (non-Javadoc)
	 * @see ivc.rmi.client.ClientIntf#handleNewPeerDisconnected(java.lang.String, java.lang.String)
	 */
	@Override
	public void handleNewPeerDisconnected(String projectServerPath, String hostAddress) throws RemoteException {
		IVCProject project = ProjectsManager.instance().getIVCProjectByServerPath(projectServerPath);	
		ConnectionManager.getInstance(project.getName()).disconnectFromHost(hostAddress);
		
	}

}
