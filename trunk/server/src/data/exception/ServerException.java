package data.exception;

public class ServerException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ServerException(String message) {
		super(message);
	}
	
	public void logError(String logLevel){
		
	}

	
}

