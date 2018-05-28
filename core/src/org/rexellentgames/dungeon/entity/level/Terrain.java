package org.rexellentgames.dungeon.entity.level;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.util.Log;

public class Terrain {
	public static byte CHASM = 0;
	public static byte DIRT = 1;
	public static byte FLOOR_A = 2;
	public static byte WATER = 3;
	public static byte WALL_SIDE = 4;
	public static byte WALL = 5;
	public static byte FLOOR_B = 6;
	public static byte LAVA = 7;
	public static byte PLANTED_DIRT = 8;
	public static byte GRASS = 9;
	public static byte TABLE = 10;
	public static byte EXIT = 11;
	public static byte FLOOR_C = 12;
	public static byte FLOOR_D = 13;
	public static byte CRACK = 14;

	public static byte SIZE = 15;

	public static int[] flags = new int[SIZE];

	public static int PASSABLE = 0x1;
	public static int SOLID = 0x2;
	public static int HOLE = 0x4;
	public static int HIGH = 0x8;
	public static int IS_DIRT = 0x10;
	public static int BREAKS_LOS = 0x12;

	static {
		flags[CHASM] = HOLE;
		flags[DIRT] = PASSABLE | IS_DIRT;
		flags[GRASS] = PASSABLE;
		flags[PLANTED_DIRT] = PASSABLE | IS_DIRT;
		flags[FLOOR_A] = PASSABLE;
		flags[WALL] = SOLID | HIGH | BREAKS_LOS;
		flags[CRACK] = SOLID | HIGH | BREAKS_LOS;
		flags[WATER] = PASSABLE;
		flags[WALL_SIDE] = HOLE;
		flags[FLOOR_B] = PASSABLE;
		flags[FLOOR_C] = PASSABLE;
		flags[FLOOR_D] = PASSABLE;
		flags[LAVA] = 0;
		flags[TABLE] = SOLID | HIGH;
		flags[EXIT] = 0;
	}

	public static TextureRegion[] dither = new TextureRegion[10];

	public static TextureRegion dirtPattern;
	public static TextureRegion grassPattern;
	public static TextureRegion waterPattern;
	public static TextureRegion lavaPattern;
	public static TextureRegion wallPattern;
	public static TextureRegion crackPattern;
	public static TextureRegion[] patterns = new TextureRegion[SIZE];

	public static TextureRegion[] dirtVariants = new TextureRegion[15];
	public static TextureRegion[] pooledge = new TextureRegion[15];
	public static TextureRegion[] waterVariants = new TextureRegion[16];
	public static TextureRegion[] lavaVariants = new TextureRegion[15];
	public static TextureRegion[] chasmVariants = new TextureRegion[15];
	public static TextureRegion[] wallVariants = new TextureRegion[15];
	public static TextureRegion[] woodVariants = new TextureRegion[16];
	public static TextureRegion[] badVariants = new TextureRegion[16];
	public static TextureRegion[] goldVariants = new TextureRegion[16];
	public static TextureRegion[] floorVariants = new TextureRegion[16];
	public static TextureRegion[] tableVariants = new TextureRegion[16];
	public static TextureRegion[] topVariants = new TextureRegion[12];

	public static TextureRegion[][] variants = new TextureRegion[SIZE][16];
	public static TextureRegion[] decor;

	public static TextureRegion exit;
	public static TextureRegion entrance;
	public static TextureRegion closedDoor;
	public static TextureRegion openDoor;

	public static TextureRegion chasm;

	private static int last = -1;

