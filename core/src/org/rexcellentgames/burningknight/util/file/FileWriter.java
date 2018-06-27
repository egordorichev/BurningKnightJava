package org.rexcellentgames.burningknight.util.file;


import java.io.*;

public class FileWriter {
	private DataOutputStream stream;

	public FileWriter(String path) throws IOException {
		File file = new File(path);

		if (!file.exists()) {
			// Make sure the path exists
			file.getParentFile().mkdirs();
			file.createNewFile();
		}

		this.stream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file), 32768));
	}

	public void writeByte(byte value) throws IOException {
		this.stream.writeByte(value);
	}

	public void writeBoolean(boolean value) throws IOException {
		this.stream.writeBoolean(value);
	}

	public void writeInt16(short value) throws IOException {
		this.stream.writeShort(value);
	}

	public void writeInt32(int value) throws IOException {
		this.stream.writeInt(value);
	}

	public void writeString(String string) throws IOException {
		if (string == null) {
			this.stream.writeByte((byte) 0);
		} else {
			// Max length is 255 chars
			// First byte is length of the string
			this.stream.writeByte(string.length());
			// Write the string
			this.stream.writeChars(string);
		}
	}

	public void writeDouble(double value) throws IOException {
		this.stream.writeDouble(value);
	}

	public void writeFloat(float value) throws IOException {
		this.stream.writeFloat(value);
	}
	public void close() throws IOException {
		this.stream.close();
	}
}