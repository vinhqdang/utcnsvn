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
	public static final String PendingRemoteCommitedLog = "\\.rcl";
	public static final String RepositoryFolder = "d:\\temp\\ivc\\projects";
	
	// common files 	
	public static final String CurrentVersionFile = "\\.cv";
	
	
	
	
	/*************** CONNECTION ******************************************/
	public static final String CONNECTED = "connected";
	public static final String DISCONNECTED = "disconnected";
	
	public static final String BIND_CLIENT = "client_ivc";
	public static final String BIND_SERVER = "server_ivc";
	
	
	
	
}
