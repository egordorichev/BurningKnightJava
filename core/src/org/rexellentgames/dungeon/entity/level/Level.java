package org.rexellentgames.dungeon.entity.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import org.rexellentgames.dungeon.Display;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.file.FileReader;
import org.rexellentgames.dungeon.util.file.FileWriter;

import java.io.File;
import java.io.IOException;

public class Level extends Entity {
	public static final byte VERSION = 0;
	public static final int WIDTH = 36;
	public static final int HEIGHT = 36;
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
	protected boolean[] passable;
	protected boolean[] low;
	protected int level;

	public static int toIndex(int x, int y) {
		return x + y * WIDTH;
	}

	public boolean[] getPassable() {
		return passable;
	}

	@Override
	public void init() {
		this.alwaysActive = true;
		this.data = new short[SIZE];
		this.depth = -10;
		this.level = Dungeon.depth;

		SolidLevel l = new SolidLevel();
		l.setLevel(this);

		this.area.add(l);
	}

	public void renderSolid() {
		OrthographicCamera camera = Camera.instance.getCamera();

		float cx = camera.position.x - Display.GAME_WIDTH / 2;
		float cy = camera.position.y - Display.GAME_HEIGHT / 2;

		int sx = (int) (Math.floor(cx / 16) - 1);
		int sy = (int) (Math.floor(cy / 16) - 1);

		int fx = (int) (Math.ceil((cx + Display.GAME_WIDTH) / 16) + 1);
		int fy = (int) (Math.ceil((cy + Display.GAME_HEIGHT) / 16) + 1);

		for (int x = Math.max(0, sx); x < Math.min(fx, WIDTH); x++) {
			for (int y = Math.max(0, sy); y < Math.min(fy, HEIGHT); y++) {
				if (!this.low[x + y * WIDTH]) {
					short tile = this.get(x, y);

					if (tile > -1) {
						Graphics.render(Graphics.tiles, tile, x * 16, y * 16);
					}
				}
			}
		}
	}

	@Override
	public void render() {
		OrthographicCamera camera = Camera.instance.getCamera();

		float cx = camera.position.x - Display.GAME_WIDTH / 2;
		float cy = camera.position.y - Display.GAME_HEIGHT / 2;

		int sx = (int) (Math.floor(cx / 16) - 1);
		int sy = (int) (Math.floor(cy / 16) - 1);

		int fx = (int) (Math.ceil((cx + Display.GAME_WIDTH) / 16) + 1);
		int fy = (int) (Math.ceil((cy + Display.GAME_HEIGHT) / 16) + 1);

		Rectangle scissors = new Rectangle();
		Rectangle clipBounds = new Rectangle(0, 0, Level.WIDTH * 16, Level.HEIGHT * 16);
		ScissorStack.calculateScissors(camera, Graphics.batch.getTransformMatrix(), clipBounds, scissors);
		ScissorStack.pushScissors(scissors);

		float m = Dungeon.time * 5 % 32;

		for (int x = Math.max(0, sx); x < Math.min(fx, WIDTH); x++) {
			for (int y = Math.max(0, sy); y < Math.min(fy, HEIGHT); y++) {
				if (this.isWater(x, y, false)) {
					Graphics.render(Graphics.tiles, 38 + x % 2 + y % 2 * 32, x * 16, y * 16 - m);
					Graphics.render(Graphics.tiles, 38 + x % 2 + (y + 1) % 2 * 32, x * 16, y * 16 - m + 16);
					Graphics.render(Graphics.tiles, 38 + x % 2 + y % 2 * 32, x * 16, y * 16 - m + 32);
					Graphics.render(Graphics.tiles, 38 + x % 2 + (y + 1) % 2 * 32, x * 16, y * 16 - m + 48);
				}
			}
		}

		Graphics.batch.flush();
		ScissorStack.popScissors();

		for (int x = Math.max(0, sx); x < Math.min(fx, WIDTH); x++) {
			for (int y = Math.max(0, sy); y < Math.min(fy, HEIGHT); y++) {
				if (this.low[x + y * WIDTH]) {
					short tile = this.get(x, y);

					if (tile > -1) {
						Graphics.render(Graphics.tiles, tile, x * 16, y * 16);
					}
				}
			}
		}
	}

