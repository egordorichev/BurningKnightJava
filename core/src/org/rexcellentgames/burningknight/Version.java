package org.rexcellentgames.burningknight;

import org.rexcellentgames.burningknight.entity.level.save.GameSave;

public class Version {
	public static final boolean debug = true;
	public static final boolean showAlphaWarning = false;
	public static final double major = 0.1;
	public static final double minor = 1.5;

	{
		// REMOVE WHEN BETA COMES OUT
		// AND ADD BETA TAG
		GameSave.playedAlpha = true;
	}

	public static String asString() {
		return "v" + major + "." + minor + (debug ? " dev" : "");
	}

	public static final String string = asString();
}