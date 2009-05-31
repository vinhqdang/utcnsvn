package ivc.data.exception;

public class Exceptions {
	
	
	public static final String REGISTRY_ALREADY_BOUNDED = "Registry server is already bounded";
	public static final String UNABLE_TO_READ_HOST = "Host address cannot be read";
	public static final String INVALID_URL = "URL is invalid";
	public static final String UNABLE_TO_INITIATE_CONNECTION = "Error initiating server connection";
	public static final String SERVER_UNBOUND = "Server is not bound";
	
	// share project command
	public static final String SERVER_CONNECTION_FAILED = "Connection to server could not be established";
	public static final String SERVER_AUTHENTICATION_FAILED ="SERVER_AUTHENTICATION_FAILED";	
	public static final String SERVER_PATH_INVALID = "SERVER_PATH_INVALID";
	
	// checkout project command
	public static final String COULD_NOT_CREATE_PROJECT = "COULD_NOT_CREATE_PROJECT";
	public static final String SERVER_PROJ_PATH_INVALID = "Specified project location is invalid ";
	
	// commit command
	public static final String FILE_OUT_OF_SYNC = "FILE_OUT_OF_SYNC";
	public static final String SERVER_UPDATE_HEADVERSION_FAILED = "SERVER_UPDATE_HEADVERSION_FAILED";
	public static final String COMMIT_NOFILE_CHANGED = "COMMIT_NOFILE_CHANGED";
 
}
