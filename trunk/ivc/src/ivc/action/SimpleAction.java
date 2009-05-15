package ivc.action;

import java.io.Serializable;


public abstract class SimpleAction implements Serializable {
	
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public boolean doAction(ActionArgs a){
		return true;
	}

}
