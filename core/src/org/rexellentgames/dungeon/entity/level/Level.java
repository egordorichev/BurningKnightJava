package org.rexellentgames.dungeon.entity.level;

import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Entity;

public class Level extends Entity {
	public static int WIDTH = 32;
	public static int HEIGHT = 32;
	public static int SIZE = WIDTH * HEIGHT;

	protected short[] data;

	@Override
	public void init() {
		this.data = new short[SIZE];
	}

	@Override
	public void render() {
		// todo: better render, using camera bounds

		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				short tile = this.get(x, y);

				if (tile > 0) {
					int xx = tile % 32 * 16;
					int yy = (int) (Math.floor(tile / 32) * 16);

					Graphics.batch.draw(Graphics.spriteSheet, x * 16, y * 16, 16, 16,
						xx, yy, 16, 16, false, false);
				}
			}
		}
	}

	public void set(int i, short v) {
		this.data[i] = v;
	}

	public void set(int x, int y, short v) {
		this.data[toIndex(x, y)] = v;
	}

	public short get(int i) {
		return this.data[i];
	}

	public short get(int x, int y) {
		return this.data[toIndex(x, y)];
	}

	public static int toIndex(int x, int y) {
		return x + y * WIDTH;
	}

	public boolean generate() {
		return false;
	}
}