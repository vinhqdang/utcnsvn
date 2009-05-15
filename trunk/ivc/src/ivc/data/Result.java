package ivc.data;

import java.io.Serializable;

public class Result implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private boolean isSuccess;
	private String message;
	private Throwable error;
	private Object resultData;
	
	public Result(){
		super();
	}
	
	public Result(boolean isSuccess, String message, Throwable error) {
		this.isSuccess = isSuccess;
		this.message = message;
		this.error = error;
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Throwable getError() {
		return error;
	}

	public void setError(Throwable error) {
		this.error = error;
	}

	public Object getResultData() {
		return resultData;
	}

	public void setResultData(Object resultData) {
		this.resultData = resultData;
	}
	
	
	
	
	
	

}
