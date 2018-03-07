package org.rexellentgames.dungeon.util;

public class Log {
	public static void error(String string) {
		System.out.println("\u001B[31m" + string + "\u001B[0m");
	}

	public static void info(String string) {
		System.out.println("\u001B[32m" + string + "\u001B[0m");
	}
}