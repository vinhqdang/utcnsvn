package ivc.commands;

import ivc.data.exception.Exceptions;
import ivc.managers.ConnectionManager;
import ivc.server.rmi.ServerIntf;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

public class FindHostProjectCommand implements IRunnableWithProgress {
	String sAddress;
	String pPath;
	Result result;

	public FindHostProjectCommand(String serverAddress, String path) {
		sAddress = serverAddress;
		pPath = path;
	}

	@Override
	/*
	 * * Validates parameters given at checkout project time by user through the wizard
	 * fields. The command checks connection with server and searches for the project the
	 * user tries to checkout
	 */
	public void run(IProgressMonitor monitor) throws InvocationTargetException,
			InterruptedException {
		monitor.beginTask("Initiating connection", 2);
		ServerIntf server;
		try {

			server = ConnectionManager.getServer(sAddress);

			monitor.worked(1);
			monitor.setTaskName("Getting project information");

			boolean isOK = server.checkProjectPath("\\" + pPath);
			if (!isOK) {
				result = new Result(false, Exceptions.SERVER_PROJ_PATH_INVALID, null);
			}
		} catch (Exception e) {
			result = new Result(false, e.getMessage(), e);
		}
		monitor.done();
		if (result == null) {
			result = new Result(true, "Success", null);
		}

	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

}
