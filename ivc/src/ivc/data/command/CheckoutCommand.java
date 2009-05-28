package ivc.data.command;

import ivc.connection.ConnectionManager;
import ivc.data.BaseVersion;
import ivc.data.Transformation;
import ivc.data.TransformationHistory;
import ivc.data.TransformationHistoryList;
import ivc.data.exception.IVCException;
import ivc.rmi.client.ClientIntf;
import ivc.rmi.server.ServerIntf;
import ivc.util.Constants;
import ivc.util.FileUtils;
import ivc.util.NetworkUtils;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

/**
 * @author danielan
 * 
 */
public class CheckoutCommand implements IRunnableWithProgress {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String serverAddress;
	private String projectPath;
	private String user;
	private String pass;
	
	
	private ServerIntf server;
	
	
	private CommandArgs args;
	private Result result;

	public CheckoutCommand(CommandArgs args) {
		this.args = args;
	}

	@Override
	public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
		// TODO Auto-generated method stub
		// init fields
		monitor.beginTask("Init local properties", 5);
		serverAddress = (String) args.getArgumentValue("serverAddress");
		projectPath = (String) args.getArgumentValue("projectPath");
		user = (String)args.getArgumentValue("user");
		pass = (String) args.getArgumentValue("password");
		
		monitor.worked(1);
		monitor.setTaskName("Establish connections");
		// 1.establish connections: connect to server; expose intf; connect to
		// other peers
		try {
			initiateConnections();
		} catch (IVCException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = new Result(false, "error", e);
			return;
		}
		
		monitor.worked(1);
		monitor.setTaskName("Establish connections");
		// 2. create local project 
		//TODO ALex create proj at checkout

		// 3. init workspace file
		createLogFiles();

		// 4. get base version and transformations
		createProjectFiles();

		// 5. create log files on peers
		createPeersRemoteFiles();
		result = new Result(true, "Success", null);
	}

	private void initiateConnections() throws IVCException {
		ConnectionManager connMan = ConnectionManager.getInstance();
		// connect to server
		try {
			connMan.connectToServer(serverAddress);
		} catch (IVCException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// expose interface
		connMan.exposeInterface();

		// get list of hosts
		List<String> peerHosts;
		try {
			peerHosts = connMan.getServer().getConnectedClientHosts();
			if (peerHosts != null) {
				for (Iterator<String> iterator = peerHosts.iterator(); iterator.hasNext();) {
					String peerHost = (String) iterator.next();
					try {
						connMan.connectToInterface(peerHost);
					} catch (IVCException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	private void createLogFiles() {
		try {
			// create document directory
			File ivcfolder = new File(projectPath + Constants.IvcFolder);
			ivcfolder.mkdir();
			// local log file
			File llfile = new File(projectPath +Constants.IvcFolder + Constants.LocalLog);
			llfile.createNewFile();
			// remote committed log
			File rclfile = new File(projectPath +Constants.IvcFolder + Constants.RemoteCommitedLog);
			rclfile.createNewFile();
			List<String> peerHosts = ConnectionManager.getInstance().getPeerHosts();
			if (peerHosts != null) {
				Iterator<String> it = peerHosts.iterator();
				while (it.hasNext()) {
					String peerHost = it.next();
					File rlufile = new File(projectPath + Constants.RemoteUnCommitedLog + "_"
							+ peerHost);
					rlufile.createNewFile();
				}
			}
			File cvFile = new File(projectPath +Constants.IvcFolder + Constants.CurrentVersionFile);
			cvFile.createNewFile();	
			ConnectionManager.getInstance().getServer().getVersionNumber(projectPath);			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	private void createPeersRemoteFiles() {
		List<ClientIntf> peers = ConnectionManager.getInstance().getPeers();
		if (peers != null) {
			Iterator<ClientIntf> it = peers.iterator();
			while (it.hasNext()) {
				ClientIntf peer = it.next();
				try {
					peer.createRLUFile(NetworkUtils.getHostAddress());
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 
	 */
	private void createProjectFiles() {
		ServerIntf server = ConnectionManager.getInstance().getServer();
		try {
			BaseVersion bv = server.returnBaseVersion(projectPath);
			// create folder structure
			Iterator<String> itfld = bv.getFolders().iterator();
			while (itfld.hasNext()) {
				File f = new File(itfld.next());
				f.mkdirs();
			}
			TransformationHistoryList thl = server.returnHeadVersion(projectPath);
			// 4. apply all transformations
			Iterator<String> it = bv.getFiles().keySet().iterator();
			while (it.hasNext()) {
				String filePath = it.next();
				StringBuffer baseContent = bv.getFiles().get(filePath);
				TransformationHistory th = getTransformationHistForFile(thl, filePath);
				for (Iterator<Transformation> iterator = th.getTransformations().iterator(); iterator.hasNext();) {
					Transformation transformation = iterator.next();
					StringBuffer headContent = transformation.applyContentTransformation(baseContent);
					// 5. create file structure
					try {
						File f = new File(filePath);
						f.createNewFile();
						FileUtils.writeStringBufferToFile(filePath, headContent);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	private TransformationHistory getTransformationHistForFile(TransformationHistoryList thl,String filePath) {
		if (thl != null) {
			Iterator<TransformationHistory> it = thl.iterator();
			while (it.hasNext()) {
				TransformationHistory th = it.next();
				if (filePath.equalsIgnoreCase(th.getFilePath())) {
					return th;
				}

			}
		}
		return new TransformationHistory();
	}


	public Result getResult() {
		return result;
	}

}
