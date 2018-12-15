package org.rexcellentgames.burningknight.util;

import org.rexcellentgames.burningknight.Version;


public class Log {
	public static final boolean ENABLE_PHYSICS_MESSAGES = false;

	public static void report(Throwable t) {
		t.printStackTrace();
		force = true;
		close();
	}

	public static void printStackTrace() {
		for (StackTraceElement s : Thread.currentThread().getStackTrace()) {
			Log.error(s.toString());
		}
	}

	private static boolean force;

	public static void close() {

	}

	public static void error(Object string) {
		/*if (!Version.debug) {
			return;
		}*/

		try {
			System.out.println("\u001B[31m" + string + "\u001B[0m");
		} catch (Exception e) {

		}
	}

	public static void info(Object string) {
		try {
			System.out.println("\u001B[32m" + string + "\u001B[0m");
		} catch (Exception e) {

		}
	}

	public static void physics(Object string) {
		if (!ENABLE_PHYSICS_MESSAGES || !Version.debug) {
			return;
		}

		try {
			System.out.println("\u001B[34m" + string + "\u001B[0m");
		} catch (Exception e) {

		}
	}
}