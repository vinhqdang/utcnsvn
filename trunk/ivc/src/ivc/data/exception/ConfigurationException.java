package ivc.data.exception;

public class ConfigurationException extends IVCException {
	
	
	/**
	 * @param propertyName
	 */
	public ConfigurationException(String propertyName){		
		super("You must set the property "+ propertyName +"in configuration file.");	
	}
	
	

}
