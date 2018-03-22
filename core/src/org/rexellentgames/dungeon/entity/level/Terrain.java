package org.rexellentgames.dungeon.entity.level;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexellentgames.dungeon.assets.Graphics;

public class Terrain {
	public static byte EMPTY = 0;
	public static byte DIRT = 1;
	public static byte FLOOR = 2;
	public static byte WATER = 3;
	public static short WALL_SIDE = 4;
	public static byte WALL = 5;
	public static short WOOD = 6;
	public static short SPIKES = 7;

	public static int[] flags = new int[8];

	public static int PASSABLE = 0x1;
	public static int SOLID = 0x2;
	public static int HOLE = 0x4;
	public static int HIGH = 0x8;

	static {
		flags[EMPTY] = HOLE;
		flags[DIRT] = PASSABLE;
		flags[FLOOR] = PASSABLE;
		flags[WALL] = SOLID | HIGH;
		flags[WATER] = PASSABLE;
		flags[WALL_SIDE] = HOLE;
		flags[WOOD] = PASSABLE;
		flags[SPIKES] = 0;
	}

	public static TextureRegion dirtPattern;
	public static TextureRegion waterPattern;

	public static void loadTextures(int set) {
		// todo: variants

		dirtPattern = Graphics.getTexture("biome-" + set + " (dirt pattern).png");
		waterPattern = Graphics.getTexture("biome-" + set + " (pool pattern).png");
	}
}