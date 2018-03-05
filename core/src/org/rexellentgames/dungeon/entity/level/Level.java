package org.rexellentgames.dungeon.entity.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import org.rexellentgames.dungeon.Display;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.item.ChangableRegistry;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.file.FileReader;
import org.rexellentgames.dungeon.util.file.FileWriter;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class Level extends Entity {
	public static final byte VERSION = 0;
	private static int WIDTH = 36;
	private static int HEIGHT = 36;
	private static int SIZE = getWIDTH() * getHEIGHT();

	public static final int[] NEIGHBOURS4 = {-getWIDTH(), +1, +getWIDTH(), -1};
	public static final int[] NEIGHBOURS8 = {+1, -1, +getWIDTH(), -getWIDTH(), +1 + getWIDTH(), +1 - getWIDTH(), -1 + getWIDTH(), -1 - getWIDTH()};
	public static final int[] NEIGHBOURS9 = {0, +1, -1, +getWIDTH(), -getWIDTH(), +1 + getWIDTH(), +1 - getWIDTH(), -1 + getWIDTH(), -1 - getWIDTH()};
	public static final Vector2[] NEIGHBOURS8V = {new Vector2(-1, -1), new Vector2(0, -1), new Vector2(1, -1),
		new Vector2(-1, 0), new Vector2(1, 0), new Vector2(-1, 1), new Vector2(0, 1), new Vector2(1, 1)};

	public static final Vector2[] NEIGHBOURS4V = {new Vector2(0, -1),
		new Vector2(-1, 0), new Vector2(1, 0), new Vector2(0, 1)};


	public short[] data;
	protected boolean[] passable;
	protected boolean[] low;
	protected Body body;
	protected int level;
	protected ArrayList<SaveableEntity> saveable = new ArrayList<SaveableEntity>();
	protected ArrayList<SaveableEntity> playerSaveable = new ArrayList<SaveableEntity>();

	public static int getWIDTH() {
		return WIDTH;
	}

	public static void setSize(int width, int height) {
		Level.WIDTH = width;
		Level.HEIGHT = height;
		Level.SIZE = width * (height + 1);
	}

	public void fill() {
		this.data = new short[getSIZE()];

		for (int i = 0; i < getSIZE(); i++) {
			this.data[i] = Terrain.WALL;
		}
	}

	public static int getHEIGHT() {
		return HEIGHT;
	}

	public static int getSIZE() {
		return SIZE;
	}

	public void addSaveable(SaveableEntity entity) {
		this.saveable.add(entity);
	}

	public void removeSaveable(SaveableEntity entity) {
		this.saveable.remove(entity);
	}

	public void addPlayerSaveable(SaveableEntity entity) {
		this.playerSaveable.add(entity);
	}

	public void removePlayerSaveable(SaveableEntity entity) {
		this.playerSaveable.remove(entity);
	}

	public void loadPassable() {
		this.passable = new boolean[getSIZE()];
		this.low = new boolean[getSIZE()];

		for (int i = 0; i < getSIZE(); i++) {
			this.passable[i] = this.checkFor(i, Terrain.PASSABLE);
			this.low[i] = this.checkFor(i, Terrain.LOW);
		}
	}

	public static int toIndex(int x, int y) {
		return x + y * getWIDTH();
	}

	public boolean[] getPassable() {
		return this.passable;
	}

	@Override
	public void init() {
		this.alwaysActive = true;
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

		for (int x = Math.max(0, sx); x < Math.min(fx, getWIDTH()); x++) {
			for (int y = Math.max(0, sy); y < Math.min(fy, getHEIGHT()); y++) {
				if (!this.low[x + y * getWIDTH()]) {
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
		Rectangle clipBounds = new Rectangle(0, 0, Level.getWIDTH() * 16, Level.getHEIGHT() * 16);
		ScissorStack.calculateScissors(camera, Graphics.batch.getTransformMatrix(), clipBounds, scissors);
		ScissorStack.pushScissors(scissors);

		float m = Dungeon.time * 5 % 32;

		for (int x = Math.max(0, sx); x < Math.min(fx, getWIDTH()); x++) {
			for (int y = Math.max(0, sy); y < Math.min(fy, getHEIGHT()); y++) {
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

		for (int x = Math.max(0, sx); x < Math.min(fx, getWIDTH()); x++) {
			for (int y = Math.max(0, sy); y < Math.min(fy, getHEIGHT()); y++) {
				if (this.low[x + y * getWIDTH()]) {
					short tile = this.get(x, y);

					if (tile > -1) {
						Graphics.render(Graphics.tiles, tile, x * 16, y * 16);
					}
				}
			}
		}
	}

	protected boolean isAWall(int x, int y) {
		if (x < 0 || y < 0 || x >= getWIDTH() || y >= getHEIGHT()) {
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
		if (x < 0 || y < 0 || x >= getWIDTH() || y >= getHEIGHT()) {
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
		if (x < 0 || y < 0 || x >= getWIDTH() || y >= getHEIGHT()) {
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

	public abstract void generate();

	public String getSavePath(DataType type) {
		if (type == DataType.LEVEL) {
			return ".ldg/save" + this.level + ".dat";
		}

		return ".ldg/player.dat";
	}

	public void addPhysics() {
		if (body != null) {
			body.getWorld().destroyBody(body);
		}

		BodyDef def = new BodyDef();
		def.type = BodyDef.BodyType.StaticBody;

		body = Dungeon.world.createBody(def);

		ArrayList<Vector2> marked = new ArrayList<Vector2>();

		Log.info(getWIDTH() + " " + getHEIGHT() + " is the size");

		for (int x = 0; x < getWIDTH(); x++) {
			for (int y = 0; y < getHEIGHT(); y++) {
				if (this.checkFor(x, y, Terrain.SOLID)) {
					int total = 0;

					for (Vector2 vec : NEIGHBOURS8V) {
						Vector2 v = new Vector2(x + vec.x, y + vec.y);

						if (v.x >= 0 && v.y >= 0 && v.x < getWIDTH() && v.y < getWIDTH()) {
							if (this.checkFor((int) v.x, (int) v.y, Terrain.SOLID)) {
								total++;
							}
						}
					}

					if (total < 8) {
						PolygonShape poly = new PolygonShape();
						int xx = x * 16;
						int yy = y * 16;


						if (yy == 0 || this.checkFor(x, y - 1, Terrain.SOLID)) {
							poly.set(new Vector2[]{
								new Vector2(xx, yy), new Vector2(xx + 16, yy),
								new Vector2(xx, yy + 16), new Vector2(xx + 16, yy + 16)
							});
						} else {
							poly.set(new Vector2[]{
								new Vector2(xx, yy + 8), new Vector2(xx + 16, yy + 8),
								new Vector2(xx, yy + 16), new Vector2(xx + 16, yy + 16)
							});
						}

						FixtureDef fixture = new FixtureDef();

						fixture.shape = poly;
						fixture.friction = 0;

						body.createFixture(fixture);

						poly.dispose();

						if (this.get(x, y) == Terrain.WALL) {
							marked.add(new Vector2(x, y));
						}
					}
				}
			}
		}
	}

	public void load(DataType type) {
		FileHandle save = Gdx.files.external(this.getSavePath(type));

		if (!save.exists()) {
			if (type == DataType.LEVEL) {
				this.generate();
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
				setSize(stream.readInt32(), stream.readInt32());
				this.data = new short[getSIZE()];

				for (int i = 0; i < getSIZE(); i++) {
					this.data[i] = stream.readInt16();
				}
			} else {
				Dungeon.depth = stream.readInt32();
				this.level = Dungeon.depth;
			}

			this.loadData(stream, type);

			stream.close();
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
				stream.writeInt32(getWIDTH());
				stream.writeInt32(getHEIGHT());

				for (int i = 0; i < getSIZE(); i++) {
					stream.writeInt16(this.data[i]);
				}
			} else {
				stream.writeInt32(Dungeon.depth); // Probably updated, if going up or down
			}

			this.writeData(stream, type);
			stream.close();
		} catch (Exception e) {
			Dungeon.reportException(e);
		}
	}

	protected void loadData(FileReader stream, DataType type) throws Exception {
		if (type == DataType.PLAYER) {
			ChangableRegistry.load(stream);

			int count = stream.readInt32();

			for (int i = 0; i < count; i++) {
				String t = stream.readString();

				Class<?> clazz = Class.forName(t);
				Constructor<?> constructor = clazz.getConstructor();
				Object object = constructor.newInstance(new Object[]{});

				SaveableEntity entity = (SaveableEntity) object;

				this.area.add(entity);
				this.playerSaveable.add(entity);

				entity.load(stream);
			}
		} else {
			int count = stream.readInt32();

			for (int i = 0; i < count; i++) {
				String t = stream.readString();

				Class<?> clazz = Class.forName(t);
				Constructor<?> constructor = clazz.getConstructor();
				Object object = constructor.newInstance(new Object[]{});

				SaveableEntity entity = (SaveableEntity) object;

				this.area.add(entity);
				this.saveable.add(entity);

				entity.load(stream);
			}
		}
	}

	public ArrayList<SaveableEntity> getPlayerSaveable() {
		return this.playerSaveable;
	}

	protected void writeData(FileWriter stream, DataType type) throws Exception {
		if (type == DataType.PLAYER) {
			ChangableRegistry.save(stream);

			stream.writeInt32(this.playerSaveable.size());

			for (int i = 0; i < this.playerSaveable.size(); i++) {
				SaveableEntity entity = this.playerSaveable.get(i);

				stream.writeString(entity.getClass().getName());
				entity.save(stream);
			}
		} else {
			stream.writeInt32(this.saveable.size());

			for (int i = 0; i < this.saveable.size(); i++) {
				SaveableEntity entity = this.saveable.get(i);

				stream.writeString(entity.getClass().getName());
				entity.save(stream);
			}
		}
	}

	public enum DataType {
		LEVEL,
		PLAYER
	}
}