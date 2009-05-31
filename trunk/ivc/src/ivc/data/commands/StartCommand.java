/**
 * 
 */
package ivc.data.commands;

import ivc.connection.ConnectionManager;
import ivc.data.IVCProject;
import ivc.data.exception.Exceptions;
import ivc.data.exception.IVCException;
import ivc.manager.ProjectsManager;
import ivc.rmi.client.ClientIntf;
import ivc.util.Constants;

import java.util.Map;

/**
 * @author danielan
 * 
 */
public class StartCommand implements CommandIntf {

	private String projectName;

	private IVCProject ivcProject;
	private ConnectionManager connectionManager;

	/*
	 * (non-Javadoc)
	 * 
	 * @see ivc.data.commands.CommandIntf#execute(ivc.data.commands.CommandArgs)
	 */
	@Override
	public Result execute(CommandArgs args) {
		// TODO Auto-generated method stub

		projectName = (String) args.getArgumentValue(Constants.PROJECT_NAME);
		ivcProject = ProjectsManager.instance().getIVCProjectByName(projectName);
		connectionManager = ConnectionManager.getInstance(ivcProject.getName());

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
		// 4. apply pending rcl transformations

		// 5. if don't have rul for them ... create one

		// 6. copy newest rcl transformations

		return new Result(true, "Success", null);
	}

}
