package org.rexcellentgames.burningknight;

public class Version {
	public static boolean debug = true;
	public static double major = 0.0;
	public static double minor = 15.6;

	public static String asString() {
		return "v" + major + "." + minor;
	}

	public static String string = asString();
}