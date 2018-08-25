package org.rexcellentgames.burningknight.entity.level;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.util.Log;
import org.rexcellentgames.burningknight.util.Random;

public class Terrain {
	public static byte WALL = 0;
	public static byte DIRT = 1;
	public static byte FLOOR_A = 2;
	public static byte WATER = 3;
	public static byte WALL_SIDE = 4;
	public static byte CHASM = 5;
	public static byte FLOOR_B = 6;
	public static byte LAVA = 7;
	public static byte GRASS = 9;
	public static byte TABLE = 10;
	public static byte EXIT = 11;
	public static byte FLOOR_C = 12;
	public static byte FLOOR_D = 13;
	public static byte CRACK = 14;
	public static byte ICE = 15;
	public static byte DRY_GRASS = 17;
	public static byte HIGH_GRASS = 16;
	public static byte HIGH_DRY_GRASS = 18;
	public static byte OBSIDIAN = 19;
	public static byte EMBER = 20;
	public static byte COBWEB = 21;

	public static byte SIZE = 22;

	public static int[] flags = new int[SIZE];
	public static Color[][] colors = new Color[10][SIZE];

	public static int PASSABLE = 0x1;
	public static int SOLID = 0x2;
	public static int HOLE = 0x4;
	public static int HIGH = 0x8;
	public static int BURNS = 0x10;
	public static int BREAKS_LOS = 0x20;
	public static int LIQUID_LAYER = 0x40;

	static {
		flags[CHASM] = HOLE;
		flags[DIRT] = PASSABLE | LIQUID_LAYER;
		flags[GRASS] = PASSABLE | BURNS | LIQUID_LAYER;
		flags[FLOOR_A] = PASSABLE;
		flags[WALL] = SOLID | HIGH | BREAKS_LOS;
		flags[CRACK] = SOLID | HIGH | BREAKS_LOS;
		flags[WATER] = PASSABLE | LIQUID_LAYER;
		flags[WALL_SIDE] = 0;
		flags[FLOOR_B] = PASSABLE | BURNS;
		flags[FLOOR_C] = PASSABLE;
		flags[FLOOR_D] = PASSABLE;
		flags[LAVA] = LIQUID_LAYER;
		flags[TABLE] = SOLID | HIGH;
		flags[ICE] = PASSABLE | LIQUID_LAYER;
		flags[DRY_GRASS] = PASSABLE | BURNS | LIQUID_LAYER;
		flags[HIGH_GRASS] = PASSABLE | HIGH | BURNS | LIQUID_LAYER;
		flags[HIGH_DRY_GRASS] = PASSABLE | HIGH | BURNS | LIQUID_LAYER;
		flags[OBSIDIAN] = PASSABLE | LIQUID_LAYER;
		flags[EMBER] = PASSABLE | LIQUID_LAYER;
		flags[COBWEB] = PASSABLE | LIQUID_LAYER | BURNS;
		flags[EXIT] = PASSABLE | LIQUID_LAYER;

		colors[0][CHASM] = Color.valueOf("#000000");
		colors[0][DIRT] = Color.valueOf("#8a4836");
		colors[0][GRASS] = Color.valueOf("#33984b");
		colors[0][HIGH_GRASS] = Color.valueOf("#33984b");
		colors[0][DRY_GRASS] = Color.valueOf("#e69c69");
		colors[0][HIGH_DRY_GRASS] = Color.valueOf("#e69c69");
		colors[0][OBSIDIAN] = Color.valueOf("#2a2f4e");
		colors[0][EMBER] = Color.valueOf("#000000");
		colors[0][FLOOR_A] = Color.valueOf("#657392");
		colors[0][FLOOR_B] = Color.valueOf("#bf6f4a");
		colors[0][FLOOR_C] = Color.valueOf("#92a1b9");
		colors[0][FLOOR_D] = Color.valueOf("#ffa214");
		colors[0][WATER] = Color.valueOf("#0098dc");
		colors[0][LAVA] = Color.valueOf("#ff5000");
		colors[0][EXIT] = Color.valueOf("#424c6e");
		colors[0][TABLE] = Color.valueOf("#f6ca9f");

		colors[1][CHASM] = Color.valueOf("#000000");
		colors[1][DIRT] = Color.valueOf("#8a4836");
		colors[1][GRASS] = Color.valueOf("#33984b");
		colors[1][HIGH_GRASS] = Color.valueOf("#33984b");
		colors[1][DRY_GRASS] = Color.valueOf("#e69c69");
		colors[1][HIGH_DRY_GRASS] = Color.valueOf("#e69c69");
		colors[1][OBSIDIAN] = Color.valueOf("#2a2f4e");
		colors[1][EMBER] = Color.valueOf("#000000");
		colors[1][FLOOR_A] = Color.valueOf("#bf6f4a");
		colors[1][FLOOR_B] = Color.valueOf("#f5555d");
		colors[1][FLOOR_C] = Color.valueOf("#5d2c28");
		colors[1][FLOOR_D] = Color.valueOf("#f6ca9f");
		colors[1][WATER] = Color.valueOf("#0098dc");
		colors[1][LAVA] = Color.valueOf("#ff5000");
		colors[1][EXIT] = Color.valueOf("#424c6e");
		colors[1][TABLE] = Color.valueOf("#858585");
	}

