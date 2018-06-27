package org.rexcellentgames.burningknight.entity.level;

import org.rexcellentgames.burningknight.util.Random;

public class Patch {
	public static boolean[] generate(float seed, int octaves) {
		return generate(Level.getWidth(), Level.getHeight(), seed, octaves);
	}

	public static boolean[] generate(int w, int h, float seed, int octaves) {
		boolean[] cur = new boolean[w * h];
		boolean[] off = new boolean[w * h];

		for (int i = 0; i < w * h; i++) {
			off[i] = Random.newFloat() < seed;
		}

		for (int i = 0; i < octaves; i++) {
			for (int y = 1; y < h - 1; y++) {
				for (int x = 1; x < w - 1; x++) {

					int pos = x + y * w;
					int count = 0;

					if (off[pos - w - 1]) {
						count++;
					}

					if (off[pos - w]) {
						count++;
					}

					if (off[pos - w + 1]) {
						count++;
					}

					if (off[pos - 1]) {
						count++;
					}

					if (off[pos + 1]) {
						count++;
					}

					if (off[pos + w - 1]) {
						count++;
					}

					if (off[pos + w]) {
						count++;
					}

					if (off[pos + w + 1]) {
						count++;
					}

					if (!off[pos] && count >= 5) {
						cur[pos] = true;
					} else {
						cur[pos] = off[pos] && count >= 4;
					}
				}
			}

			boolean[] tmp = cur;
			cur = off;
			off = tmp;
		}

		return off;
	}
}