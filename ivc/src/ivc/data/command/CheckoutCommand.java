package ivc.data.command;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;

import ivc.data.BaseVersion;
import ivc.data.Result;
import ivc.data.Transformation;
import ivc.data.TransformationHistory;
import ivc.data.exception.ServerException;
import ivc.rmi.client.ClientIntf;
import ivc.rmi.server.ServerIntf;
import ivc.util.ConnectionManager;
import ivc.util.Constants;
import ivc.util.FileHandler;

/**
 * @author danielan
 * 
 */
public class CheckoutCommand implements CommandIntf, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String serverAddress;
	private ServerIntf server;
	private String projectPath;

	/*
	 * (non-Javadoc)
	 * 
	 * @see command.CommandIntf#execute(command.CommandArgs)
	 */
	@Override
	public Result execute(CommandArgs args) {
		// init fields
		serverAddress = (String) args.getArgumentValue("serverAddress");
		projectPath = (String)args.getArgumentValue("projectPath");
		

		// 1.establish connections
		initiateConnections();

		// 2. init workspace file
		initLogFiles();

		// 3. get base version and transformations
		createProjectFiles();

		// 6. create log files on peers
		createPeersRemoteFiles();
		
		return new Result(true, "Success", null);
	}

	/**
	 * 
	 */
	private void initiateConnections(){
		ConnectionManager connMan = ConnectionManager.getInstance();
		// connect to server
		connMan.connectToServer(serverAddress);

		// expose interface
		connMan.exposeInterface();

		// get list of hosts
		List<String> peerHosts;
		try {
			peerHosts = connMan.getServer().getClientHosts();
			if (peerHosts != null) {
				for (Iterator<String> iterator = peerHosts.iterator(); iterator	.hasNext();) {
					String peerHost = (String) iterator.next();
					try {
						connMan.connectToInterface(peerHost);
					} catch (ServerException e) {
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
	private void initLogFiles() {
		try {
			// create document directory
			File ivcfolder = new File(projectPath + Constants.IvcFolder);
			ivcfolder.mkdir();
			// local log file
			File llfile = new File(projectPath + Constants.LocalLog);
			llfile.createNewFile();
			// remote committed log
			File rclfile = new File(projectPath + Constants.RemoteCommitedLog);
			rclfile.createNewFile();
			List<String> peerHosts = ConnectionManager.getInstance().getPeerHosts();
			if (peerHosts != null){
				Iterator<String> it = peerHosts.iterator();
				while(it.hasNext()){
					String peerHost = it.next();
					File rlufile = new File(projectPath + Constants.RemoteUnCommitedLog+"_"+peerHost);
					rlufile.createNewFile();
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 */
	private void createPeersRemoteFiles(){
		List<ClientIntf> peers = ConnectionManager.getInstance().getPeers();
		if (peers != null){
			Iterator<ClientIntf> it = peers.iterator();
			while(it.hasNext()){
				ClientIntf peer = it.next();
				try {
					peer.createRLUFile(ConnectionManager.getInstance().getHostAddress());
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
	private void createProjectFiles(){
		ServerIntf server = ConnectionManager.getInstance().getServer();
		try {
			BaseVersion bv = server.getBaseVersion();
			// create folder structure
			Iterator<String> itfld = bv.getFolders().iterator();
			while (itfld.hasNext()) {
				File f = new File(itfld.next());
				f.mkdirs();
			}
			List<TransformationHistory> thl = server.getHeadVersion();
			// 4. apply all transformations
			Iterator<String> it = bv.getFiles().keySet().iterator();
			while (it.hasNext()) {
				String filePath = it.next();
				StringBuffer baseContent = bv.getFiles().get(filePath);
				TransformationHistory th = getTransformationHistForFile(thl,filePath);
				for (Iterator<Transformation> iterator = th.getTransformations().iterator(); iterator.hasNext();) {
					Transformation transformation = iterator.next();
					StringBuffer headContent = transformation
							.applyTransformation(baseContent);
					// 5. create files
					try {
						File f = new File(filePath);
						f.createNewFile();
						FileHandler.writeStringBufferToFile(filePath,
								headContent);
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
	private TransformationHistory getTransformationHistForFile(	List<TransformationHistory> thl, String filePath) {
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

}
