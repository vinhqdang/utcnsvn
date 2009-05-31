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
	private int lastChangedRevision;

	private Date lastChangedDate;

	private Status status;

	public ResourceStatus(int lastChangedRev, Date lastCDate, Status status) {
		this.lastChangedDate = lastCDate;
		this.setLastChangedRevision(lastChangedRev);
		this.status = status;
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
			dos.writeInt(getLastChangedRevision());

			// lastChangedDate
			dos.writeLong(lastChangedDate.getTime());

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
			setLastChangedRevision(dis.readInt());

			// last changed date
			lastChangedDate = new Date(dis.readLong());

			status = Status.values()[dis.readInt()];
		} catch (IOException e) {
			return;
		}
	}

	public Status getStatus() {
		return status;
	}

	private void setLastChangedRevision(int lastChangedRevision) {
		this.lastChangedRevision = lastChangedRevision;
	}

	private int getLastChangedRevision() {
		return lastChangedRevision;
	}

	public Date getLastChangedDate() {
		return lastChangedDate;
	}

	public void setLastChangedDate(Date lastChangedDate) {
		this.lastChangedDate = lastChangedDate;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
}
