package org.rexellentgames.dungeon.util;

public class CollisionHelper {
	public static boolean check(int px, int py, int x, int y, int w, int h) {
		return px >= x && py >= y && px <= x + w && py <= y + h;
	}
	
	public static boolean check(float x, float y, float w, float h, float x1, float y1, float w1, float h1) {
		return (x < x1 + w1 &&
			x + w > x1 &&
			y < y1 + h1 &&
			y + h > y1);
	}
}