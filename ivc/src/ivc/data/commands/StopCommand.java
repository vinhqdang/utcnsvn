/**
 * 
 */
package ivc.data.commands;

/**
 * @author danielan
 *
 */
public class StopCommand implements CommandIntf {

	/* (non-Javadoc)
	 * @see ivc.data.commands.CommandIntf#execute(ivc.data.commands.CommandArgs)
	 */
	@Override
	public Result execute(CommandArgs args) {
		// TODO Auto-generated method stub
		// 1. remove host from clients hosts
		// 2. notify all peers to remove host
		
		return null;
	}

}
