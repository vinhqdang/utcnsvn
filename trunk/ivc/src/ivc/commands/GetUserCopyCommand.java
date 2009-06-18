/**
 * 
 */
package ivc.commands;

import ivc.data.IVCProject;
import ivc.data.exception.Exceptions;
import ivc.data.operation.Operation;
import ivc.data.operation.OperationHistory;
import ivc.data.operation.OperationHistoryList;
import ivc.managers.ConnectionManager;
import ivc.server.rmi.ServerIntf;
import ivc.util.Constants;

import java.rmi.RemoteException;
import java.util.Iterator;

/**
 * @author danielan
 * 
 */
public class GetUserCopyCommand implements CommandIntf {

	private String hostAddress;
	private IVCProject ivcProject;
	private String filePath;

	private StringBuffer fileContent;

	@Override
	/*
	 * * Computes the current version of the remote file based on the operations stored in
	 * the log files of the current user workspace
	 */
	public Result execute(CommandArgs args) {
		hostAddress = (String) args.getArgumentValue(Constants.HOST_ADDRESS);
		ivcProject = (IVCProject) args.getArgumentValue(Constants.IVCPROJECT);
		filePath = (String) args.getArgumentValue(Constants.FILE_PATH);

		fileContent = new StringBuffer();
		ConnectionManager conMg = ConnectionManager.getInstance(ivcProject.getName());
		// get base version
		ServerIntf server = conMg.getServer();
		if (server != null) {
			try {
				fileContent = server.getBaseVersionForFile(ivcProject.getServerPath(),
						filePath);
			} catch (RemoteException e) {
				return new Result(true, Exceptions.COULD_NOT_GET_BASEVERSION_FORFILE, e);
			}
			int version = 1;
			OperationHistoryList rul = new OperationHistoryList();
			if (hostAddress.equalsIgnoreCase(Constants.COMMITED)) {
				version = Integer.MAX_VALUE;
			} else {
				rul = ivcProject.getRemoteUncommitedLog(hostAddress);
				if (rul != null && rul.getOperationHistForFile(filePath) != null) {
					version = rul.getOperationHistForFile(filePath).getOperations()
							.getLast().getFileVersion();
				}
			}
			// apply commited transformations
			try {
				OperationHistoryList cl = server.returnHeadVersion(ivcProject
						.getServerPath());
				OperationHistory fileOps = cl.getOperationHistForFile(filePath);
				if (fileOps != null) {
					for (Iterator<Operation> iterator = fileOps.getOperations()
							.descendingIterator(); iterator.hasNext();) {
						Operation operation = iterator.next();
						if (operation.getOperationType() == Operation.CHARACTER_ADD
								|| operation.getOperationType() == Operation.CHARACTER_DELETE) {
							if (operation.getFileVersion() <= version) {
								try {
									fileContent = operation
											.applyContentTransformation(fileContent);
								} catch (Exception e) {
								}
							}
						}
					}
				}
			} catch (RemoteException e1) {
				return new Result(true, Exceptions.COULD_NOT_GET_COMMITEDLOG, e1);
			}
			// apply uncommitted operations
			if (!hostAddress.equalsIgnoreCase(Constants.COMMITED)) {
				if (rul != null && rul.getOperationHistForFile(filePath) != null) {
					OperationHistory rulOps = rul.getOperationHistForFile(filePath);
					if (rulOps != null) {
						for (Iterator<Operation> iterator = rulOps.getOperations()
								.descendingIterator(); iterator.hasNext();) {
							Operation operation = iterator.next();
							if (operation.getOperationType() == Operation.CHARACTER_ADD
									|| operation.getOperationType() == Operation.CHARACTER_DELETE) {
								fileContent = operation
										.applyContentTransformation(fileContent);
							}
						}
					}
				}
			}
		}

		Result res = new Result(true, "Success", null);
		res.setResultData(fileContent);
		return res;
	}

}
