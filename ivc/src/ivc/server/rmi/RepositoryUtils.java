/**
 * 
 */
package ivc.server.rmi;

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
			e.printStackTrace();
		}
		File cvFile = new File(Constants.RepositoryFolder + projectPath + Constants.CurrentVersionFile);
		try {
			cvFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		File rclFile = new File(Constants.RepositoryFolder + projectPath + Constants.PendingRemoteCommitedLog);
		try {
			rclFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		File transFile = new File(Constants.RepositoryFolder + projectPath + Constants.CommitedLog);
		try {
			transFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public static boolean checkProjectPath(String projectPath){
		File file = new File(Constants.RepositoryFolder + projectPath);
		return file.exists();
	}

}
