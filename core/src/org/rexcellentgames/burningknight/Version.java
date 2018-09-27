package org.rexcellentgames.burningknight;

public class Version {
	public static boolean debug = false;
	public static double major = 0.0;
	public static double minor = 19.1;

	public static String asString() {
		return "v" + major + "." + minor + (debug ? " dev" : "");
	}

	public static String string = asString();
}
