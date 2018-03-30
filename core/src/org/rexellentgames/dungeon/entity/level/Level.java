package org.rexellentgames.dungeon.entity.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import org.rexellentgames.dungeon.Display;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.creature.Creature;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.item.ChangableRegistry;
import org.rexellentgames.dungeon.entity.item.Item;
import org.rexellentgames.dungeon.entity.item.ItemHolder;
import org.rexellentgames.dungeon.entity.level.entities.Entrance;
import org.rexellentgames.dungeon.entity.level.entities.fx.ChasmFx;
import org.rexellentgames.dungeon.entity.level.levels.*;
import org.rexellentgames.dungeon.entity.level.rooms.Room;
import org.rexellentgames.dungeon.entity.level.rooms.regular.RegularRoom;
import org.rexellentgames.dungeon.entity.level.rooms.regular.ladder.EntranceRoom;
import org.rexellentgames.dungeon.entity.level.rooms.regular.ladder.ExitRoom;
import org.rexellentgames.dungeon.net.Network;
import org.rexellentgames.dungeon.util.Line;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.Random;
import org.rexellentgames.dungeon.util.file.FileReader;
import org.rexellentgames.dungeon.util.file.FileWriter;
import org.rexellentgames.dungeon.util.geometry.Point;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;

public abstract class Level extends Entity {
	public static final byte VERSION = 0;
	public static final Vector2[] NEIGHBOURS8V = {new Vector2(-1, -1), new Vector2(0, -1), new Vector2(1, -1),
		new Vector2(-1, 0), new Vector2(1, 0), new Vector2(-1, 1), new Vector2(0, 1), new Vector2(1, 1)};
	public static boolean GENERATED = false;
	public static float heat;

	public static String[] COMPASS = {
		"NESW", "ESW", "NSW", "SW", "NEW", "EW", "NW", "W",
		"NES", "ES", "NS", "S", "NE", "E", "N"
	};

	private static int WIDTH = 36;
	private static int HEIGHT = 36;
	private static int SIZE = getWidth() * getHeight();
	public byte[] data;
	protected byte[] variants;
	protected byte[] walls;
	protected float[] light;
	protected float[] lightR;
	protected float[] lightG;
	protected float[] lightB;
	protected boolean[] passable;
	protected boolean[] low;
	protected boolean[] free;
	protected Body body;
	protected int level;
	protected ArrayList<SaveableEntity> saveable = new ArrayList<SaveableEntity>();
	protected ArrayList<SaveableEntity> playerSaveable = new ArrayList<SaveableEntity>();
	protected ArrayList<Room> rooms;

	public static int getWidth() {
		return WIDTH;
	}

	public static void setWidth(int WIDTH) {
		Level.WIDTH = WIDTH;
		Level.SIZE = Level.WIDTH * Level.HEIGHT;
	}

	public static void setSize(int width, int height) {
		Level.WIDTH = width;
		Level.HEIGHT = height;
		Level.SIZE = width * (height + 1);
	}

	public static int getHeight() {
		return HEIGHT;
	}

	public static void setHeight(int h) {
		Level.HEIGHT = h;
		Level.SIZE = Level.WIDTH * Level.HEIGHT;
	}

	public static int getSIZE() {
		return SIZE;
	}

	public static int toIndex(int x, int y) {
		return x + y * getWidth();
	}

	public static BetterLevel forDepth(int depth) {
		switch (depth) {
			case -1:
				return new SkyLevel();
			case 0:
			case 1:
			case 2:
			case 3:
			default:
				return new HallLevel();
			case 4:
				return new HallBossLevel();
			case 5:
			case 6:
			case 7:
			case 8:
				return new StorageLevel();
			// todo: case 9: boss level
			case 10:
			case 11:
			case 12:
			case 13:
				return new PrisonLevel();
			// todo: case 14: boss level
			case 15:
			case 16:
			case 17:
			case 18:
				return new LibraryLevel();
			// todo: case 19: boss level
			case 20:
			case 21:
			case 22:
			case 23:
				return new HellLevel();
			// todo: case 24: THE FINAL BOSS LEVEL
		}
	}

	public void initLight() {
		this.light = new float[getSIZE()];
		this.lightR = new float[getSIZE()];
		this.lightG = new float[getSIZE()];
		this.lightB = new float[getSIZE()];
	}

