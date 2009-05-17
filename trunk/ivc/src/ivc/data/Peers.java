/**
 * 
 */
package ivc.data;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author danielan
 *
 */
public class Peers implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<String> peers;
	private String filePath;
	
	public Peers(){
		peers =  new ArrayList<String>();
		filePath = "ws_peers.txt";
	}
	
	public void addPeerHost(String hostName){
		peers.add(hostName);
		writePeers(peers);
	}
	
	public void setPeers(List<String> peers){
		this.peers = peers;
		writePeers(peers);
	}
	
	private void writePeers(List<String> peers){
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(filePath);
		} catch (FileNotFoundException e) {
			java.io.File f=  new java.io.File(filePath);
			try {
				f.createNewFile();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				fos =  new FileOutputStream(filePath);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
			ObjectOutputStream oos;
			try {
				fos = new FileOutputStream(filePath);				
				oos = new ObjectOutputStream(fos);
				oos.writeObject(peers);
				oos.close();
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}
	
	private List<String> readPeers(){
		FileInputStream fis;
		try {
			fis = new FileInputStream(filePath);
			try {
				ObjectInputStream ois =  new ObjectInputStream(fis);
				try {
					return peers = (List<String>)ois.readObject();					
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ArrayList<String>();
	}
	
	public List<String> getPeers(){
		peers = readPeers();
		return peers;
	}

}
