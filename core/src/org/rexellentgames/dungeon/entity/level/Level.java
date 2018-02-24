package org.rexellentgames.dungeon.entity.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import org.rexellentgames.dungeon.Display;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.file.FileReader;
import org.rexellentgames.dungeon.util.file.FileWriter;

public class Level extends Entity {
	public static final byte VERSION = 0;
	private static final String SAVE_PATH = ".ldg/save.dat";

	public static final int WIDTH = 64;
	public static final int HEIGHT = 64;
	public static final int SIZE = WIDTH * HEIGHT;

	public static final int[] NEIGHBOURS4 = {-WIDTH, +1, +WIDTH, -1};
	public static final int[] NEIGHBOURS8 = {+1, -1, +WIDTH, -WIDTH, +1 + WIDTH, +1 - WIDTH, -1 + WIDTH, -1 - WIDTH};
	public static final int[] NEIGHBOURS9 = {0, +1, -1, +WIDTH, -WIDTH, +1 + WIDTH, +1 - WIDTH, -1 + WIDTH, -1 - WIDTH};
	public static final Vector2[] NEIGHBOURS8V = {new Vector2(-1, -1), new Vector2(0, -1), new Vector2(1, -1),
		new Vector2(-1, 0), new Vector2(1, 0), new Vector2(-1, 1), new Vector2(0, 1), new Vector2(1, 1)};

	public static final Vector2[] NEIGHBOURS4V = {new Vector2(0, -1),
		new Vector2(-1, 0), new Vector2(1, 0), new Vector2(0, 1)};


	protected Vector2 spawn;
	protected short[] data;

	@Override
	public void init() {
		this.data = new short[SIZE];
		this.depth = -10;
	}

	@Override
	public void render() {
		OrthographicCamera camera = (OrthographicCamera) Camera.instance.getCamera();

		float cx = camera.position.x - Display.GAME_WIDTH / 2;
		float cy = camera.position.y - Display.GAME_HEIGHT / 2;

		int sx = (int) (Math.floor(cx / 16) - 1);
		int sy = (int) (Math.floor(cy / 16) - 1);

		int fx = (int) (Math.ceil((cx + Display.GAME_WIDTH) / 16) + 1);
		int fy = (int) (Math.ceil((cy + Display.GAME_HEIGHT) / 16) + 1);

		for (int x = Math.max(0, sx); x < Math.min(fx, WIDTH - 1); x += 2) {
			for (int y = Math.max(0, sy); y < Math.min(fy, HEIGHT - 1); y += 2) {
				if (this.isWater(x, y, false) || this.isWater(x + 1, y, false) ||
					this.isWater(x, y + 1, false) || this.isWater(x + 1, y + 1, false) ||
					this.isWater(x, y - 1, false) || this.isWater(x + 1, y - 1, false)) {

					x = (int) (Math.floor(x / 2) * 2);
					y = (int) (Math.floor(y / 2) * 2);

					Graphics.batch.draw(Graphics.tiles, x * 16, y * 16 - Dungeon.time * 6 % 32, 32, 32,
						96, 16, 32, 32, false, false);
				}
			}
		}

		for (int x = Math.max(0, sx); x < Math.min(fx, WIDTH); x++) {
			for (int y = Math.max(0, sy); y < Math.min(fy, HEIGHT); y++) {
				short tile = this.get(x, y);

				if (tile > -1) {
					Graphics.render(Graphics.tiles, tile, x * 16, y * 16);
				}
			}
		}
	}

	protected boolean isAWall(int x, int y) {
		if (x < 0 || y < 0 || x >= WIDTH || y >= HEIGHT) {
			return true;
		}

		int tile = this.get(x, y);
		int xx = tile % 32;
		int yy = (int) (Math.floor(tile / 32));

		return xx < 4 && yy < 11;
	}

	protected boolean isWater(int x, int y) {
		return this.isWater(x, y, true);
	}

	protected boolean isWater(int x, int y, boolean wall) {
		if (x < 0 || y < 0 || x >= WIDTH || y >= HEIGHT) {
			return true;
		}

		int tile = this.get(x, y);
		int xx = tile % 32;
		int yy = (int) (Math.floor(tile / 32));

		if (xx < 3 && yy > 10 && yy < 15) {
			return true;
		}

		return wall && this.isAWall(x, y);
	}

	public void setSpawn(Vector2 spawn) {
		this.spawn = spawn;
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

			this.loadData(stream);

			stream.close();
			this.addPhysics();

		} catch (Exception e) {
			Dungeon.reportException(e);
		}
	}

	protected void addPhysics() {

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

			this.writeData(stream);

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