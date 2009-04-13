package command;

import data.Result;

public interface CommandIntf {
	
	public Result execute(CommandArgs args);

}
