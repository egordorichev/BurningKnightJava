package org.rexellentgames.dungeon.util;

public class MathUtils {
	public static float clamp(float min, float max, float val) {
		return Math.max(min, Math.min(max, val));
	}
}