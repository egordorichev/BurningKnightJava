package org.rexellentgames.dungeon.util;

public class CollisionHelper {
	public static boolean check(int px, int py, int x, int y, int w, int h) {
		return px >= x && py >= y && px <= x + w && py <= y + h;
	}
}