package org.rexcellentgames.burningknight.entity.level;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.util.Log;
import org.rexcellentgames.burningknight.util.Random;

public class Terrain {
	public static byte CHASM = 0;
	public static byte DIRT = 1;
	public static byte FLOOR_A = 2;
	public static byte WATER = 3;
	public static byte WALL_SIDE = 4;
	public static byte WALL = 5;
	public static byte FLOOR_B = 6;
	public static byte LAVA = 7;
	public static byte GRASS = 9;
	public static byte TABLE = 10;
	public static byte EXIT = 11;
	public static byte FLOOR_C = 12;
	public static byte FLOOR_D = 13;
	public static byte CRACK = 14;
	public static byte FLOOR_FAKE = 15;

	public static byte SIZE = 16;

	public static int[] flags = new int[SIZE];

	public static int PASSABLE = 0x1;
	public static int SOLID = 0x2;
	public static int HOLE = 0x4;
	public static int HIGH = 0x8;
	public static int BREAKS_LOS = 0x20;
	public static int BREAKS_ENEMY_LOS = 0x40;

	static {
		flags[CHASM] = HOLE;
		flags[DIRT] = PASSABLE;
		flags[GRASS] = PASSABLE;
		flags[FLOOR_A] = PASSABLE;
		flags[FLOOR_FAKE] = PASSABLE | BREAKS_ENEMY_LOS;
		flags[WALL] = SOLID | HIGH | BREAKS_LOS;
		flags[CRACK] = SOLID | HIGH | BREAKS_LOS;
		flags[WATER] = PASSABLE;
		flags[WALL_SIDE] = 0;
		flags[FLOOR_B] = PASSABLE;
		flags[FLOOR_C] = PASSABLE;
		flags[FLOOR_D] = PASSABLE;
		flags[LAVA] = 0;
		flags[TABLE] = SOLID | HIGH;
		flags[EXIT] = 0;
	}

	public static byte randomFloor() {
		switch (Random.newInt(3)) {
			case 0: default: return FLOOR_A;
			case 1: return FLOOR_B;
			case 2: return FLOOR_C;
		}
	}

	public static TextureRegion[] dither = new TextureRegion[10];

	public static TextureRegion dirtPattern;
	public static TextureRegion grassPattern;
	public static TextureRegion waterPattern;
	public static TextureRegion lavaPattern;
	public static TextureRegion wallPattern;
	public static TextureRegion crackPattern;
	public static TextureRegion chasmPattern;
	public static TextureRegion[] patterns = new TextureRegion[SIZE];

	public static TextureRegion[] pooledge = new TextureRegion[15];
	public static TextureRegion[] lavaedge = new TextureRegion[15];
	public static TextureRegion[] waterVariants = new TextureRegion[16];
	public static TextureRegion[] lavaVariants = new TextureRegion[16];
	public static TextureRegion[] wallVariants = new TextureRegion[15];
	public static TextureRegion[] woodVariants = new TextureRegion[16];
	public static TextureRegion[] badVariants = new TextureRegion[16];
	public static TextureRegion[] goldVariants = new TextureRegion[16];
	public static TextureRegion[] floorVariants = new TextureRegion[16];
	public static TextureRegion[] tableVariants = new TextureRegion[16];
	public static TextureRegion[] topVariants = new TextureRegion[12];
	public static TextureRegion[] wallTop = new TextureRegion[12];
	public static TextureRegion[] sides = new TextureRegion[3];

	public static TextureRegion[][] variants = new TextureRegion[SIZE][16];
	public static TextureRegion[] decor;

	public static TextureRegion exit;
	public static TextureRegion entrance;

	private static int last = -1;

	public static void loadTextures(int set) {
		if (last == set) {
			return;
		}

		last = set;
		String bm = "biome-" + set;

		Log.info("Loading biome " + set);

		dirtPattern = Graphics.getTexture("biome-gen-dirt pattern");
		grassPattern = Graphics.getTexture("biome-gen-grass pattern");
		waterPattern = Graphics.getTexture("biome-gen-pool pattern");
		lavaPattern = Graphics.getTexture("biome-gen-lava pattern");
		wallPattern = Graphics.getTexture(bm + "-wall pattern");
		crackPattern = Graphics.getTexture(bm + "-crack");

		entrance = Graphics.getTexture("prop (stairs U)");
		exit = Graphics.getTexture("prop (stairs D)");

		patterns[DIRT] = dirtPattern;
		patterns[GRASS] = grassPattern;
		patterns[WATER] = waterPattern;
		patterns[LAVA] = lavaPattern;
		patterns[WALL] = wallPattern;
		patterns[CRACK] = crackPattern;

		decor = new TextureRegion[] {
			Graphics.getTexture("prop (torch A)"),
			Graphics.getTexture("prop (torch B)"),
			Graphics.getTexture("prop (walldeco A)"),
			Graphics.getTexture("prop (walldeco B)"),
			Graphics.getTexture("prop (walldeco C)")
		};

		for (int i = 0; i < 12; i++) {
			wallTop[i] = Graphics.getTexture(bm + "-top " + i);
		}

		for (int i = 0; i < 3; i++) {
			sides[i] = Graphics.getTexture(bm + "-side " + i);
		}

		for (int i = 0; i < 10; i++) {
			dither[i] = Graphics.getTexture("fx-dither-idle-" + String.format("%02d", i));
		}

		for (int i = 0; i < 15; i++) {
			pooledge[i] = Graphics.getTexture("biome-gen-pooledge" + Level.COMPASS[i]);
		}

		for (int i = 0; i < 15; i++) {
			lavaedge[i] = Graphics.getTexture("biome-gen-lavaedge" + Level.COMPASS[i]);
		}

		for (int i = 0; i < 16; i++) {
			waterVariants[i] = Graphics.getTexture("biome-gen-pool" + Level.COMPASS[i]);
		}

		for (int i = 0; i < 16; i++) {
			lavaVariants[i] = Graphics.getTexture("biome-gen-lava" + Level.COMPASS[i]);
		}

		/*
		for (int i = 0; i < 15; i++) {
			chasmVariants[i] = Graphics.getTexture(bm + " (chasm" + Level.COMPASS[i] + ")");
		}*/

		for (int i = 0; i < 15; i++) {
			wallVariants[i] = Graphics.getTexture("biome-gen-wall" + Level.COMPASS[i]);
		}

		for (int i = 0; i < 16; i++) {
			tableVariants[i] = Graphics.getTexture(bm + "-platform A" + Level.COMPASS[i]);
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

		chasmPattern = Graphics.getTexture("biome-gen-chasm_bg");
		patterns[CHASM] = chasmPattern;

		// variants[WALL] = wallVariants;
		// variants[CRACK] = wallVariants;
		variants[FLOOR_B] = woodVariants;
		variants[FLOOR_A] = floorVariants;
		variants[FLOOR_FAKE] = floorVariants;
		variants[LAVA] = lavaVariants;
		variants[TABLE] = tableVariants;
		variants[FLOOR_C] = badVariants;
		variants[FLOOR_D] = goldVariants;
	}

	public static char[] letters = new char[] { 'A', 'B', 'C', 'D' };
	public static int[] wallMap = new int[] { -1, -1, -1, 9, -1, -1, 0, 5, -1, 11, -1, 10, 2, 6, 1, -1 };
	public static int[] wallMapExtra = new int[] { -1, 7, 3, -1, 4, -1, -1, -1, 8, -1, -1, -1, -1, -1, -1, -1 };
}