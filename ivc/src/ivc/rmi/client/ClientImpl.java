package ivc.rmi.client;

import ivc.commands.CommandArgs;
import ivc.commands.UpdateAnnotationsCommand;
import ivc.data.IVCProject;
import ivc.data.exception.IVCException;
import ivc.data.operation.Operation;
import ivc.data.operation.OperationHistory;
import ivc.data.operation.OperationHistoryList;
import ivc.managers.ConnectionManager;
import ivc.managers.ProjectsManager;
import ivc.util.Constants;
import ivc.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Iterator;

public class ClientImpl extends UnicastRemoteObject implements ClientIntf {

	/**
	 * default serial version
	 */
	private static final long serialVersionUID = 1L;

	public ClientImpl() throws RemoteException {
		super();
	}

	@Override
	public void receiveTransformation(Operation operation) throws RemoteException {
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
			File rlufile = new File(projPath + Constants.IvcFolder + Constants.RemoteUnCommitedLog + "_" + host.replaceAll("\\.", "_"));
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
	 * @see ivc.rmi.client.ClientIntf#updateRCL(java.lang.String, ivc.data.OperationHistoryList)
	 */
	@Override
	public void updateRCL(String projectServerPath, String sourceHost, OperationHistoryList ohl) throws RemoteException {
		if (ohl == null || ohl.getOperationHist() == null) {
			return;
		}
		IVCProject project = ProjectsManager.instance().getIVCProjectByServerPath(projectServerPath);
		UpdateAnnotationsCommand command = new UpdateAnnotationsCommand();
		CommandArgs args = new CommandArgs();
		args.putArgument(Constants.IVCPROJECT, project);
		args.putArgument(Constants.HOST_ADDRESS, sourceHost);
		args.putArgument(Constants.ISCOMMIT, Boolean.TRUE);
		Iterator<OperationHistory> it = ohl.iterator();
		while (it.hasNext()) {
			OperationHistory oh = it.next();
			args.putArgument(Constants.OPERATION_HIST, oh);
			command.execute(args);
		}
		// OperationHistoryList oldThl = project.getRemoteCommitedLog();
		// OperationHistoryList newThl = oldThl.appendOperationHistoryList(thl);
		// project.setRemoteCommitedLog(newThl);
		OperationHistoryList rul = project.getRemoteUncommitedLog(sourceHost);
		OperationHistoryList newrul = rul.removeOperationHistList(ohl);
		project.setRemoteUncommitedLog(newrul, sourceHost);
	}

	/*
	 * (non-Javadoc)
	 *  
	 * @see ivc.rmi.client.ClientIntf#handleNewPeerConnected(java.lang.String, java.lang.String)
	 */
	@Override
	public void handleNewPeerConnected(String projectServerPath, String hostAddress) throws RemoteException {
		IVCProject project = ProjectsManager.instance().getIVCProjectByServerPath(projectServerPath);
		if (project != null) {
			ConnectionManager connManager = ConnectionManager.getInstance(project.getName());
			try {
				connManager.connectToInterface(hostAddress);
			} catch (IVCException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
		}
		// if don't have rul for them ... create one
		File f = new File(project.getProject().getLocation().toOSString() + Constants.IvcFolder + Constants.RemoteUnCommitedLog + "_"
				+ hostAddress.replaceAll("\\.", "_"));
		if (!f.exists()) {
			try {
				f.createNewFile();
				FileUtils.writeObjectToFile(f.getAbsolutePath(), new OperationHistoryList());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ivc.rmi.client.ClientIntf#updateRUL(java.lang.String, java.lang.String, ivc.data.OperationHistoryList)
	 */
	@Override
	public void updateRUL(String projectServerPath, String sourceHost, OperationHistoryList thl) throws RemoteException {
		IVCProject project = ProjectsManager.instance().getIVCProjectByServerPath(projectServerPath);

		UpdateAnnotationsCommand command = new UpdateAnnotationsCommand();
		CommandArgs args = new CommandArgs();
		args.putArgument(Constants.IVCPROJECT, project);
		args.putArgument(Constants.HOST_ADDRESS, sourceHost);
		args.putArgument(Constants.ISCOMMIT, Boolean.FALSE);
		Iterator<OperationHistory> it = thl.iterator();
		while (it.hasNext()) {
			OperationHistory oh = it.next();
			args.putArgument(Constants.OPERATION_HIST, oh);
			command.execute(args);
		}
		// File f = new File(project.getProject().getLocation().toOSString() + Constants.IvcFolder + Constants.RemoteUnCommitedLog + "_"
		// + sourceHost.replaceAll("\\.", "_"));
		// if (!f.exists()) {
		// try {
		// f.createNewFile();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
		// Object ofromFile = FileUtils.readObjectFromFile(f.getAbsolutePath());
		// if (ofromFile == null) {
		// ofromFile = new OperationHistoryList();
		// }
		// OperationHistoryList currentThl = (OperationHistoryList) ofromFile;
		// OperationHistoryList newThl = currentThl.appendOperationHistoryList(thl);
		// FileUtils.writeObjectToFile(f.getAbsolutePath(), newThl);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ivc.rmi.client.ClientIntf#handleNewPeerDisconnected(java.lang.String, java.lang.String)
	 */
	@Override
	public void handleNewPeerDisconnected(String projectServerPath, String hostAddress) throws RemoteException {
		IVCProject project = ProjectsManager.instance().getIVCProjectByServerPath(projectServerPath);
		ConnectionManager.getInstance(project.getName()).disconnectFromHost(hostAddress);

	}

}
