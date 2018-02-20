package org.rexellentgames.dungeon.util;

public class MathUtils {
	public static int clamp(int min, int max, int val) {
		return Math.max(min, Math.min(max, val));
	}
}