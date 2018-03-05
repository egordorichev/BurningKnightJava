package org.rexellentgames.dungeon.util;

public class Random {
	public static float newFloat(float min, float max) {
		return (float) (min + Math.random() * (max - min));
	}

	public static float newFloat(float max) {
		return (float) (Math.random() * max);
	}

	public static float newFloat() {
		return (float) Math.random();
	}

	public static int newInt(int max) {
		return max > 0 ? (int) (Math.random() * max) : 0;
	}

	public static int newInt(int min, int max) {
		return min + (int) (Math.random() * (max - min));
	}

	public static boolean chance(int a) {
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