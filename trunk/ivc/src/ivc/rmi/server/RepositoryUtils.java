/**
 * 
 */
package ivc.rmi.server;

import ivc.util.Constants;

import java.io.File;
import java.io.IOException;

/**
 * @author danielan
 *
 */
public class RepositoryUtils {
	
	public static void initRepository(String projectPath)  {
		if (!checkProjectPath(projectPath)) {
			File projPath = new File(Constants.RepositoryFolder + projectPath);
			projPath.mkdir();
		}
		File bvFile = new File(Constants.RepositoryFolder + projectPath + Constants.BaseVersionFile);
		try {
			bvFile.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		File cvFile = new File(Constants.RepositoryFolder + projectPath + Constants.CurrentVersionFile);
		try {
			cvFile.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		File rclFile = new File(Constants.RepositoryFolder + projectPath + Constants.PendingRemoteCommitedLog);
		try {
			rclFile.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		File transFile = new File(Constants.RepositoryFolder + projectPath + Constants.CommitedLog);
		try {
			transFile.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// BaseVersion bv =
		// (BaseVersion)FileHandler.readObjectFromFile(ServerBusiness.PROJECTPATH
		// + "\\.ivc\\.bv");
		// createFolderStructure(bv.getFolders());
		// createFileStructure(bv.getFiles());
	}

	public static boolean checkProjectPath(String projectPath){
		File file = new File(Constants.RepositoryFolder + projectPath);
		return file.exists();
	}

}
