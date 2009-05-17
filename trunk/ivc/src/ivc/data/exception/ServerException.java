package ivc.data.exception;


public class ServerException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ServerException(String message) {
		super(message);
	}
	
	/**
	 * implements write error stacktrace to LogFile
	 */
	public void logError(){
		String stackStraceStr="";
		StackTraceElement[] els = getStackTrace();
		for (StackTraceElement stackTraceElement : els) {
			stackStraceStr += "\n"+stackTraceElement.toString();
		}
//		HTMLLogger.error(stackStraceStr);
	}

	
}