	public static Color getColor(byte t) {
		if (t == Terrain.WALL || t == Terrain.CRACK) {
			return Level.colors[Dungeon.level.uid];
		}

		Color color = colors[lastt][t];

		if (color != null) {
			return color;
		}

		return Color.WHITE;
	}

	public static byte last;

	public static byte randomFloorNotLast() {
		byte l = last;

		do {
			randomFloor();
		} while (last == l);

		return last;
	}

	public static byte randomFloor() {
		switch (Random.newInt(3)) {
			case 0: default: last = FLOOR_A; break;
			case 1: last = FLOOR_B; break;
			case 2: last = FLOOR_C; break;
		}

		return last;
	}

	public static TextureRegion[] dither = new TextureRegion[10];

	public static TextureRegion dirtPattern;
	public static TextureRegion grassPattern;
	public static TextureRegion dryGrassPattern;
	public static TextureRegion waterPattern;
	public static TextureRegion lavaPattern;
	public static TextureRegion wallPattern;
	public static TextureRegion crackPattern;
	public static TextureRegion chasmPattern;
	public static TextureRegion cobwebPattern;
	public static TextureRegion emberPattern;
	public static TextureRegion obsidianPattern;
	public static TextureRegion icePattern;
	public static TextureRegion[] patterns = new TextureRegion[SIZE];

	public static TextureRegion[] pooledge = new TextureRegion[16];
	public static TextureRegion[] lavaedge = new TextureRegion[16];
	public static TextureRegion[] dirtedge = new TextureRegion[16];
	public static TextureRegion[] drygrassedge = new TextureRegion[16];
	public static TextureRegion[] grassedge = new TextureRegion[16];
	public static TextureRegion[] obedge = new TextureRegion[16];
	public static TextureRegion[] webedge = new TextureRegion[16];
	public static TextureRegion[] woodVariants = new TextureRegion[16];
	public static TextureRegion[] badVariants = new TextureRegion[16];
	public static TextureRegion[] goldVariants = new TextureRegion[16];
	public static TextureRegion[] floorVariants = new TextureRegion[16];
	public static TextureRegion[] tableVariants = new TextureRegion[16];
	public static TextureRegion[] topVariants = new TextureRegion[12];
	public static TextureRegion[][] wallTop = new TextureRegion[3][12];
	public static TextureRegion[] sides = new TextureRegion[3];

	public static TextureRegion[][] variants = new TextureRegion[SIZE][16];
	public static TextureRegion[] decor;

	public static TextureRegion exit;
	public static TextureRegion entrance;

	private static int lastt = -1;

