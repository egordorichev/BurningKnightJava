package org.rexellentgames.dungeon.entity.level;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.util.Log;

public class Terrain {
	public static byte CHASM = 0;
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
		flags[CHASM] = HOLE;
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
	public static TextureRegion wallPattern;
	public static TextureRegion[] patterns = new TextureRegion[8];

	public static TextureRegion[] dirtVariants = new TextureRegion[15];
	public static TextureRegion[] waterVariants = new TextureRegion[15];
	public static TextureRegion[] chasmVariants = new TextureRegion[15];
	public static TextureRegion[] wallVariants = new TextureRegion[15];

	public static TextureRegion[][] variants = new TextureRegion[8][15];

	private static int last = -1;

	public static void loadTextures(int set) {
		if (last == set) {
			return;
		}

		last = set;
		String bm = "biome-" + set;

		Log.info("Loading biome " + set);

		dirtPattern = Graphics.getTexture(bm + " (dirt pattern)");
		waterPattern = Graphics.getTexture(bm + " (pool pattern)");
		wallPattern = Graphics.getTexture(bm + " (wall pattern)");

		patterns[DIRT] = dirtPattern;
		patterns[WATER] = waterPattern;
		patterns[WALL] = wallPattern;

		for (int i = 0; i < 15; i++) {
			dirtVariants[i] = Graphics.getTexture(bm + " (dirt " + Level.COMPASS[i] + ")");
		}

		for (int i = 0; i < 15; i++) {
			waterVariants[i] = Graphics.getTexture(bm + " (pool " + Level.COMPASS[i] + ")");
		}

		for (int i = 0; i < 15; i++) {
			chasmVariants[i] = Graphics.getTexture(bm + " (chasm " + Level.COMPASS[i] + ")");
		}

		for (int i = 0; i < 15; i++) {
			wallVariants[i] = Graphics.getTexture(bm + " (wall " + Level.COMPASS[i] + ")");
		}

		variants[DIRT] = dirtVariants;
		variants[CHASM] = chasmVariants;
		variants[WATER] = waterVariants;
		variants[WALL] = wallVariants;
	}
}