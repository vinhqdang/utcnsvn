/**
 * 
 */
package ivc.commands;

import ivc.data.IVCProject;
import ivc.data.exception.Exceptions;
import ivc.data.exception.IVCException;
import ivc.data.operation.OperationHistory;
import ivc.data.operation.OperationHistoryList;
import ivc.managers.ConnectionManager;
import ivc.managers.ProjectsManager;
import ivc.rmi.client.ClientIntf;
import ivc.util.Constants;
import ivc.util.FileUtils;
import ivc.util.NetworkUtils;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author danielan
 * 
 */
public class StartCommand implements CommandIntf {



	/*
	 * (non-Javadoc)
	 * 
	 * @see ivc.commands.CommandIntf#execute(ivc.commands.CommandArgs)
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
			OperationHistoryList pendingRCL = connectionManager.getServer().returnPendingRCL(ivcProject.getServerPath(), NetworkUtils.getHostAddress());
			OperationHistoryList RCL = ivcProject.getRemoteCommitedLog();
			UpdateAnnotationsCommand command = new UpdateAnnotationsCommand();
			CommandArgs args = new CommandArgs();
			args.putArgument(Constants.IVCPROJECT, ivcProject);
			args.putArgument(Constants.HOST_ADDRESS, "commited");
			args.putArgument(Constants.ISCOMMIT, Boolean.TRUE);
			Iterator<OperationHistory> it = pendingRCL.iterator();
			while (it.hasNext()) {
				OperationHistory oh = it.next();
				args.putArgument(Constants.OPERATION_HIST, oh);
				command.execute(args);
			}			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 3. create missing rul files
		List<String> hosts = connectionManager.getPeerHosts();
		if (hosts != null){
			Iterator<String> it  = hosts.iterator();
			while(it.hasNext()){
				String host = it.next();
				File rulfile = new File(ivcProject.getProject().getLocation().toOSString() + Constants.IvcFolder + Constants.RemoteUnCommitedLog + "_" + host.replaceAll("\\.", "_"));
				if (!rulfile.exists()){
					try {
						rulfile.createNewFile();
						FileUtils.writeObjectToFile(rulfile.getAbsolutePath(), new OperationHistoryList());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}

		// 4. append pending rul transformations
		try {
			Map<String,OperationHistoryList> pendingRULs = connectionManager.getServer().returnPendingRUL(ivcProject.getServerPath(), NetworkUtils.getHostAddress());
			Iterator<String> it = pendingRULs.keySet().iterator();
			while (it.hasNext()){
				String host = it.next();
				OperationHistoryList pendingRUL = pendingRULs.get(host);
				OperationHistoryList rul = new OperationHistoryList();
				File rulfile = new File(ivcProject.getProject().getLocation().toOSString() + Constants.IvcFolder + Constants.RemoteUnCommitedLog + "_" + host.replaceAll("\\.", "_"));
				if (!rulfile.exists()){
					try {
						rulfile.createNewFile();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else{
					rul =  ivcProject.getRemoteUncommitedLog(host);
				}
				UpdateAnnotationsCommand command = new UpdateAnnotationsCommand();
				CommandArgs args = new CommandArgs();
				args.putArgument(Constants.IVCPROJECT, ivcProject);
				args.putArgument(Constants.HOST_ADDRESS, host);
				args.putArgument(Constants.ISCOMMIT, Boolean.FALSE);
				Iterator<OperationHistory> itt = rul.iterator();
				while (itt.hasNext()) {
					OperationHistory oh = itt.next();
					args.putArgument(Constants.OPERATION_HIST, oh);
					command.execute(args);
				}				
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new Result(true, "Success", null);
	}

}
