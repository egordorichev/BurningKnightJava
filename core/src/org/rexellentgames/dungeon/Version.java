package org.rexellentgames.dungeon;

public class Version {
	public static boolean debug = true;
	public static double major = 0.0;
	public static double minor = 12.0;

	public static String asString() {
		return "v" + major + "." + minor;
	}
}