package org.rexellentgames.dungeon.util;

import com.badlogic.gdx.Gdx;

import java.io.FileWriter;
import java.io.IOException;

public class Log {
	private static FileWriter file;

	public static void report(Throwable t) {
		try {
			file.write("Exception:");
			file.write(t.getMessage() + "\n" + t.getCause());
		} catch (IOException e) {
			e.printStackTrace();
		}

		t.printStackTrace();
	}

	public static void init() {
		try {
			file = new FileWriter(Gdx.files.external("bk.log").file());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void close() {
		try {
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void error(String string) {
		try {
			file.write("[ERROR]: " + string + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("\u001B[31m" + string + "\u001B[0m");
	}

	public static void info(String string) {
		try {
			file.write(string + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("\u001B[32m" + string + "\u001B[0m");
	}
}