	public void fill() {
		this.data = new byte[getSIZE()];

		this.initLight();

		byte tile = Terrain.WALL;

		switch (Dungeon.depth) {
			case -1:
				tile = Terrain.CHASM;
				break;
			case 0:
				tile = Terrain.DIRT;
				break;
			case 15:
			case 16:
			case 17:
			case 18:
				tile = Terrain.CHASM;
		}

		Log.info("Filling the level with " + tile);

		for (int i = 0; i < getSIZE(); i++) {
			this.data[i] = tile;
		}
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
		this.variants = new byte[getSIZE()];
		this.walls = new byte[getSIZE()];

		for (int i = 0; i < getSIZE(); i++) {
			this.passable[i] = this.checkFor(i, Terrain.PASSABLE);
			this.low[i] = !this.checkFor(i, Terrain.HIGH);
		}

		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				byte tile = this.get(x, y);

				if (tile == Terrain.CHASM) {
					this.tileUp(x, y, tile, false);
				} else if (tile == Terrain.WATER) {
					this.tileUp(x, y, tile, false);
				} else if (tile == Terrain.DIRT || tile == Terrain.PLANTED_DIRT) {
					this.tileUp(x, y, Terrain.IS_DIRT, true);
				} else if (tile == Terrain.FLOOR || tile == Terrain.WOOD) {
					this.makeFloor(x, y, tile);
				}

				byte count = 0;

				if (!this.shouldTile(x, y + 1, Terrain.WALL, false)) {
					count += 1;
				}

				if (!this.shouldTile(x + 1, y, Terrain.WALL, false)) {
					count += 2;
				}

				if (!this.shouldTile(x, y - 1, Terrain.WALL, false)) {
					count += 4;
				}

				if (!this.shouldTile(x - 1, y, Terrain.WALL, false)) {
					count += 8;
				}

				this.walls[toIndex(x, y)] = count;
			}
		}
	}

	private void makeFloor(int x, int y, int tile) {
		int i = toIndex(x, y);

		if (this.variants[i] != 0) {
			return;
		}

		byte var = (byte) Random.newInt(0, 11);

		if (var == 9 || var == 10) {
			for (int xx = x; xx < x + 2; xx++) {
				for (int yy = y; yy < y + 2; yy++) {
					if (this.get(xx, yy) != tile || this.variants[toIndex(xx, yy)] != 0) {
						var = (byte) Random.newInt(0, 8);
						break;
					}
				}
			}
		}

		if (var == 9) {
			this.variants[toIndex(x, y)] = 10;
			this.variants[toIndex(x + 1, y)] = 11;
			this.variants[toIndex(x, y + 1)] = 8;
			this.variants[toIndex(x + 1, y + 1)] = 9;
		} else if (var == 10) {
			this.variants[toIndex(x, y)] = 14;
			this.variants[toIndex(x + 1, y)] = 15;
			this.variants[toIndex(x, y + 1)] = 12;
			this.variants[toIndex(x + 1, y + 1)] = 13;
		} else {
			this.variants[i] = var;
		}
	}

	private void tileUp(int x, int y, int tile, boolean flag) {
		byte count = 0;

		if (this.shouldTile(x, y + 1, tile, flag)) {
			count += 1;
		}

		if (this.shouldTile(x + 1, y, tile, flag)) {
			count += 2;
		}

		if (this.shouldTile(x, y - 1, tile, flag)) {
			count += 4;
		}

		if (this.shouldTile(x - 1, y, tile, flag)) {
			count += 8;
		}

		this.variants[toIndex(x, y)] = count;
	}

	private boolean shouldTile(int x, int y, int tile, boolean flag) {
		if (!this.isValid(x, y)) {
			return false;
		}

		byte t = this.get(x, y);

		if (flag) {
			return this.checkFor(x, y, tile) || t == Terrain.WALL;
		} else {
			return t == tile || t == Terrain.WALL;
		}
	}

	public boolean[] getPassable() {
		return this.passable;
	}

	@Override
	public void init() {
		this.alwaysRender = true;
		this.depth = -10;
		this.level = Dungeon.depth;

		SolidLevel l = new SolidLevel();
		l.setLevel(this);

		this.area.add(l);
	}

	public void renderLight() {
		OrthographicCamera camera = Camera.instance.getCamera();

		Graphics.batch.end();

		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Graphics.shape.begin(ShapeRenderer.ShapeType.Filled);

		float zoom = camera.zoom;

		float cx = camera.position.x - Display.GAME_WIDTH / 2 * zoom;
		float cy = camera.position.y - Display.GAME_HEIGHT / 2 * zoom;

		int sx = (int) (Math.floor(cx / 16) - 1);
		int sy = (int) (Math.floor(cy / 16) - 1);

		int fx = (int) (Math.ceil((cx + Display.GAME_WIDTH * zoom) / 16) + 1);
		int fy = (int) (Math.ceil((cy + Display.GAME_HEIGHT * zoom) / 16) + 1);

		float dt = Gdx.graphics.getDeltaTime() / 5;

		for (int i = 0; i < this.getSIZE(); i++) {
			float v = this.light[i];

			if (v > 0) {
				this.light[i] = Math.max(0, v - dt);
				this.lightR[i] = Math.max(0, this.lightR[i] - dt);
				this.lightG[i] = Math.max(0, this.lightG[i] - dt);
				this.lightB[i] = Math.max(0, this.lightB[i] - dt);
			}

		}

		for (int x = Math.max(0, sx); x < Math.min(fx, getWidth()); x++) {
			for (int y = Math.max(0, sy); y < Math.min(fy, getHeight()); y++) {
				int i = x + y * getWidth();
				float v = this.light[i];
				float r = this.lightR[i];
				float g = this.lightG[i];
				float b = this.lightB[i];

				Graphics.shape.setColor(r, g, b, 1f - v);
				Graphics.shape.rect(x * 16, y * 16 - 8, 16, 16);
			}
		}

		Graphics.shape.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
		Graphics.batch.begin();
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

		for (int x = Math.max(0, sx); x < Math.min(fx, getWidth()); x++) {
			for (int y = Math.max(0, sy); y < Math.min(fy, getHeight()); y++) {
				int i = x + y * getWidth();

				if (!this.low[i]) {
					byte tile = this.get(i);

					if (tile > 0 && Terrain.patterns[tile] != null) {
						TextureRegion region = new TextureRegion(Terrain.patterns[tile]);

						region.setRegionX(region.getRegionX() + x % 4 * 16);
						region.setRegionY(region.getRegionY() + (3 - (y % 4)) * 16);
						region.setRegionWidth(16);
						region.setRegionHeight(16);

						Graphics.render(region, x * 16, y * 16);
					}
				} else {
					byte v = this.walls[i];

					if (v != 15 && v % 2 == 1) {
						Graphics.render(Terrain.wallVariants[v], x * 16, y * 16);
					}
				}
			}
		}

		// Usefull room debug

		/*
		Graphics.batch.end();
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Graphics.shape.setColor(1, 1, 1, 0.1f);
		Graphics.shape.begin(ShapeRenderer.ShapeType.Filled);
		for (Room room : this.rooms) {
			Graphics.shape.rect(room.left * 16, room.top * 16, room.getWidth() * 16, room.getHeight() * 16);
		}
		Graphics.shape.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
		Graphics.batch.begin();*/
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

		for (int x = Math.max(0, sx); x < Math.min(fx, getWidth()); x++) {
			for (int y = Math.max(0, sy); y < Math.min(fy, getHeight()); y++) {
				int i = x + y * getWidth();

				if (this.low[i]) {
					byte tile = this.get(i);

					if (tile > 0 && Terrain.patterns[tile] != null) {
						TextureRegion region = new TextureRegion(Terrain.patterns[tile]);

						region.setRegionX(region.getRegionX() + x % 4 * 16);
						region.setRegionY(region.getRegionY() + (3 - y % 4) * 16);
						region.setRegionWidth(16);
						region.setRegionHeight(16);

						Graphics.render(region, x * 16, y * 16 - 8);
					}

					if (Terrain.variants[tile] != null) {
						byte variant = this.variants[i];

						if (variant != Terrain.variants[tile].length && Terrain.variants[tile][variant] != null) {
							Graphics.render(Terrain.variants[tile][variant], x * 16, y * 16 - 8);
						}
					}

					byte v = this.walls[i];

					if (v != 15 && v % 2 == 0) {
						Graphics.render(Terrain.wallVariants[v], x * 16, y * 16);
					}

					if (tile == Terrain.CHASM && Random.chance(0.4f)) {
						Dungeon.area.add(new ChasmFx(Random.newFloat(1f) * 16 + x * 16, Random.newFloat(1f) * 16 + y * 16 - 8));
					}
				}
			}
		}
	}

	public byte getVariant(int x, int y) {
		return this.variants[toIndex(x, y)];
	}

	public void addLight(float x, float y, float r, float g, float b, float a, float max) {
		float dt = Gdx.graphics.getDeltaTime();
		int i = (int) (Math.floor(x / 16) + Math.floor(y / 16) * getWidth());

		if (i < 0 || i >= this.getSIZE()) {
			return;
		}

		if (this.light[i] < max) {
			this.light[i] = this.light[i] + a * dt;
		}

		if (this.lightR[i] < max) {
			this.lightR[i] = this.lightR[i] + r * dt;
		}

		if (this.lightG[i] < max) {
			this.lightG[i] = this.lightG[i] + g * dt;
		}

		if (this.lightB[i] < max) {
			this.lightB[i] = this.lightB[i] + b * dt;
		}
	}

	public float getLight(int i) {
		if (i < 0 || i >= getSIZE()) {
			return 0;
		}

		return this.light[i];
	}

	public float getLight(int x, int y) {
		int i = toIndex(x, y);

		if (i < 0 || i >= getSIZE()) {
			return 0;
		}

		return this.light[i];
	}

	public void addLightInRadius(float x, float y, float r, float g, float b, float a, float rd, boolean xray) {
		int fx = (int) Math.floor((x) / 16);
		int fy = (int) Math.floor((y) / 16);

		if (fx < 0 || fy < 0) {
			return;
		}

		for (int xx = (int) -rd; xx <= rd; xx++) {
			for (int yy = (int) -rd; yy <= rd; yy++) {
				if (xx + fx < 0 || yy + fy < 0 || xx + fx >= Level.getWidth() || yy + fy >= Level.getHeight()) {
					continue;
				}

				float d = (float) Math.sqrt(xx * xx + yy * yy);

				if (d < rd) {
					boolean see = xray;
					float v = 1;

					if (!see) {
						byte vl = this.canSee(fx, fy, fx + xx, fy + yy);

						if (vl == 1 && yy >= 0) {
							v = 0.5f;
							see = true;
						} else if (vl == 0) {
							see = true;
						}
					}

					if (see) {
						Dungeon.level.addLight(x + xx * 16, y + yy * 16, r, g, b, a, (rd - d) / rd * v);
					}
				}
			}
		}
	}

	public byte[] getData() {
		return this.data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public byte canSee(int x, int y, int px, int py) {
		Line line = new Line(x, y, px, py);
		boolean first = false;

		for (Point point : line.getPoints()) {
			if (first) {
				return 2;
			}

			if (this.get((int) point.x, (int) point.y) == Terrain.WALL) {
				first = true;
			}
		}

		if (first) {
			return 1;
		}

		return 0;
	}

	public boolean isValid(int x, int y) {
		return !(x < 0 || y < 0 || x >= getWidth() || y >= getHeight());
	}

	public boolean checkFor(int i, int flag) {
		return (Terrain.flags[this.get(i)] & flag) == flag;
	}

	public boolean checkFor(int x, int y, int flag) {
		return (Terrain.flags[this.get(x, y)] & flag) == flag;
	}

	public void set(int i, byte v) {
		this.data[i] = v;
	}

	public void set(int x, int y, byte v) {
		this.data[toIndex(x, y)] = v;
	}

	public byte get(int i) {
		return this.data[i];
	}

	public byte get(int x, int y) {
		return this.data[toIndex(x, y)];
	}

	public abstract void generate();

	public String getSavePath(DataType type) {
		if (type == DataType.LEVEL) {
			return ".ldg/level" + this.level + ".save";
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

		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getHeight(); y++) {
				boolean b = x == 0 || y == 0 || x == WIDTH - 1 || y == HEIGHT - 1;

				if (b || this.checkFor(x, y, Terrain.SOLID)) {
					int total = 0;

					for (Vector2 vec : NEIGHBOURS8V) {
						Vector2 v = new Vector2(x + vec.x, y + vec.y);

						if (v.x >= 0 && v.y >= 0 && v.x < getWidth() && v.y < getWidth()) {
							if (this.checkFor((int) v.x, (int) v.y, Terrain.SOLID)) {
								total++;
							}
						}
					}

					if (total < 8 || b) {
						PolygonShape poly = new PolygonShape();
						int xx = x * 16;
						int yy = y * 16;


						if (b || this.checkFor(x, y + 1, Terrain.SOLID)) {
							ArrayList<Vector2> array = new ArrayList<>();

							boolean bb = (!this.isValid(x, y - 1) || this.checkFor(x, y - 1, Terrain.SOLID));

							if (bb || !this.isValid(x - 1, y) || this.checkFor(x - 1, y, Terrain.SOLID)) {
								array.add(new Vector2(xx, yy));
							} else {
								array.add(new Vector2(xx, yy + 6));
								array.add(new Vector2(xx + 6, yy));
							}

							if (bb || !this.isValid(x - 1, y) || this.checkFor(x + 1, y, Terrain.SOLID)) {
								array.add(new Vector2(xx + 16, yy));
							} else {
								array.add(new Vector2(xx + 16, yy + 6));
								array.add(new Vector2(xx + 10, yy));
							}

							array.add(new Vector2(xx, yy + 16));
							array.add(new Vector2(xx + 16, yy + 16));

							poly.set(array.toArray(new Vector2[] {}));
						} else {
							ArrayList<Vector2> array = new ArrayList<>();

							boolean bb = (!this.isValid(x, y - 1) || this.checkFor(x, y - 1, Terrain.SOLID));

							if (bb || !this.isValid(x - 1, y) || this.checkFor(x - 1, y, Terrain.SOLID)) {
								array.add(new Vector2(xx, yy));
							} else {
								array.add(new Vector2(xx, yy + 6));
								array.add(new Vector2(xx + 6, yy));
							}

							if (bb || !this.isValid(x - 1, y) || this.checkFor(x + 1, y, Terrain.SOLID)) {
								array.add(new Vector2(xx + 16, yy));
							} else {
								array.add(new Vector2(xx + 16, yy + 6));
								array.add(new Vector2(xx + 10, yy));
							}
							
							if (this.checkFor(x - 1, y, Terrain.SOLID)) {
								array.add(new Vector2(xx, yy + 12));
							} else {
								array.add(new Vector2(xx, yy + 6));
								array.add(new Vector2(xx + 6, yy + 12));
							}

							if (this.checkFor(x + 1, y, Terrain.SOLID)) {
								array.add(new Vector2(xx + 16, yy + 12));
							} else {
								array.add(new Vector2(xx + 10, yy + 12));
								array.add(new Vector2(xx + 16, yy + 6));
							}

							poly.set(array.toArray(new Vector2[] {}));
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
		if (Network.client != null) {
			return;
		}

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
				this.data = new byte[getSIZE()];
				this.initLight();

				for (int i = 0; i < getSIZE(); i++) {
					this.data[i] = stream.readByte();
				}
			}

			this.loadData(stream, type);

			stream.close();
		} catch (Exception e) {
			Dungeon.reportException(e);
		}
	}

	public byte[] getVariants() {
		return this.variants;
	}

	public void save(DataType type) {
		FileHandle save = Gdx.files.external(this.getSavePath(type));
		Log.info("Saving " + type.toString().toLowerCase() + "...");

		try {
			FileWriter stream = new FileWriter(save.file().getAbsolutePath());

			stream.writeByte(VERSION);

			if (type == Level.DataType.LEVEL) {
				stream.writeInt32(getWidth());
				stream.writeInt32(getHeight());

				for (int i = 0; i < getSIZE(); i++) {
					stream.writeByte(this.data[i]);
				}
			}

			this.writeData(stream, type);
			stream.close();
		} catch (Exception e) {
			Dungeon.reportException(e);
		}
	}

	protected void loadData(FileReader stream, DataType type) throws Exception {
		ChangableRegistry.load(stream);

		if (type == DataType.PLAYER) {

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
			heat = 0;
			int count = stream.readInt32();

			this.rooms = new ArrayList<Room>();

			for (int i = 0; i < count; i++) {
				String t = stream.readString();

				Class<?> clazz = Class.forName(t);
				Constructor<?> constructor = clazz.getConstructor();
				Object object = constructor.newInstance(new Object[]{});

				Room room = (Room) object;

				room.left = stream.readInt32();
				room.top = stream.readInt32();
				room.right = stream.readInt32();
				room.bottom = stream.readInt32();

				this.rooms.add(room);
			}

			count = stream.readInt32();

			for (int i = 0; i < count; i++) {
				int in = stream.readInt32();
				this.rooms.get(in).neighbours.add(this.rooms.get(stream.readInt32()));
			}

			count = stream.readInt32();

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
		ChangableRegistry.save(stream);

		if (type == DataType.PLAYER) {

			stream.writeInt32(this.playerSaveable.size());

			for (int i = 0; i < this.playerSaveable.size(); i++) {
				SaveableEntity entity = this.playerSaveable.get(i);

				stream.writeString(entity.getClass().getName());
				entity.save(stream);
			}
		} else {
			stream.writeInt32(this.rooms.size());

			for (int i = 0; i < this.rooms.size(); i++) {
				Room room = this.rooms.get(i);

				stream.writeString(room.getClass().getName());
				stream.writeInt32(room.left);
				stream.writeInt32(room.top);
				stream.writeInt32(room.right);
				stream.writeInt32(room.bottom);
			}

			int count = 0;

			for (Room room : this.rooms) {
				for (Room n : room.neighbours) {
					int in = this.rooms.indexOf(n);

					if (in > -1) {
						count++;
					}
				}
			}

			stream.writeInt32(count);

			for (int i = 0; i < this.rooms.size(); i++) {
				Room room = this.rooms.get(i);

				for (Room n : room.neighbours) {
					int in = this.rooms.indexOf(n);

					if (in > -1) {
						stream.writeInt32(i);
						stream.writeInt32(in);
					}
				}
			}

			stream.writeInt32(this.saveable.size());

			for (int i = 0; i < this.saveable.size(); i++) {
				SaveableEntity entity = this.saveable.get(i);

				stream.writeString(entity.getClass().getName());
				entity.save(stream);
			}
		}
	}

	private boolean check(int x, int y) {
		if (x < 0 || y < 0 || x >= getWidth() || y >= getHeight()) {
			return false;
		}

		return this.get(x, y) == Terrain.WALL;
	}

	public Room getRandomRoom() {
		return this.rooms.get(Random.newInt(this.rooms.size()));
	}

	public Room getRandomRoom(Class<? extends Room> type) {
		for (int i = 0; i < 30; i++) {
			Room room = this.rooms.get(Random.newInt(this.rooms.size()));

			if (type.isInstance(room)) {
				return room;
			}
		}

		return null;
	}

	public Point getRandomFreePoint(Class<? extends Room> type) {
		for (int i = 0; i < 10; i++) {
			Room room = this.getRandomRoom(type);

			if (room == null || room instanceof ExitRoom || room instanceof EntranceRoom) {
				continue;
			}

			for (int j = 0; j < 100; j++) {
				Point point = room.getRandomCell();
				int in = (int) (point.x + point.y * getWidth());

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
				point = this.getRandomFreePoint(RegularRoom.class);
			}

			ItemHolder holder = new ItemHolder();

			holder.setItem(item);
			holder.x = point.x * 16 + Random.newInt(-4, 4);
			holder.y = point.y * 16 + Random.newInt(-4, 4);

			this.addSaveable(holder);
			this.area.add(holder);
		}
	}

	public void spawnCreatures() {
		ArrayList<Creature> creatures = this.generateCreatures();

		for (Creature creature : creatures) {
			Point point = null;

			while (point == null) {
				point = this.getRandomFreePoint(RegularRoom.class);
			}

			creature.x = point.x * 16;
			creature.y = point.y * 16;

			this.addSaveable(creature);
			this.area.add(creature);
		}
	}

	public ArrayList<Room> getRooms() {
		return this.rooms;
	}

	protected ArrayList<Item> generateItems() {
		return new ArrayList<Item>();
	}

	protected ArrayList<Creature> generateCreatures() {
		return new ArrayList<Creature>();
	}

	public Room findRoomFor(int x, int y) {
		for (Room room : this.rooms) {
			if (room.inside(new Point(x, y))) {
				return room;
			}
		}

		return null;
	}

	public enum DataType {
		LEVEL,
		PLAYER
	}
}