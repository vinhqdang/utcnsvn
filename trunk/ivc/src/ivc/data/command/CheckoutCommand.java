package ivc.data.command;


import java.rmi.RemoteException;
import java.util.Iterator;
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

import ivc.data.Result;
import ivc.data.exception.ServerException;
import ivc.rmi.ServerIntf;
import ivc.util.ConnectionManager;

/**
 * @author danielan
 *
 */
public class CheckoutCommand implements CommandIntf {

	private String hostAddress;
	private ServerIntf remotePeer;
	
	/* (non-Javadoc)
	 * @see command.CommandIntf#execute(command.CommandArgs)
	 */
	@Override
	public Result execute(CommandArgs args) {
		
		// establish connections 
		hostAddress = (String)args.getArgumentValue("hostAddress");
		
		// acquire files 
		try {
			remotePeer.checkout(args);
			// build local workspace
			initLogFiles();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
			
        
		return null;
	}
	
	/**
	 * 
	 */
	private void initLogFiles(){
	//TODO initLogFiles	
	}
	
	/**
	 *  Establish connections with all participants 
	 */
	private void initConnections(){
		ConnectionManager connManager = ConnectionManager.getInstance();
		try {
			// expose current interface
			ConnectionManager.getInstance().exposeInterface();
			//connect to the others
			ConnectToPeerCommand connCommand = new ConnectToPeerCommand();
			CommandArgs connArgs = new CommandArgs();
			connArgs.putArgument("hostAddress", hostAddress);
			connCommand.execute(connArgs);		
			remotePeer = ConnectionManager.getInstance().getPeerByAddress(hostAddress);		
		} catch (ServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void createProject(){
		//CREATE PROJECT
		/*
		String projectName = (String) args.getArgumentValue("projectName");
		Vector project;
		
		// get local workspace
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		
		// get the project root
        IWorkspaceRoot root = workspace.getRoot();
        
        // get a handle to project with name 'projectName'
        IProject newProject  = root.getProject(projectName);
        
        // get a reference to the Java project 'newProject' 
        IJavaProject javaProject = JavaCore.create(newProject);
        
        if (newProject.exists()) {
        	try {
        		// get all package fragment roots of 'javaProject'
        		// a package fragment root contains a set of package fragments; it corresponds 
        		// to an underlying resource which is either a folder, JAR, or zip. We are looking for a folder.
        		IPackageFragmentRoot[] packageRoot = javaProject.getAllPackageFragmentRoots();
        		
        		// there must be only one project in the workspace; we get a reference to it
		        IPackageFragmentRoot fr = packageRoot[0];
		        
		        // for each document received from the repository 
		        for(Iterator it = project.iterator(); it.hasNext();) {
					
		        	// documents and associated information are passed as ReducedDocumentModel objects
					ReducedDocumentModel dm = (ReducedDocumentModel)it.next();
					// get document name
					String documentName = dm.getDocumentName();
					// get document text
					String documentText = dm.getDocumentText();
					// get operations performed remotely on the document
					Vector operations = dm.getOperations();
					// create a new document model for the document
					DocumentModel doc = new DocumentModel();
					// apply remote operations on document in order to obtain
					// the current version of the document
					documentText = doc.applyOperations(documentText, operations);
					
					// path of the document inside the project is contained in 
					// the document name; we retrieve from the document name the 
					// name of the document and the name of the folder inside which the 
					// document might be located
					int p1 = documentName.indexOf(".");
					int p2 = documentName.lastIndexOf(".");
					int p3 = documentName.lastIndexOf(".", p2-1);
					int p4 = documentName.indexOf(".",p1+1);
					
					// get the actual document name
					String file = documentName.substring(p3+1);
					
					// contains the name of the folder inside which the document is located
					String pack;
					if (p3 == p4)
						pack = documentName.substring(p1+1,p3);
					else
						pack = documentName.substring(p4+1,p3);
					
					// perform some string transformation
					String temp = pack.replace(".", "/");
					temp = "/"+projectName+"/"+temp;
					
					IPath path = new Path(temp);
					
					// retrieve the folder(the underlying resource of a folder is a package fragment) 
					// situated on path 'path' inside the project
					// this folder represents the folder inside which our document is located 
					IPackageFragment pf = javaProject.findPackageFragment(path);
					
					// if the folder does not already exist, create it
					if (pf == null) 
						pf = fr.createPackageFragment(pack, false, null);
					
					ICompilationUnit cu;
					// if folder already existed/was successfully created, create compilation unit inside folder
					// in our case a compilation unit represents a java file
					if (pf != null) {
						// create a new java file with name 'file' and contents 'documentText'
						cu = pf.createCompilationUnit(file, documentText, true, null);
						// get the java resource underlying the newly created compilation unit
						IResource res = cu.getCorrespondingResource();
						if (res instanceof IFile) {
							// attach document listener to the newly created file
							AttachListeners.attachFileListener((IFile)res);
						}
					}
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			*/
	}

	
}
