package org.rexellentgames.dungeon.entity.level;

public class Terrain {
	public static short EMPTY = 38;
	public static short DOOR = 5;
	public static short GRASS = 4;
	public static short FLOOR = 7;
	public static short ENTRANCE = 68;
	public static short EXIT = 69;
	public static short WALL = 33;
	public static short LOW_GRASS = 36;
	public static short WATER = 385;

	public static int[] flags = new int[512];

	public static int PASSABLE = 0x1;
	public static int SOLID = 0x2;
	public static int SECRET = 0x4;

	static {
		flags[EMPTY] = PASSABLE;
		flags[GRASS] = PASSABLE;
		flags[WALL] = SOLID;
		flags[DOOR] = PASSABLE;
		flags[FLOOR] = PASSABLE;
		flags[LOW_GRASS] = PASSABLE;
		flags[WATER] = PASSABLE;
		flags[EXIT] = PASSABLE;
		flags[ENTRANCE] = PASSABLE;

		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 11; y++) {
				flags[x + y * Level.WIDTH] = SOLID;
			}
		}
	}
}