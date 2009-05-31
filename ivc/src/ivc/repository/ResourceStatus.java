package ivc.repository;

import java.io.IOException;
import java.util.Date;

import ivc.repository.streams.StatusFromBytesStream;
import ivc.repository.streams.StatusToBytesStream;

/**
 * 
 * @author alexm
 * 
 *         Class used to manage the status of a resource
 */

public class ResourceStatus {
	private long lastChangedRevision;
	
	private Date lastChangedDate;
	private String lastCommitAuthor;
	private Status status;

	public ResourceStatus(long lastChangedRev,Date lastCDate,String lastCommitAtuh,Status status){
		this.lastChangedDate=lastCDate;
		this.setLastChangedRevision(lastChangedRev);
		this.lastCommitAuthor=lastCommitAtuh;
		this.status=status;
	}
	public ResourceStatus(byte bytes[]) {
		
		fromBytes(new StatusFromBytesStream(bytes));
	}

	/**
	 * Get the status encoded in bytes
	 * 
	 * @return byte[] with externalized status
	 */
	public byte[] getBytes() {
		StatusToBytesStream out = new StatusToBytesStream();
		getBytesInto(out);
		return out.toByteArray();
	}

	/**
	 * Writes the resource to an output stream
	 * 
	 * @param dos
	 */
	protected void getBytesInto(StatusToBytesStream dos) {
		try {
			// lastChangedRevision
			dos.writeLong(getLastChangedRevision());

			// lastChangedDate
			dos.writeLong(lastChangedDate.getTime());

			// lastCommitAuthor
			dos.writeString(lastCommitAuthor);

			// textStatus
			// dos.writeInt(textStatus);

			// propStatus
			// dos.writeInt(propStatus);

			// status
			dos.writeInt(status.ordinal());

		} catch (IOException e) {
			return;
		}
	}

	/**
	 * reads the resource from an output stream
	 * 
	 * @param dis
	 */
	protected void fromBytes(StatusFromBytesStream dis) {
		try {
			// last changed revision
			setLastChangedRevision(dis.readLong());

			// last changed date
			lastChangedDate = new Date(dis.readLong());

			// last commitAuthor
			lastCommitAuthor = dis.readString();

			status = Status.values()[dis.readInt()];
		} catch (IOException e) {
			return;
		}
	}
	public Status getStatus(){
		return status;
	}
	private void setLastChangedRevision(long lastChangedRevision) {
		this.lastChangedRevision = lastChangedRevision;
	}
	private long getLastChangedRevision() {
		return lastChangedRevision;
	}
	public Date getLastChangedDate() {
		return lastChangedDate;
	}
	public void setLastChangedDate(Date lastChangedDate) {
		this.lastChangedDate = lastChangedDate;
	}
	public String getLastCommitAuthor() {
		return lastCommitAuthor;
	}
	public void setLastCommitAuthor(String lastCommitAuthor) {
		this.lastCommitAuthor = lastCommitAuthor;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
}
