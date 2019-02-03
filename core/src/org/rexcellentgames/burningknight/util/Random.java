package org.rexcellentgames.burningknight.util;

import org.rexcellentgames.burningknight.game.state.ItemSelectState;

public class Random {
	private static String seed = "";

	public static String getSeed() {
		return seed;
	}

	public static void setSeed(String seed) {
		switch (seed) {
			case "ICE": case "CASTLE": case "FOREST": case "LIBRARY":
			case "BLOOD": case "TECH": case "DESERT": case "MAANEX":
			case "DIE": case "BOMB": case "KEY": case "HP": case "CHEATER":
			case "GOLD": case "BK":
				seed += "_" + ItemSelectState.randomAlphaNumeric(8);
		}

		Random.seed = seed;

		random = new java.util.Random(ItemSelectState.stringToSeed(seed));
		Log.error("Seed is " + seed + " (" + ItemSelectState.stringToSeed(seed) + ") ");
	}

	public static java.util.Random random = new java.util.Random();

	public static float newFloat(float min, float max) {
		return (min + random.nextFloat() * (max - min));
	}

	public static float newFloatDice(float min, float max) {
		return (newFloat(min, max) + newFloat(min, max)) / 2;
	}

	public static float newFloat(float max) {
		return (random.nextFloat() * max);
	}

	public static float newFloat() {
		return random.nextFloat();
	}

	public static float newAngle() {
		return newFloat((float) (Math.PI * 2));
	}

	public static int newInt(int max) {
		return max > 0 ? (int) (random.nextFloat() * max) : 0;
	}

	public static int newInt(int min, int max) {
		return min + (int) (random.nextFloat() * (max - min));
	}

	public static boolean chance(float a) {
		return newFloat(100) <= a;
	}

	public static int chances(float[] chances) {
		int length = chances.length;
		float sum = 0;

		for (float chance : chances) {
			sum += chance;
		}

		float value = newFloat(sum);
		sum = 0;

		for (int i = 0; i < length; i++) {
			sum += chances[i];

				if (value < sum) {
				return i;
			}
		}

		return -1;
	}

	public static int chances(Float[] chances) {
		float[] primitiveChances = new float[chances.length];
		
		for (int i = 0; i < chances.length; i++) {
			primitiveChances[i] = chances[i];
		}
		
		return chances(primitiveChances);
	}
}