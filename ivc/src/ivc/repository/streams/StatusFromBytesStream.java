package ivc.repository.streams;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * Performance optimized ByteArrayInputStream for storing status data.
	 * This is one-purpose specialized stream without need for synchronization
	 * or generic bounds checking
 */
public final class StatusFromBytesStream extends ByteArrayInputStream
{
	private DataInputStream dis;
	
	protected StatusFromBytesStream(byte buf[])    	
	{
		super(buf);
		this.dis = new DataInputStream(this);
	}

	/**
	 * Overrides the standatd {@link ByteArrayInputStream#read()}
	 * This is one-purpose specialized stream without need for synchronization.
	 */
    public final int read() {
    	return (pos < count) ? (buf[pos++] & 0xff) : -1;
    }

	/**
	 * Overrides the standatd {@link ByteArrayInputStream#read(byte[], int, int)}
	 * This is one-purpose specialized stream without need for synchronization.
	 */
    public final int read(byte b[], int off, int len) {
    	if (pos >= count) {
    	    return -1;
    	}
    	if (pos + len > count) {
    	    len = count - pos;
    	}
    	if (len <= 0) {
    	    return 0;
    	}
    	System.arraycopy(buf, pos, b, off, len);
    	pos += len;
    	return len;
    }

    public final long readLong() throws IOException {
    	return this.dis.readLong();
    }

    public final int readInt() throws IOException {
    	return this.dis.readInt();
    }

    public final boolean readBoolean() throws IOException {
    	return this.dis.readBoolean();
    }

    public final String readString() throws IOException {
    	int length = this.dis.readInt();
    	if (length == 0) {
    		return null;
    	}
    	char[] chars = new char[length];
    	for (int i = 0; i < length; i++) {
			chars[i] = this.dis.readChar();
		}
    	return new String(chars);
    }

    public final String readUTF() throws IOException {
    	return this.dis.readUTF();
    }

}