package ivc.data.command;

import ivc.data.Result;

public interface CommandIntf {
	
	public Result execute(CommandArgs args);

}
