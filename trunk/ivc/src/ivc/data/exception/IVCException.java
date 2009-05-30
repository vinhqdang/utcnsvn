package ivc.data.exception;

public class IVCException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public IVCException(String message) {
		super(message);
	}

	/**
	 * implements write error stacktrace to LogFile
	 */
	public void logError() {
		String stackStraceStr = "";
		StackTraceElement[] els = getStackTrace();
		for (StackTraceElement stackTraceElement : els) {
			stackStraceStr += "\n" + stackTraceElement.toString();
		}
		// HTMLLogger.error(stackStraceStr);
	}

	public IVCException(Exception e) {
		super(e);
	}

}
