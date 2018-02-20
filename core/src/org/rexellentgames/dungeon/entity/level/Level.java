package org.rexellentgames.dungeon.entity.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.file.FileReader;
import org.rexellentgames.dungeon.util.file.FileWriter;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Level extends Entity {
	public static final byte VERSION = 0;
	private static final String SAVE_PATH = ".ldg/save.dat";

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

	public void generateUntilGood() {
		Log.info("Generating...");

		while (!this.generate()) {
			Log.error("Failed to generate the level!");
		}
	}

	public void load() {
		FileHandle save = Gdx.files.external(SAVE_PATH);

		if (!save.exists()) {
			generateUntilGood();
			return;
		}

		Log.info("Loading...");

		try {
			FileReader stream = new FileReader(save.file().getAbsolutePath());

			byte version = stream.readByte();

			if (version < VERSION) {
				Log.info("Old version, porting...");
				// todo: port to the current version
			} else if (version > VERSION) {
				Log.error("Future version! Can't load!");
				return;
			}

			for (int i = 0; i < SIZE; i++) {
				this.data[i] = stream.readInt16();
			}

			stream.close();
		} catch (Exception e) {
			Dungeon.reportException(e);
		}
	}

	public void save() {
		FileHandle save = Gdx.files.external(SAVE_PATH);
		Log.info("Saving...");

		try {
			FileWriter stream = new FileWriter(save.file().getAbsolutePath());

			stream.writeByte(VERSION);

			for (int i = 0; i < SIZE; i++) {
				stream.writeInt16(this.data[i]);
			}

			stream.close();
		} catch (Exception e) {
			Dungeon.reportException(e);
		}
	}

	protected void writeData(FileWriter stream) throws Exception {

	}

	protected void loadData(FileReader stream) throws Exception {

	}
}