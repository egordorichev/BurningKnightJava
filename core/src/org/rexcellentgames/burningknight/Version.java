package org.rexcellentgames.burningknight;

public class Version {
	public static final boolean debug = true;
	public static final boolean showAlphaWarning = false;
	public static final double major = 0.1;
	public static final double minor = 0.4;

	public static String asString() {
		return "v" + major + "." + minor + (debug ? " dev" : "");
	}

	public static final String string = asString();
}