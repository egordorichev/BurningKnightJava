package org.rexellentgames.dungeon.entity.level;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.util.Log;

public class Terrain {
	public static byte EMPTY = 0;
	public static byte DIRT = 1;
	public static byte FLOOR = 2;
	public static byte WATER = 3;
	public static short WALL_SIDE = 4;
	public static byte WALL = 5;
	public static byte WOOD = 6;
	public static byte SPIKES = 7;

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
	public static TextureRegion woodPattern;
	public static TextureRegion wallPattern;
	public static TextureRegion[] patterns = new TextureRegion[8];
	private static int last = -1;

	public static void loadTextures(int set) {
		if (last == set) {
			return;
		}

		last = set;

		Log.info("Loading biome " + set);

		Log.info("biome-" + set + " (dirt pattern)");

		dirtPattern = Graphics.getTexture("biome-" + set + " (dirt pattern)");
		waterPattern = Graphics.getTexture("biome-" + set + " (pool pattern)");
		woodPattern = Graphics.getTexture("biome-" + set + " (planks pattern)");
		wallPattern = Graphics.getTexture("biome-" + set + " (wall pattern)");

		Log.info("dirt " + dirtPattern);

		patterns[DIRT] = dirtPattern;
		patterns[WATER] = waterPattern;
		patterns[WOOD] = woodPattern;
		patterns[WALL] = wallPattern;
	}
}