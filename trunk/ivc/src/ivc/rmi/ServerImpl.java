package ivc.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import ivc.data.Result;
import ivc.data.Transformation;
import ivc.command.CommandArgs;

public class ServerImpl extends UnicastRemoteObject implements ServerIntf {

	/**
	 * default serial version
	 */
	private static final long serialVersionUID = 1L;

	public ServerImpl() throws RemoteException {
		super();
	}

	@Override
	public Result checkout(CommandArgs args) throws RemoteException {
		// TODO Auto-generated method stub
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

}
