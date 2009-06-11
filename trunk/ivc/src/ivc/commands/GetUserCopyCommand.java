/**
 * 
 */
package ivc.commands;

import ivc.data.IVCProject;
import ivc.util.Constants;

/**
 * @author danielan
 *
 */
public class GetUserCopyCommand implements CommandIntf {

	private String hostAddress;
	private IVCProject ivcProject;
	private String filePath;
	
	private String fileContent;
	/* (non-Javadoc)
	 * @see ivc.commands.CommandIntf#execute(ivc.commands.CommandArgs)
	 */
	@Override
	public Result execute(CommandArgs args) {
		// TODO Auto-generated method stub
		hostAddress = (String) args.getArgumentValue(Constants.HOST_ADDRESS);
		ivcProject = (IVCProject) args.getArgumentValue(Constants.IVCPROJECT);
		filePath = (String) args.getArgumentValue(Constants.FILE_PATH);
		
		Result res =  new Result(true, "Success",null);
		res.setResultData(fileContent);
		return res;
	}

}
