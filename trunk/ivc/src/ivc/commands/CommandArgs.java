package ivc.commands;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class CommandArgs implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Map<String,Object> args;
	
	public CommandArgs(){
		super();
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getArgumentValue(String key){
		return args.get(key);
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void putArgument(String key,Object value){
		if (args == null){
			args = new HashMap<String,Object>();
		}
		args.put(key, value);
	}

}
