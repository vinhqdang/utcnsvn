package ivc.rmi;

import ivc.data.Result;
import ivc.data.Transformation;
import ivc.data.command.CommandArgs;
import ivc.data.command.ConnectToPeerCommand;
import ivc.util.ConnectionManager;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class ClientImpl extends UnicastRemoteObject implements ClientIntf {
	private int test = 0;

	/**
	 * default serial version
	 */
	private static final long serialVersionUID = 1L;

	public ClientImpl() throws RemoteException {
		super();
	}

	@Override
	public Result checkout(CommandArgs args) throws RemoteException {

		/*
		 * 
		 * // will hold a list of File objects for each // document from the
		 * repository List<File> project = new ArrayList<File>();
		 * 
		 * // will hold the list of all operations committed for a given
		 * document Vector operations = new Vector();
		 * 
		 * // for each client of the repository for(int i=0;
		 * i<this.clientsList.size(); i++) { IClient client =
		 * (IClient)this.clientsList.get(i); try {
		 * 
		 * if (client.getSite() == clientSite) { // we found the client
		 * performing the checkout
		 * 
		 * // get all the documents from the repository Vector documents =
		 * this.documentList.getDocuments();
		 * 
		 * // for each document from the repository for(Iterator
		 * it=documents.iterator(); it.hasNext();) { Document doc =
		 * (Document)it.next();
		 * 
		 * // if the document has not been deleted if (!doc.isDeleted()) {
		 * 
		 * // get the document name String documentName = doc.getDocumentName();
		 * 
		 * // get the document text String documentText = doc.getDocumentText();
		 * 
		 * // get all committed operations for this document operations =
		 * this.getOperations(documentName, 0, version);
		 * 
		 * if (operations != null) { // create a new ReducedDocumentModel object
		 * that will enclose all the necessary // information regarding the
		 * document(document name, text, operations committed // up to current
		 * version) ReducedDocumentModel dm = new
		 * ReducedDocumentModel(documentName,documentText,operations); if (dm ==
		 * null) System.out.println("null dm"); project.add(dm); } } }
		 * 
		 * // send the client the list of all the documents from the repository
		 * client.checkoutProject(project,this.projectName); } } catch
		 * (Exception e) { e.printStackTrace(); } }
		 */
		return null;
	}

	@Override
	public Result commit(CommandArgs args) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result update(CommandArgs args) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result sendTransformation(Transformation transformation)
			throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * @see ivc.rmi.ServerIntf#connectToPeer(java.lang.String)
	 */
	@Override
	public Result connectToPeer(String hostAddress) throws RemoteException {
		ConnectToPeerCommand command = new ConnectToPeerCommand();
		CommandArgs args = new CommandArgs();
		args.putArgument("hostAddress", hostAddress);
		return command.execute(args);
	}

	/*
	 * @see ivc.rmi.ServerIntf#getPeers()
	 */
	@Override
	public List<String> getPeers() throws RemoteException {
		return ConnectionManager.getInstance().getPeerHosts();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ivc.rmi.ClientIntf#test()
	 */
	@Override
	public void test(String who,String path) throws RemoteException {
		File file = new File(path);
		try {
			System.out.println("Creating file....");
			file.createNewFile();
			System.out.println("Created file");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
