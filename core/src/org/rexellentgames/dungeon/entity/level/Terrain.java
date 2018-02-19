package org.rexellentgames.dungeon.entity.level;

public class Terrain {
	public static short EMPTY = 0;
	public static short DOOR = 3;
	public static short GRASS = 4;
	public static short WALL = 33;
	// don't forget to update this value!
	public static short TERRAIN_MAX = 34;

	public static int[] flags = new int[TERRAIN_MAX];

	public static int PASSABLE = 0x1;
	public static int SOLID = 0x2;

	static {
		flags[EMPTY] = PASSABLE;
		flags[GRASS] = PASSABLE;
		flags[WALL] = SOLID;
		flags[DOOR] = PASSABLE;
	}
}