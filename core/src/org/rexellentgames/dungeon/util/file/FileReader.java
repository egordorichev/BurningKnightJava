package org.rexellentgames.dungeon.util.file;

import java.io.*;

public class FileReader {
	private DataInputStream stream;

	public FileReader(String path) throws FileNotFoundException {
		this.stream = new DataInputStream(new BufferedInputStream(new FileInputStream(path), 32768));
	}

	public byte readByte() throws IOException {
		return this.stream.readByte();
	}

	public boolean readBoolean() throws IOException {
		return this.stream.readBoolean();
	}

	public short readInt16() throws IOException {
		return this.stream.readShort();
	}

	public int readInt32() throws IOException {
		return this.stream.readInt();
	}

	public String readString() throws IOException {
		byte length = this.stream.readByte();

		if (length == 0) {
			return null;
		}

		StringBuilder result = new StringBuilder();

		for (int i = 0; i < length; i++) {
			result.append(this.stream.readChar());
		}

		return result.toString();
	}

	public double readDouble() throws IOException {
		return this.stream.readDouble();
	}

	public void close() throws IOException {
		this.stream.close();
	}
}