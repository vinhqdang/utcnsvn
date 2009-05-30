package ivc.data.command;

import ivc.connection.ConnectionManager;
import ivc.data.exception.IVCException;

import ivc.rmi.server.ServerIntf;

import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

public class FindHostProject implements IRunnableWithProgress {
	String sAddress;
	String pPath;
	Result result;

	public FindHostProject(String serverAddress, String path) {
		sAddress = serverAddress;
		pPath = path;
	}

	@Override
	public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
		monitor.beginTask("Initiating connection", 2);
		ServerIntf server;
		try {
			server = ConnectionManager.getInstance().connectToServer(sAddress);

			monitor.worked(1);
			monitor.setTaskName("Getting project information");
			server.checkProjectPath(pPath);
		} catch (RemoteException e) {
			result = new Result(false, e.getMessage(), e);
		} catch (IVCException e) {
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
