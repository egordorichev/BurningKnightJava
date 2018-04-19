package org.rexellentgames.dungeon.util;

public class Random {
	public static java.util.Random random = new java.util.Random();

	public static float newFloat(float min, float max) {
		return (min + random.nextFloat() * (max - min));
	}

	public static float newFloat(float max) {
		return (random.nextFloat() * max);
	}

	public static float newFloat() {
		return random.nextFloat();
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

		for (int i = 0; i < length; i++) {
			sum += chances[i];
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
}