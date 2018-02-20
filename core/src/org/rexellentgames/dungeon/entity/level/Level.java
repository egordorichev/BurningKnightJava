package org.rexellentgames.dungeon.entity.level;

import com.badlogic.gdx.math.Vector2;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Entity;

public class Level extends Entity {
	public static final int WIDTH = 32;
	public static final int HEIGHT = 32;
	public static final int SIZE = WIDTH * HEIGHT;
	public static final int[] NEIGHBOURS4 = {-WIDTH, +1, +WIDTH, -1};
	public static final int[] NEIGHBOURS8 = {+1, -1, +WIDTH, -WIDTH, +1 + WIDTH, +1 - WIDTH, -1 + WIDTH, -1 - WIDTH};
	public static final int[] NEIGHBOURS9 = {0, +1, -1, +WIDTH, -WIDTH, +1 + WIDTH, +1 - WIDTH, -1 + WIDTH, -1 - WIDTH};
	public static final Vector2[] NEIGHBOURS8V = {new Vector2(-1, -1), new Vector2(0, -1), new Vector2(1, -1),
		new Vector2(-1, 0), new Vector2(1, 0), new Vector2(-1, 1), new Vector2(0, 1), new Vector2(1, 1)};

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