	public static void loadTextures(int set) {
		if (last == set) {
			return;
		}

		last = set;
		String bm = "biome-" + set;

		Log.info("Loading biome " + set);

		dirtPattern = Graphics.getTexture(bm + " (dirt pattern)");
		grassPattern = Graphics.getTexture(bm + " (grass pattern)");
		waterPattern = Graphics.getTexture(bm + " (pool pattern)");
		lavaPattern = Graphics.getTexture(bm + " (lava pattern)");
		wallPattern = Graphics.getTexture(bm + "-wall pattern");
		crackPattern = Graphics.getTexture("biome-gen-crack");

		entrance = Graphics.getTexture(bm + " (stairs U)");
		exit = Graphics.getTexture(bm + " (stairs D)");
		closedDoor = Graphics.getTexture(bm + " (door WE)");
		openDoor = Graphics.getTexture(bm + " (door NS)");

		patterns[DIRT] = dirtPattern;
		patterns[GRASS] = grassPattern;
		patterns[PLANTED_DIRT] = dirtPattern;
		patterns[WATER] = waterPattern;
		patterns[LAVA] = lavaPattern;
		patterns[WALL] = wallPattern;
		patterns[CRACK] = crackPattern;

		decor = new TextureRegion[] {
			Graphics.getTexture(bm + " (torch A)"),
			Graphics.getTexture(bm + " (torch B)"),
			Graphics.getTexture(bm + " (walldeco A)"),
			Graphics.getTexture(bm + " (walldeco B)"),
			Graphics.getTexture(bm + " (walldeco C)")
		};

		for (int i = 0; i < 10; i++) {
			dither[i] = Graphics.getTexture("fx-dither-idle-" + String.format("%02d", i));
		}

		for (int i = 0; i < 15; i++) {
			dirtVariants[i] = Graphics.getTexture(bm + " (dirt" + Level.COMPASS[i] + ")");
		}

		for (int i = 0; i < 15; i++) {
			pooledge[i] = Graphics.getTexture("biome-gen-pooledge" + Level.COMPASS[i]);
		}

		for (int i = 0; i < 16; i++) {
			waterVariants[i] = Graphics.getTexture("biome-gen-pool" + Level.COMPASS[i]);
		}

		for (int i = 0; i < 15; i++) {
			lavaVariants[i] = Graphics.getTexture(bm + " (lava" + Level.COMPASS[i] + ")");
		}

		for (int i = 0; i < 15; i++) {
			chasmVariants[i] = Graphics.getTexture(bm + " (chasm" + Level.COMPASS[i] + ")");
		}

		for (int i = 0; i < 15; i++) {
			wallVariants[i] = Graphics.getTexture("biome-gen-wall" + Level.COMPASS[i]);
		}

		for (int i = 0; i < 16; i++) {
			tableVariants[i] = Graphics.getTexture(bm + " (desk" + Level.COMPASS[i] + ")");
		}

		for (int i = 0; i < 16; i++) {
			woodVariants[i] = Graphics.getTexture(bm + "-floor B " + String.format("%02d", i + 1));
		}

		for (int i = 0; i < 16; i++) {
			floorVariants[i] = Graphics.getTexture(bm + "-floor A " + String.format("%02d", i + 1));
		}

		for (int i = 0; i < 16; i++) {
			goldVariants[i] = Graphics.getTexture(bm + "-floor D " + String.format("%02d", i + 1));
		}

		for (int i = 0; i < 16; i++) {
			badVariants[i] = Graphics.getTexture(bm + "-floor C " + String.format("%02d", i + 1));
		}

		for (int i = 0; i < 12; i++) {
			topVariants[i] = Graphics.getTexture(bm + "-wall " + letters[i / 4] + " " + String.format("%02d", i % 4 + 1));
		}

		variants[DIRT] = dirtVariants;
		variants[GRASS] = dirtVariants;
		variants[PLANTED_DIRT] = dirtVariants;
		variants[CHASM] = chasmVariants;
		variants[WALL] = wallVariants;
		variants[CRACK] = wallVariants;
		variants[FLOOR_B] = woodVariants;
		variants[FLOOR_A] = floorVariants;
		variants[LAVA] = lavaVariants;
		variants[TABLE] = tableVariants;
		variants[FLOOR_C] = badVariants;
		variants[FLOOR_D] = goldVariants;

		chasm = Graphics.getTexture(bm + " (chasmbg)");
	}

	public static char[] letters = new char[] { 'A', 'B', 'C', 'D' };
}