	protected boolean isAWall(int x, int y) {
		if (x < 0 || y < 0 || x >= WIDTH || y >= HEIGHT) {
			return true;
		}

		int tile = this.get(x, y);

		if (tile == Terrain.DECO) {
			return true;
		}

		int xx = tile % 32;
		int yy = (int) (Math.floor(tile / 32));

		return xx < 4 && yy < 11;
	}

	protected boolean isWater(int x, int y) {
		return this.isWater(x, y, true);
	}

	public boolean checkFor(int i, int flag) {
		return (Terrain.flags[this.get(i)] & flag) == flag;
	}

	public boolean checkFor(int x, int y, int flag) {
		return (Terrain.flags[this.get(x, y)] & flag) == flag;
	}

	protected boolean isWaterOrFall(int x, int y) {
		if (x < 0 || y < 0 || x >= WIDTH || y >= HEIGHT) {
			return true;
		}

		int tile = this.get(x, y);

		if ((tile == Terrain.FALL || tile == Terrain.WATER_FALL || tile == Terrain.EMPTY) && this.checkFor(x, y + 1, Terrain.PASSABLE)) {
			return true;
		}

		int xx = tile % 32;
		int yy = (int) (Math.floor(tile / 32));

		if (xx < 4 && yy > 10 && yy < 15) {
			return true;
		}

		return false;
	}

	protected boolean isWater(int x, int y, boolean wall) {
		if (x < 0 || y < 0 || x >= WIDTH || y >= HEIGHT) {
			return true;
		}

		int tile = this.get(x, y);

		int xx = tile % 32;
		int yy = (int) (Math.floor(tile / 32));

		if (xx < 4 && yy > 10 && yy < 15) {
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

	public boolean generate() {
		return false;
	}

	public void generateUntilGood() {
		Log.info("Generating...");

		while (!this.generate()) {
			Log.error("Failed to generate the level!");
		}
	}

	public String getSavePath(DataType type) {
		if (type == DataType.LEVEL) {
			return ".ldg/save" + this.level + ".dat";
		}

		return ".ldg/player.dat";
	}

	protected void addPhysics() {

	}

	public void load(DataType type) {
		FileHandle save = Gdx.files.external(this.getSavePath(type));

		if (!save.exists()) {
			Dungeon.loaded = false;

			if (type == DataType.LEVEL) {
				generateUntilGood();
				return;
			} else {
				File file = save.file();
				file.getParentFile().mkdirs();

				try {
					file.createNewFile();
					return;
				} catch (IOException e) {
					Dungeon.reportException(e);
				}
			}
		} else {
			Dungeon.loaded = true;
		}

		Log.info("Loading... " + type.toString());

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

			if (type == DataType.LEVEL) {
				for (int i = 0; i < SIZE; i++) {
					this.data[i] = stream.readInt16();
				}
			}

			this.loadData(stream, type);

			stream.close();

			if (type == Level.DataType.LEVEL) {
				this.addPhysics();
			}
		} catch (Exception e) {
			Dungeon.reportException(e);
		}
	}

	public void save(DataType type) {
		FileHandle save = Gdx.files.external(this.getSavePath(type));
		Log.info("Saving... " + type.toString());

		try {
			FileWriter stream = new FileWriter(save.file().getAbsolutePath());

			stream.writeByte(VERSION);

			if (type == Level.DataType.LEVEL) {
				for (int i = 0; i < SIZE; i++) {
					stream.writeInt16(this.data[i]);
				}
			}

			this.writeData(stream, type);
			stream.close();
		} catch (Exception e) {
			Dungeon.reportException(e);
		}
	}

	protected void writeData(FileWriter stream, DataType type) throws Exception {

	}

	protected void loadData(FileReader stream, DataType type) throws Exception {

	}

	public enum DataType {
		LEVEL,
		PLAYER
	}
}