	public static void loadTextures(int set) {
		if (lastt == set) {
			return;
		}

		lastt = set;
		String bm = "biome-" + set;

		Log.info("Loading biome " + set);

		dirtPattern = Graphics.getTexture("biome-gen-dirt pattern");
		grassPattern = Graphics.getTexture("biome-gen-grass pattern");
		dryGrassPattern = Graphics.getTexture("biome-gen-dry_grass_pattern");
		waterPattern = Graphics.getTexture("biome-gen-pool pattern");
		lavaPattern = Graphics.getTexture("biome-gen-lava pattern");
		wallPattern = Graphics.getTexture(bm + "-wall pattern");
		crackPattern = Graphics.getTexture(bm + "-crack");
		emberPattern = Graphics.getTexture("biome-gen-coal_pattern");
		cobwebPattern = Graphics.getTexture("biome-gen-cobweb_pattern");
		obsidianPattern = Graphics.getTexture("biome-gen-obsidian_pattern");
		icePattern = Graphics.getTexture("biome-gen-ice_pattern");

		entrance = Graphics.getTexture("props-entance");
		exit = Graphics.getTexture("props-exit");

		patterns[DIRT] = dirtPattern;
		patterns[GRASS] = grassPattern;
		patterns[WATER] = waterPattern;
		patterns[LAVA] = lavaPattern;
		patterns[WALL] = wallPattern;
		patterns[CRACK] = crackPattern;

		decor = new TextureRegion[] {
			Graphics.getTexture("props-decor_a"),
			Graphics.getTexture("props-decor_b"),
			Graphics.getTexture("props-decor_c"),
			Graphics.getTexture("props-decor_d"),
			Graphics.getTexture("props-decor_e")
		};

		for (int j = 0; j < 3; j++) {
			for (int i = 0; i < 12; i++) {
				wallTop[j][i] = Graphics.getTexture(bm + "-" + j + " top " + i);
			}
		}

		for (int i = 0; i < 3; i++) {
			sides[i] = Graphics.getTexture(bm + "-side " + i);
		}

		for (int i = 0; i < 10; i++) {
			dither[i] = Graphics.getTexture("fx-dither-idle-" + String.format("%02d", i));
		}

		for (int i = 0; i < 16; i++) {
			pooledge[i] = Graphics.getTexture("biome-gen-pooledge" + Level.COMPASS[i]);
		}

		for (int i = 0; i < 16; i++) {
			lavaedge[i] = Graphics.getTexture("biome-gen-lavaedge" + Level.COMPASS[i]);
		}

		for (int i = 0; i < 16; i++) {
			obedge[i] = Graphics.getTexture("biome-gen-ob" + Level.COMPASS[i]);
		}

		for (int i = 0; i < 16; i++) {
			webedge[i] = Graphics.getTexture("biome-gen-web" + Level.COMPASS[i]);
		}

		for (int i = 0; i < 16; i++) {
			grassedge[i] = Graphics.getTexture("biome-gen-grassedge" + Level.COMPASS[i]);
		}

		for (int i = 0; i < 16; i++) {
			drygrassedge[i] = Graphics.getTexture("biome-gen-dry" + Level.COMPASS[i]);
		}

		for (int i = 0; i < 16; i++) {
			dirtedge[i] = Graphics.getTexture("biome-gen-dirtedge" + Level.COMPASS[i]);
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

		variants[FLOOR_B] = woodVariants;
		variants[FLOOR_A] = floorVariants;
		variants[TABLE] = tableVariants;
		variants[FLOOR_C] = badVariants;
		variants[FLOOR_D] = goldVariants;
	}

	public static char[] letters = new char[] { 'A', 'B', 'C', 'D' };
	public static int[] wallMap = new int[] { -1, -1, -1, 9, -1, -1, 0, 5, -1, 11, -1, 10, 2, 6, 1, -1 };
	public static int[] wallMapExtra = new int[] { -1, 7, 3, -1, 4, -1, -1, -1, 8, -1, -1, -1, -1, -1, -1, -1 };
}