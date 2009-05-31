/**
 * 
 */
package ivc.data.commands;

import ivc.connection.ConnectionManager;
import ivc.data.IVCProject;
import ivc.data.Peer;
import ivc.data.Transformation;
import ivc.data.TransformationHistoryList;
import ivc.manager.ProjectsManager;
import ivc.rmi.client.ClientIntf;
import ivc.util.Constants;
import ivc.util.FileUtils;
import ivc.util.NetworkUtils;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IProject;

/**
 * @author danielan
 * 
 */
public class HandleTransformationCommand implements CommandIntf {

	private Transformation transformation;
	private IVCProject ivcProject;

	/*
	 * (non-Javadoc)
	 * 
	 * @see ivc.data.command.CommandIntf#execute(ivc.data.command.CommandArgs)
	 */
	@Override
	public Result execute(CommandArgs args) {
		// init local variables
		transformation = (Transformation) args.getArgumentValue(Constants.TRANSFORMATION);
		IProject project = (IProject) args.getArgumentValue(Constants.IPROJECT);
		ivcProject = ProjectsManager.instance().getIVCProjectByName(project.getName());

		// 1.add transformation to ll
		updateLL();
		// 2.add transformation to rul of others
		updateRUL();

		return new Result(true, "Success", null);
	}

	private void updateLL() {
		String llPath = ivcProject.getProject().getLocation().toOSString() + Constants.IvcFolder + Constants.LocalLog;
		TransformationHistoryList oldLL = (TransformationHistoryList) FileUtils.readObjectFromFile(llPath);
		if (oldLL == null) {
			oldLL = new TransformationHistoryList();
		}
		TransformationHistoryList newLL = oldLL.appendTransformation(transformation);
		FileUtils.writeObjectToFile(llPath, newLL);
	}

	private void updateRUL() {
		ConnectionManager connMan = ConnectionManager.getInstance(ivcProject.getName());
		TransformationHistoryList thl = new TransformationHistoryList();
		thl.appendTransformation(transformation);
		List<String> disconnected = new ArrayList<String>();
		try {
			List<Peer> allHosts = connMan.getServer().getAllClientHosts(ivcProject.getServerPath());
			Iterator<Peer> it = allHosts.iterator();
			while (it.hasNext()) {
				Peer peer = it.next();
				if (peer.getConnectionStatus().equalsIgnoreCase(Constants.CONNECTED)) {
					ClientIntf clientIntf = connMan.getPeerByAddress(peer.getHostAddress());
					clientIntf.updateRUL(ivcProject.getServerPath(), NetworkUtils.getHostAddress(), thl);
				} else {
					disconnected.add(peer.getHostAddress());
				}
			}
			connMan.getServer().updatePendingRUL(ivcProject.getServerPath(), NetworkUtils.getHostAddress(), disconnected, thl);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
