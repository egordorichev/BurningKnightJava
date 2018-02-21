package org.rexellentgames.dungeon.entity.level;

public class Terrain {
	public static short EMPTY = 0;
	public static short DOOR = 5;
	public static short GRASS = 6;
	public static short FLOOR = 7;
	public static short ENTRANCE = 8;
	public static short EXIT = 9;
	public static short SECRET_DOOR = 10;
	public static short WALL = 33;
	public static short LOW_GRASS = 38;
	public static short FRONT_WALL = 65;
	public static short WATER = 70;

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
		flags[SECRET_DOOR] = SOLID | SECRET;
		flags[FRONT_WALL] = SOLID;

		for (int x = 0; x < 5; x++) {
			for (int y = 0; y < 12; y++) {
				flags[x + y * Level.WIDTH] = SOLID;
			}
		}
	}
}