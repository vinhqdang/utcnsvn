/**
 * 
 */
package ivc.commands;

import ivc.client.rmi.ClientIntf;
import ivc.data.IVCProject;
import ivc.data.Peer;
import ivc.data.operation.OperationHistory;
import ivc.data.operation.OperationHistoryList;
import ivc.managers.ConnectionManager;
import ivc.managers.ProjectsManager;
import ivc.util.Constants;
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
public class HandleOperationCommand implements CommandIntf {

	private OperationHistory operationHist;
	private IVCProject ivcProject;

	@Override
	/*
	 * * Writes a new created operation in all log files. A new created operation has to
	 * be added in local log file and remote uncommitted log files of all peers.
	 */
	public Result execute(CommandArgs args) {
		// init local variables
		operationHist = (OperationHistory) args
				.getArgumentValue(Constants.OPERATION_HIST);
		IProject project = (IProject) args.getArgumentValue(Constants.IPROJECT);
		ivcProject = ProjectsManager.instance().getIVCProjectByName(project.getName());

		// 1.add operation to ll
		updateLL();
		// 2.add operation to rul of others
		updateRUL();

		return new Result(true, "Success", null);
	}

	private void updateLL() {
		OperationHistoryList oldLL = ivcProject.getLocalLog();
		OperationHistoryList newLL = oldLL.appendOperationHistory(operationHist);
		ivcProject.setLocalLog(newLL);
	}

	private void updateRUL() {
		ConnectionManager connMan = ConnectionManager.getInstance(ivcProject.getName());
		OperationHistoryList thl = new OperationHistoryList();
		thl.appendOperationHistory(operationHist);
		List<String> disconnected = new ArrayList<String>();
		try {
			List<Peer> allHosts = connMan.getServer().getAllClientHosts(
					ivcProject.getServerPath());
			Iterator<Peer> it = allHosts.iterator();
			while (it.hasNext()) {
				Peer peer = it.next();
				if (peer.getConnectionStatus().equalsIgnoreCase(Constants.CONNECTED)
						&& connMan.getPeerByAddress(peer.getHostAddress()) != null) {
					ClientIntf clientIntf = connMan.getPeerByAddress(peer
							.getHostAddress());
					if (clientIntf != null) {
						clientIntf.updateRUL(ivcProject.getServerPath(), NetworkUtils
								.getHostAddress(), thl);
					}
				} else {
					if (!peer.getHostAddress().equalsIgnoreCase(
							NetworkUtils.getHostAddress())) {
						disconnected.add(peer.getHostAddress());
					}
				}
			}
			connMan.getServer().updatePendingRUL(ivcProject.getServerPath(),
					NetworkUtils.getHostAddress(), disconnected, thl);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

}
