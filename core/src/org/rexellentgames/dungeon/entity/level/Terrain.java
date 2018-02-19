package org.rexellentgames.dungeon.entity.level;

public class Terrain {
	public static int EMPTY = 0;
	public static int WALL = 33;
	// don't forget to update this value!
	public static int TERRAIN_MAX = 34;

	public static int[] flags = new int[TERRAIN_MAX];

	public static int PASSABLE = 0x1;
	public static int SOLID = 0x2;

	static {
		flags[EMPTY] = PASSABLE;
		flags[WALL] = SOLID;
	}
}