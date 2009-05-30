package ivc.repository.streams;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public final class StatusToBytesStream extends ByteArrayOutputStream {
	protected StatusToBytesStream() {
		// Set the default size which should fit for most cases
		super(256);
	}

	/**
	 * Overrides the standard {@link ByteArrayOutputStream#write(int)}. This is
	 * one-purpose specialized stream without need for synchronization. The
	 * method does not check for available capacity, the
	 * {@link #ensureCapacity(int)} has to be explicitely called prior
	 */
	public final void write(int b) {
		buf[count] = (byte) b;
		count++;
	}

	/**
	 * Ensure the stream is able to store next n bytes. Grow the array if
	 * necessary.
	 * 
	 * @param length
	 */
	private void ensureCapacity(int length) {
		int newcount = count + length;
		if (newcount > buf.length) {
			byte newbuf[] = new byte[Math.max(buf.length + 100, newcount)];
			System.arraycopy(buf, 0, newbuf, 0, count);
			buf = newbuf;
		}
	}

	/**
	 * Overrides the standard {@link ByteArrayOutputStream#toByteArray()}. This
	 * is one-purpose stream so we don't have to return the copy of the buffer,
	 * so we return the ByteArrays' buffer itself directly.
	 */
	public final byte[] toByteArray() {
		return buf;
	}

	public final void writeLong(long v) throws IOException {
		ensureCapacity(8);
		write((byte) (v >>> 56));
		write((byte) (v >>> 48));
		write((byte) (v >>> 40));
		write((byte) (v >>> 32));
		write((byte) (v >>> 24));
		write((byte) (v >>> 16));
		write((byte) (v >>> 8));
		write((byte) (v >>> 0));
	}

	public final void writeInt(int v) throws IOException {
		ensureCapacity(4);
		write((v >>> 24) & 0xFF);
		write((v >>> 16) & 0xFF);
		write((v >>> 8) & 0xFF);
		write((v >>> 0) & 0xFF);
	}

	public final void writeBoolean(boolean v) throws IOException {
		ensureCapacity(1);
		write(v ? 1 : 0);
	}

	public final void writeString(String v) throws IOException {
		int length = (v != null) ? v.length() : 0;
		writeInt(length);
		ensureCapacity(length * 2);
		for (int i = 0; i < length; i++) {
			char c = v.charAt(i);
			write((c >>> 8) & 0xFF);
			write((c >>> 0) & 0xFF);
		}
	}
}