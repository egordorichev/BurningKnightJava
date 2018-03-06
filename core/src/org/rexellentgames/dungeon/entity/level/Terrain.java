package org.rexellentgames.dungeon.entity.level;

public class Terrain {
	public static short EMPTY = 67;
	public static short DOOR = 66;
	public static short GRASS = 32;
	public static short FLOOR = 1;
	public static short ENTRANCE = 64;
	public static short EXIT = 65;
	public static short WALL = 0;
	public static short LOW_GRASS = 33;
	public static short WATER = 17;
	public static short FALL = 68;
	public static short WATER_FALL = 69;
	public static short DECO = 43;

	public static int[] flags = new int[512];

	public static int PASSABLE = 0x1;
	public static int SOLID = 0x2;
	public static int HOLE = 0x4;
	public static int LOW = 0x8;

	static {
		flags[EMPTY] = HOLE | LOW;
		flags[FALL] = HOLE | LOW;
		flags[WATER_FALL] = HOLE | LOW;
		flags[GRASS] = PASSABLE | LOW;
		flags[WALL] = SOLID;
		flags[DOOR] = PASSABLE;
		flags[FLOOR] = PASSABLE | LOW;
		flags[LOW_GRASS] = PASSABLE | LOW;
		flags[WATER] = PASSABLE | LOW;
		flags[EXIT] = PASSABLE;
		flags[ENTRANCE] = PASSABLE;

		for (int x = 1; x < 32; x++) {
			flags[x] = PASSABLE | LOW;
		}
	}
}