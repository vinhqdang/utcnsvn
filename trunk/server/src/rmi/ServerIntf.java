package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

import data.Result;
import command.CommandArgs;

public interface ServerIntf extends Remote {
	
	public Result checkout(CommandArgs args)throws RemoteException;
	
	public Result update(CommandArgs args) throws RemoteException;
	
	public Result commit(CommandArgs args) throws RemoteException;
	

}
