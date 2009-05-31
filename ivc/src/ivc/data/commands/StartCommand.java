/**
 * 
 */
package ivc.data.commands;

import ivc.connection.ConnectionManager;
import ivc.data.IVCProject;
import ivc.data.TransformationHistoryList;
import ivc.data.exception.Exceptions;
import ivc.data.exception.IVCException;
import ivc.manager.ProjectsManager;
import ivc.rmi.client.ClientIntf;
import ivc.util.Constants;
import ivc.util.FileUtils;
import ivc.util.NetworkUtils;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author danielan
 * 
 */
public class StartCommand implements CommandIntf {



	/*
	 * (non-Javadoc)
	 * 
	 * @see ivc.data.commands.CommandIntf#execute(ivc.data.commands.CommandArgs)
	 */
	@Override	
	public Result execute(CommandArgs args) {
		// 1. find all projects
		ProjectsManager.instance().findProjects();
		
		// 2. handle initiation for each project
		HashMap<String,IVCProject> projects  = ProjectsManager.instance().getProjects();
		if (projects != null){
			Iterator<String> it = projects.keySet().iterator();
			while(it.hasNext()){
				String projectName = it.next();
				IVCProject ivcProject = projects.get(projectName);
				Result r = handleInitiateProject(ivcProject);
				if (!r.isSuccess()){
					return r;
				}
			}
		}
		return new Result(true, "Success", null);
	}
	
	private Result handleInitiateProject (IVCProject ivcProject){
		ConnectionManager connectionManager = ConnectionManager.getInstance(ivcProject.getName());
		// 1. initiate connections
		try {
			Map<String, ClientIntf> peers = connectionManager.initiateConnections(ivcProject.getServerAddress(),ivcProject.getServerPath());
		} catch (IVCException e) {
			e.printStackTrace();
			return new Result(false, Exceptions.SERVER_CONNECTION_FAILED, e);
		}
		if (connectionManager.getServer() == null) {
			return new Result(false, Exceptions.SERVER_CONNECTION_FAILED, null);
		}
		// 2. append pending rcl transformations
		try {
			TransformationHistoryList pendingRCL = connectionManager.getServer().returnPendingRCL(ivcProject.getServerPath(), NetworkUtils.getHostAddress());
			TransformationHistoryList RCL = (TransformationHistoryList)FileUtils.readObjectFromFile(ivcProject.getProject().getLocation().toOSString() + Constants.IvcFolder +Constants.RemoteCommitedLog);
			RCL.appendTransformationHistoryList(pendingRCL);
			FileUtils.writeObjectToFile(ivcProject.getProject().getLocation().toOSString() + Constants.IvcFolder +Constants.RemoteCommitedLog, RCL);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 3. append pending rul transformations
		try {
			Map<String,TransformationHistoryList> pendingRULs = connectionManager.getServer().returnPendingRUL(ivcProject.getServerPath(), NetworkUtils.getHostAddress());
			Iterator<String> it = pendingRULs.keySet().iterator();
			while (it.hasNext()){
				String host = it.next();
				TransformationHistoryList pendingRUL = pendingRULs.get(host);
				TransformationHistoryList rul = new TransformationHistoryList();
				File rulfile = new File(ivcProject.getProject().getLocation().toOSString() + Constants.IvcFolder + Constants.RemoteUnCommitedLog + "_" + host.replaceAll(".", "_"));
				if (!rulfile.exists()){
					try {
						rulfile.createNewFile();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else{
					rul =  (TransformationHistoryList) FileUtils.readObjectFromFile(rulfile.getAbsolutePath());
				}
				rul.appendTransformationHistoryList(pendingRUL);
				FileUtils.writeObjectToFile(rulfile.getAbsolutePath(),rul);
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new Result(true, "Success", null);
	}

}
