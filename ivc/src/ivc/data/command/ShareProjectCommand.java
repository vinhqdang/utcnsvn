/**
 * 
 */
package ivc.data.command;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import ivc.data.Result;
import ivc.data.exception.ServerException;
import ivc.util.ConnectionManager;

/**
 * @author danielan
 *
 */
public class ShareProjectCommand implements CommandIntf {

	/* (non-Javadoc)
	 * @see ivc.command.CommandIntf#execute(ivc.command.CommandArgs)
	 */
	@Override
	public Result execute(CommandArgs args) {
		// TODO Auto-generated method stub
		
		try {
			//expose interface
			ConnectionManager.getInstance().exposeInterface();
			
			// save base version for each file??
			
			// create workspace log files
			
			// update gui ??
		} catch (ServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*
    	File folder = null;
    	
    	// repository has not been created
    	if (!this.repositoryCreated) {
    	
    		// set committed project version to 0
    		this.version = 0;
    		// set project name to "test"
    		this.projectName = "test";
    		
    		// create a new file object for the repository 
    		File repositoryFolder = new File(repositoryPath);
    		try {
    			// delete all contents from the repository folder
				emptyRepository(repositoryFolder);
			} catch (IOException e1) { e1.printStackTrace(); }
    		repositoryFolder.mkdir();
    		
    		// create separate folder for each document
    		Set set = hm.entrySet();
    	    Iterator i = set.iterator();
    	    while(i.hasNext()) {
    	      Map.Entry me = (Map.Entry)i.next();
    	      
    	      // get folder name for each document
    	      String docPath = getDocumentName((String)me.getKey());
    	      String folderPath  = repositoryPath + docPath;
    	      String docContents = (String)me.getValue();
    	      
    	      // create document directory
    	      folder = new File(folderPath);
      		  folder.mkdir();
      		  
      		  // create initial version of the document
              File document = new File(folder, "0document.java");
              
              try {
            	  // write the document in the repository folder
            	  document.createNewFile();
		          FileWriter writer = new FileWriter(document);
		          writer.write(docContents);
		          writer.flush();
		          writer.close();
			  } catch (IOException e) { e.printStackTrace(); }
            	  
    	    }
    	    try {
    	    	// create a document list for the repository
				this.documentList = new DocumentList(new File(repositoryPath), 0);
			} catch (Exception e) { e.printStackTrace(); }
			
			String annotationPath = repositoryPath+annotationDir;
			File annDir = new File(annotationPath);
			annDir.mkdir();
			this.annotationLog = new Vector();
			
			// change flag to indicated that the repository was created
    		this.repositoryCreated = true;
    	}
    	*/
		return null;
	}


	
}
