package org.rexellentgames.dungeon.entity.level;

public class Terrain {
	public static short EMPTY = 67;
	public static short DOOR = 66;
	public static short DIRT = 63;
	public static short GOOD_DIRT = 71;
	public static short PLANTED_DIRT = 72;
	public static short FLOOR = 1;
	public static short ENTRANCE = 64;
	public static short EXIT = 65;
	public static short WALL = 0;
	public static short WATER = 255;
	public static short FALL = 68;
	public static short WATER_FALL = 69;
	public static short DECO = 43;
	public static short WOOD = 44;
	public static short WOOD_SUPPORT = 45;
	public static short SPIKES = 46;

	public static int[] flags = new int[512];

	public static int PASSABLE = 0x1;
	public static int SOLID = 0x2;
	public static int HOLE = 0x4;
	public static int HIGH = 0x8;

	static {
		flags[EMPTY] = HOLE;
		flags[FALL] = HOLE;
		flags[WATER_FALL] = HOLE;
		flags[DIRT] = PASSABLE;
		flags[WALL] = SOLID | HIGH;
		flags[DOOR] = PASSABLE;
		flags[FLOOR] = PASSABLE;
		flags[WATER] = PASSABLE;
		flags[EXIT] = PASSABLE;
		flags[ENTRANCE] = PASSABLE;
		flags[WOOD] = PASSABLE;
		flags[WOOD_SUPPORT] = HOLE;
		flags[SPIKES] = 0;

		for (int x = 1; x < 32; x++) {
			flags[x] = PASSABLE;
		}

		for (int x = 34; x < 41; x++) {
			flags[x] = PASSABLE;
		}

		for (int x = 47; x < 63; x++) {
			flags[x] = PASSABLE;
		}
	}
}