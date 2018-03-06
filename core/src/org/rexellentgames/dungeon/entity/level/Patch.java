package org.rexellentgames.dungeon.entity.level;

import org.rexellentgames.dungeon.util.Random;

public class Patch {
	public static boolean[] generate(float seed, int octaves) {
		int w = Level.getWIDTH();
		int h = Level.getHEIGHT();

		boolean[] cur = new boolean[Level.getSIZE()];
		boolean[] off = new boolean[Level.getSIZE()];

		for (int i = 0; i < Level.getSIZE(); i++) {
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
					} else if (off[pos] && count >= 4) {
						cur[pos] = true;
					} else {
						cur[pos] = false;
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