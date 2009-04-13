package data;

import java.util.UUID;

public class User {
	
	private UUID userID;
	private String userName;
	
	public User() {
		super();
	}

	public UUID getUserID() {
		return userID;
	}

	public void setUserID(UUID userID) {
		this.userID = userID;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	

}
