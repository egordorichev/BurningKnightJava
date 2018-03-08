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
import org.rexellentgames.dungeon.entity.creature.Creature;
import org.rexellentgames.dungeon.entity.item.ChangableRegistry;
import org.rexellentgames.dungeon.entity.item.Item;
import org.rexellentgames.dungeon.entity.item.ItemHolder;
import org.rexellentgames.dungeon.entity.level.rooms.Room;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.Random;
import org.rexellentgames.dungeon.util.file.FileReader;
import org.rexellentgames.dungeon.util.file.FileWriter;
import org.rexellentgames.dungeon.util.geometry.Point;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class Level extends Entity {
	public static boolean GENERATED = false;

	public static final byte VERSION = 0;
	private static int WIDTH = 36;
	private static int HEIGHT = 36;
	private static int SIZE = getWIDTH() * getHEIGHT();

	public static final Vector2[] NEIGHBOURS8V = {new Vector2(-1, -1), new Vector2(0, -1), new Vector2(1, -1),
		new Vector2(-1, 0), new Vector2(1, 0), new Vector2(-1, 1), new Vector2(0, 1), new Vector2(1, 1)};

	public short[] data;
	protected boolean[] passable;
	protected boolean[] low;
	protected boolean[] free;
	protected byte[] counts;
	protected Body body;
	protected int level;
	protected ArrayList<SaveableEntity> saveable = new ArrayList<SaveableEntity>();
	protected ArrayList<SaveableEntity> playerSaveable = new ArrayList<SaveableEntity>();
	protected ArrayList<Room> rooms;

	public static int getWIDTH() {
		return WIDTH;
	}

	public static void setSize(int width, int height) {
		Level.WIDTH = width;
		Level.HEIGHT = height;
		Level.SIZE = width * (height + 1);
	}

	public short getFloor() {
		return Terrain.FLOOR;
	}

	public void fill() {
		this.data = new short[getSIZE()];

		short tile = Terrain.WALL;

		switch (Dungeon.depth) {
			case -1: tile = Terrain.EMPTY; break;
			case 0: tile = Terrain.GRASS; break;
			case 15: case 16: case 17: case 18: tile = Terrain.EMPTY;
		}

		Log.info("Filling the level with " + tile);

		for (int i = 0; i < getSIZE(); i++) {
			this.data[i] = tile;
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
			this.low[i] = !this.checkFor(i, Terrain.HIGH);
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

		float zoom = camera.zoom;

		float cx = camera.position.x - Display.GAME_WIDTH / 2 * zoom;
		float cy = camera.position.y - Display.GAME_HEIGHT / 2 * zoom;

		int sx = (int) (Math.floor(cx / 16) - 1);
		int sy = (int) (Math.floor(cy / 16) - 1);

		int fx = (int) (Math.ceil((cx + Display.GAME_WIDTH * zoom) / 16) + 1);
		int fy = (int) (Math.ceil((cy + Display.GAME_HEIGHT * zoom) / 16) + 1);

		for (int x = Math.max(0, sx); x < Math.min(fx, getWIDTH()); x++) {
			for (int y = Math.max(0, sy); y < Math.min(fy, getHEIGHT()); y++) {
				int i = x + y * getWIDTH();

				if (!this.low[i]) {
					short tile = this.get(i);

					if (tile > -1) {
						Graphics.render(Graphics.tiles, tile, x * 16, y * 16);
					}
				} else {
					byte count = this.counts[i];

					if (count != 0 && (count & (1L)) == 0) {
						Graphics.render(Graphics.tiles, 1 + count, x * 16, y * 16);
					}
				}
			}
		}
	}

	@Override
	public void render() {
		OrthographicCamera camera = Camera.instance.getCamera();

		float zoom = camera.zoom;

		float cx = camera.position.x - Display.GAME_WIDTH / 2 * zoom;
		float cy = camera.position.y - Display.GAME_HEIGHT / 2 * zoom;

		int sx = (int) (Math.floor(cx / 16) - 1);
		int sy = (int) (Math.floor(cy / 16) - 1);

		int fx = (int) (Math.ceil((cx + Display.GAME_WIDTH * zoom) / 16) + 1);
		int fy = (int) (Math.ceil((cy + Display.GAME_HEIGHT * zoom) / 16) + 1);

		for (int x = Math.max(0, sx); x < Math.min(fx, getWIDTH()); x++) {
			for (int y = Math.max(0, sy); y < Math.min(fy, getHEIGHT()); y++) {
				if (this.isWater(x, y, false)) {
					Graphics.batch.draw(Graphics.tiles, x * 16, y * 16, 144 + x % 2 * 16, Math.round(48 +
						(y % 2 * 16 - Dungeon.time * 8) % 32), 16, 16);
				}
			}
		}

		for (int x = Math.max(0, sx); x < Math.min(fx, getWIDTH()); x++) {
			for (int y = Math.max(0, sy); y < Math.min(fy, getHEIGHT()); y++) {
				int i = x + y * getWIDTH();

				if (this.low[i]) {
					short tile = this.get(i);

					if (tile > -1) {
						Graphics.render(Graphics.tiles, tile, x * 16, y * 16);
					}
				}
				byte count = this.counts[i];

				if (count != 0 && (count & (1L)) != 0) {
					Graphics.render(Graphics.tiles, 1 + count, x * 16, y * 16);
				}
			}
		}
	}

	public boolean isAWall(int x, int y) {
		if (x < 0 || y < 0 || x >= getWIDTH() || y >= getHEIGHT()) {
			return true;
		}

		return this.get(x, y) == Terrain.WALL;
	}

	public boolean isWater(int x, int y) {
		return this.isWater(x, y, true);
	}

	public boolean checkFor(int i, int flag) {
		return (Terrain.flags[this.get(i)] & flag) == flag;
	}

	public boolean checkFor(int x, int y, int flag) {
		return (Terrain.flags[this.get(x, y)] & flag) == flag;
	}

	public boolean isWaterOrFall(int x, int y) {
		if (x < 0 || y < 0 || x >= getWIDTH() || y >= getHEIGHT()) {
			return true;
		}

		int tile = this.get(x, y);

		if ((tile == Terrain.FALL || tile == Terrain.WATER_FALL || tile == Terrain.EMPTY) && this.checkFor(x, y + 1, Terrain.PASSABLE)) {
			return true;
		}

		int xx = tile % 32;
		int yy = (int) (Math.floor(tile / 32));

		return (xx > 16 && yy == 0);
	}

	public boolean isWater(int x, int y, boolean wall) {
		if (x < 0 || y < 0 || x >= getWIDTH() || y >= getHEIGHT()) {
			return true;
		}

		int tile = this.get(x, y);

		if (tile == Terrain.WATER || (wall && tile == Terrain.WOOD)) {
			return true;
		}

		int xx = tile % 32;
		int yy = (int) (Math.floor(tile / 32));

		if (xx > 16 && yy == 0) {
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
			return ".ldg/depth" + this.level + ".save";
		}

		return ".ldg/player.save";
	}

	public void addPhysics() {
		if (body != null) {
			body.getWorld().destroyBody(body);
		}

		BodyDef def = new BodyDef();
		def.type = BodyDef.BodyType.StaticBody;

		body = Dungeon.world.createBody(def);

		ArrayList<Vector2> marked = new ArrayList<Vector2>();

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


						if (this.checkFor(x, y + 1, Terrain.SOLID)) {
							poly.set(new Vector2[]{
								new Vector2(xx, yy), new Vector2(xx + 16, yy),
								new Vector2(xx, yy + 16), new Vector2(xx + 16, yy + 16)
							});
						} else {
							poly.set(new Vector2[]{
								new Vector2(xx, yy), new Vector2(xx + 16, yy),
								new Vector2(xx, yy + 12), new Vector2(xx + 16, yy + 12)
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

		Log.info("Loading " + type.toString().toLowerCase() + "...");

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
			}

			this.loadData(stream, type);

			stream.close();
		} catch (Exception e) {
			Dungeon.reportException(e);
		}
	}

	public void save(DataType type) {
		FileHandle save = Gdx.files.external(this.getSavePath(type));
		Log.info("Saving " + type.toString().toLowerCase() + "...");

		try {
			FileWriter stream = new FileWriter(save.file().getAbsolutePath());

			stream.writeByte(VERSION);

			if (type == Level.DataType.LEVEL) {
				stream.writeInt32(getWIDTH());
				stream.writeInt32(getHEIGHT());

				for (int i = 0; i < getSIZE(); i++) {
					stream.writeInt16(this.data[i]);
				}
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

	public void tile() {
		this.counts = new byte[getSIZE()];

		for (int x = 0; x < getWIDTH(); x++) {
			for (int y = 0; y < getHEIGHT(); y++) {
				if (this.get(x, y) != Terrain.WALL) {
					int count = 0;

					if (this.check(x, y + 1)) {
						count += 1;
					}

					if (this.check(x + 1, y)) {
						count += 2;
					}

					if (this.check(x, y - 1)) {
						count += 4;
					}

					if (this.check(x - 1, y)) {
						count += 8;
					}

					this.counts[toIndex(x, y)] = (byte) count;
				}
			}
		}
	}

	private boolean check(int x, int y) {
		if (x < 0 || y < 0 || x >= getWIDTH() || y >= getHEIGHT()) {
			return false;
		}

		return this.get(x, y) == Terrain.WALL;
	}

	protected Room getRandomRoom(Room.Type type) {
		for (int i = 0; i < 30; i++) {
			Room room = this.rooms.get(Random.newInt(this.rooms.size()));

			if (room.getType() == type) {
				return room;
			}
		}

		return null;
	}

	protected Point getRandomFreePoint(Room.Type type) {
		for (int i = 0; i < 10; i++) {
			Room room = this.getRandomRoom(type);

			if (room == null) {
				continue;
			}

			for (int j = 0; j < 100; j++) {
				Point point = room.getRandomCell();
				int in = (int) (point.x + point.y * getWIDTH());

				if (!this.free[in]) {
					this.free[in] = true;
					return point;
				}
			}
		}

		return null;
	}

	public void spawnItems() {
		ArrayList<Item> items = this.generateItems();

		for (Item item : items) {
			Point point = null;

			while (point == null) {
				point = this.getRandomFreePoint(Room.Type.REGULAR);
			}

			ItemHolder holder = new ItemHolder();

			holder.setItem(item);
			holder.x = point.x * 16;
			holder.y = point.y * 16;

			this.addSaveable(holder);
			this.area.add(holder);
		}
	}

	public void spawnCreatures() {
		ArrayList<Creature> creatures = this.generateCreatures();

		for (Creature creature : creatures) {
			Point point = null;

			while (point == null) {
				point = this.getRandomFreePoint(Room.Type.REGULAR);
			}

			creature.x = point.x * 16;
			creature.y = point.y * 16;

			this.addSaveable(creature);
			this.area.add(creature);
		}
	}

	protected ArrayList<Item> generateItems() {
		return new ArrayList<Item>();
	}

	protected ArrayList<Creature> generateCreatures() {
		return new ArrayList<Creature>();
	}

	public enum DataType {
		LEVEL,
		PLAYER
	}
}