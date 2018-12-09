package org.rexcellentgames.burningknight;

public class Version {
	public static final boolean debug = true;
	public static final boolean showAlphaWarning = !debug;
	public static final double major = 0.1;
	public static final double minor = 0.3;

	public static String asString() {
		return "v" + major + "." + minor + (debug ? " dev" : "");
	}

	public static final String string = asString();
}