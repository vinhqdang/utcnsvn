package ivc.commands;


public interface CommandIntf {
	
	/**
	 *  Execute methods that defines commands behavior 
	 * @param args
	 * @return
	 */
	public Result execute(CommandArgs args);

}
