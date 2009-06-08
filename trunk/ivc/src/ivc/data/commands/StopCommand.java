/**
 * 
 */
package ivc.data.commands;

import ivc.data.IVCProject;
import ivc.data.exception.Exceptions;
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

/**
 * @author danielan
 * 
 */
public class StopCommand implements CommandIntf {

	/*
	 * (non-Javadoc)
	 * 
	 * @see ivc.data.commands.CommandIntf#execute(ivc.data.commands.CommandArgs)
	 */
	@Override
	public Result execute(CommandArgs args) {
		
		// 2. handle initiation for each project
		HashMap<String, IVCProject> projects = ProjectsManager.instance().getProjects();
		if (projects != null) {
			Iterator<String> it = projects.keySet().iterator();
			while (it.hasNext()) {
				String projectName = it.next();
				IVCProject ivcProject = projects.get(projectName);
				Result r = handleDisconnectProject(ivcProject);
				if (!r.isSuccess()) {
					return r;
				}
			}
		}
		return new Result(true, "Success", null);
	}

	private Result handleDisconnectProject(IVCProject ivcProject) {
		ConnectionManager connectionManager = ConnectionManager.getInstance(ivcProject.getName());
		// 1. remove host from clients hosts
		List<ClientIntf> hosts = connectionManager.getPeers();
		if (hosts != null) {
			Iterator<ClientIntf> it = hosts.iterator();
			while (it.hasNext()) {
				ClientIntf host = it.next();
				try {
					host.handleNewPeerDisconnected(ivcProject.getServerPath(), NetworkUtils.getHostAddress());
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return new Result(true, Exceptions.COULD_NOT_DISCONNECT_FROM_HOST, e);
				}
			}
		}

		// 2. disconnect from server
		connectionManager.disconnectFromServer();
		return new Result(true, "Success", null);
	}

}
