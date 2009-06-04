/**
 * 
 */
package ivc.util;

import java.io.File;

/**
 * @author danielan
 *
 */
public class Constants {
	
	/*************** FILES **********************************************/
	
	// client files
	public static final String LocalLog = "\\.ll";
	public static final String RemoteCommitedLog = "\\.rcl";
	public static final String RemoteUnCommitedLog = "\\.rul";
	public static final String ServerFile = "\\.svr";
	public static final String IvcFolder = "\\.ivc";
	
	// server files
	public static final String BaseVersionFile = "\\.bv";
	public static final String Peers ="\\.peers";
	public static final String CommitedLog = "\\.cl";
	public static final String PendingRemoteCommitedLog = "\\.prcl";
	public static final String PendingRemoteUncommitedLog = "\\.prul";
	public static final String RepositoryFolder = "d:\\temp\\ivc\\projects";
	
	// common files 	
	public static final String CurrentVersionFile = "\\.cv";
	
	
	
	
	/*************** CONNECTION ******************************************/
	public static final String CONNECTED = "connected";
	public static final String DISCONNECTED = "disconnected";
	
	public static final String BIND_CLIENT = "client_ivc";
	public static final String BIND_SERVER = "server_ivc";
	
	/*************** COMMANDS *******************************************/
	public static final String PROJECT_NAME = "projectName";
	public static final String PROJECT_PATH = "projectPath";
	public static final String SERVER_ADDRESS = "serverAddress";
	public static final String USERNAME = "userName";
	public static final String PASSWORD = "pass";
	public static final String IPROJECT = "project";
	public static final String IVCPROJECT = "ivcproject";
	public static final String FILE_PATHS = "filePaths";
	public static final String OPERATION = "operation";
	public static final String OPERATION_HIST = "operation_hist";
	public static final String OPERATION_HIST_LIST1 = "operation_hist1";
	public static final String OPERATION_HIST_LIST2 = "operation_hist2";
	
	
	
	
	
	
}
