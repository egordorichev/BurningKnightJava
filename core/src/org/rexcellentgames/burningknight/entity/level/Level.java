package org.rexcellentgames.burningknight.entity.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.GdxRuntimeException;
import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.level.blood.BloodLevel;
import org.rexcellentgames.burningknight.entity.level.entities.Exit;
import org.rexcellentgames.burningknight.entity.level.entities.fx.ChasmFx;
import org.rexcellentgames.burningknight.entity.level.levels.desert.DesertLevel;
import org.rexcellentgames.burningknight.entity.level.levels.forest.ForestLevel;
import org.rexcellentgames.burningknight.entity.level.levels.hall.HallLevel;
import org.rexcellentgames.burningknight.entity.level.levels.library.LibraryLevel;
import org.rexcellentgames.burningknight.entity.level.rooms.Room;
import org.rexcellentgames.burningknight.entity.level.rooms.entrance.EntranceRoom;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Line;
import org.rexcellentgames.burningknight.util.Log;
import org.rexcellentgames.burningknight.util.MathUtils;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.file.FileWriter;
import org.rexcellentgames.burningknight.util.geometry.Point;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class Level extends SaveableEntity {
	public static boolean RENDER_ROOM_DEBUG = false;
	public static boolean RENDER_PASSABLE = false;
	public static boolean SHADOWS = true;

	public static Color[] colors = {
		Color.valueOf("#1a1932"),
		Color.valueOf("#391f21"),
		Color.valueOf("#5d2c28"),
		Color.valueOf("#1a1932"),
		Color.valueOf("#272727"),
		Color.valueOf("#1a1932"),
		Color.valueOf("#571c27")
	};

	public Room entrance;
	public Room exit;

	public static final Vector2[] NEIGHBOURS8V = {new Vector2(-1, -1), new Vector2(0, -1), new Vector2(1, -1),
		new Vector2(-1, 0), new Vector2(1, 0), new Vector2(-1, 1), new Vector2(0, 1), new Vector2(1, 1)};
	public static boolean GENERATED = false;

	public static String[] COMPASS = {
		" NESW", " ESW", " NSW", " SW", " NEW", " EW", " NW", " W",
		" NES", " ES", " NS", " S", " NE", " E", " N", ""
	};

	private static int WIDTH = 36;
	private static int HEIGHT = 36;
	private static int SIZE = getWidth() * getHeight();
	public byte[] data;
	public byte[] liquidData;
	protected byte[] variants;
	protected byte[] liquidVariants;
	protected byte[] walls;
	public static int[] orders = new int[5];
	protected float[] light;
	protected float[] lightR;
	protected float[] lightG;
	protected float[] lightB;
	public boolean[] passable;
	protected boolean[] low;
	protected boolean[] free;
	protected byte[] decor;
	public boolean[] explored;
	protected Body body;
	protected ArrayList<Room> rooms;
	public ArrayList<Item> itemsToSpawn = new ArrayList<>();

	public void exploreAll() {
		for (int i = 0; i < getSize(); i++) {
			if (data[i] > 0 && data[i] != Terrain.CRACK) {
				explored[i] = true;
			}
		}
	}

	public void exploreRandom() {
		for (int i = 0; i < getSize(); i++) {
			if ((data[i] > 0 && data[i] != Terrain.CRACK) && Random.chance(50)) {
				explored[i] = true;
			}
		}
	}

	public void setPassable(int x, int y, boolean v) {
		this.passable[toIndex(x, y)] = v;
	}

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

	public void generateDecor() {
		decor = new byte[getSize()];
	}

	public static int getHeight() {
		return HEIGHT;
	}

	public static void setHeight(int h) {
		Level.HEIGHT = h;
		Level.SIZE = Level.WIDTH * Level.HEIGHT;
	}

	public static int getSize() {
		return SIZE;
	}

	public static int toIndex(int x, int y) {
		return x + y * getWidth();
	}

	public static byte[] depths = new byte[21];
	public static boolean[] boss = new boolean[21];

	public static RegularLevel forDepth(int depth) {
		int weight = 1;

		for (int i = 0; i < 5; i++) {
			weight += depths[i];

			if (depth < weight) {
				if (depth > 0 && boss[depth]) {
					switch (orders[i]) {
						case 0: default: return new HallLevel().setBoss(true);
						case 1: return new DesertLevel().setBoss(true);
						case 2: return new LibraryLevel().setBoss(true);
					}
				} else {
					switch (orders[i]) {
						case 0: default: return new HallLevel();
						case 1: return new DesertLevel();
						case 2: return new LibraryLevel();
						case 3: return new ForestLevel();
						case 4: return new BloodLevel();
					}
				}
			}
		}

		return new HallLevel();
	}

	public String getDepthAsCoolNum() {
		int weight = 0;

		for (int i = 0; i < 5; i++) {
			weight += depths[i];

			if (Dungeon.depth <= weight) {
				return "" + letters[(depths[i] - (weight - Dungeon.depth) - 1)];
			}
		}

		return "";
	}

	public String formatDepth() {
		if (Dungeon.depth == -1) {
			return Locale.get("castle");
		} else if (Dungeon.depth == 0) {
			return Locale.get("beginning");
		} else {
			return getName() + " " + getDepthAsCoolNum();
		}
	}

	private static String[] letters = { "I", "II", "III", "IV", "V", "VI" };

	public boolean addLight = false;

	public void initLight() {
		this.light = new float[getSize()];
		this.lightR = new float[getSize()];
		this.lightG = new float[getSize()];
		this.lightB = new float[getSize()];

		Color color = colors[Dungeon.level.uid];

		Arrays.fill(this.lightR, color.r);
		Arrays.fill(this.lightG, color.g);
		Arrays.fill(this.lightB, color.b);
		//  (BurningKnight.instance == null)
		Arrays.fill(this.light, Dungeon.level.addLight && false ? 1f : 0f);
	}

	public int uid = 0;

	public void fill() {
		this.data = new byte[getSize()];
		this.liquidData = new byte[getSize()];
		this.explored = new boolean[getSize()];

		this.initLight();

		byte tile = Terrain.WALL;

		for (int i = 0; i < getSize(); i++) {
			this.data[i] = tile;
		}
	}

	public void loadPassable() {
		this.passable = new boolean[getSize()];
		this.low = new boolean[getSize()];
		this.variants = new byte[getSize()];
		this.liquidVariants = new byte[getSize()];
		this.walls = new byte[getSize()];

		for (int i = 0; i < getSize(); i++) {
			this.passable[i] = this.checkFor(i, Terrain.PASSABLE);
			this.low[i] = !this.checkFor(i, Terrain.HIGH);
		}

		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				this.tile(x, y);
			}
		}
	}

	public void tile(int x, int y) {
		byte tile = this.get(x, y);

		if (tile == Terrain.CHASM) {
			this.tileUp(x, y, tile, false);
		} else if (tile == Terrain.WALL || tile == Terrain.CRACK) {
			this.tileUp(x, y, tile, false);
			this.decor[toIndex(x, y)] = (byte) Random.newInt(3);
		} else if (tile == Terrain.TABLE) {
			this.tileUp(x, y, tile, false);
		} else if (tile == Terrain.GRASS) {
			this.tileUp(x, y, tile, false);
		} else if (tile == Terrain.FLOOR_A || tile == Terrain.FLOOR_B || tile == Terrain.FLOOR_C || tile == Terrain.FLOOR_D) {
			this.makeFloor(x, y, tile);
		}

		byte t = this.liquidData[toIndex(x, y)];

		if (t == Terrain.LAVA) {
			this.tileUpLiquid(x, y, t, false);
		} else if (t == Terrain.WATER) {
			this.tileUpLiquid(x, y, t, false);
		} else if (t == Terrain.DIRT) {
			this.tileUpLiquid(x, y, t, false);
		} else if (t == Terrain.GRASS) {
			this.tileUpLiquid(x, y, t, false);
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

		if (!this.shouldTile(x + 1, y + 1, Terrain.WALL, false)) {
			count += 16;
		}

		if (!this.shouldTile(x + 1, y - 1, Terrain.WALL, false)) {
			count += 32;
		}

		if (!this.shouldTile(x - 1, y - 1, Terrain.WALL, false)) {
			count += 64;
		}

		if (!this.shouldTile(x - 1, y + 1, Terrain.WALL, false)) {
			count += 128;
		}

		this.walls[toIndex(x, y)] = count;
	}

	public void tileRegion(int x, int y) {
		for (int xx = x - 1; xx <= x + 1; xx++) {
			for (int yy = y - 1; yy <= y + 1; yy++) {
				this.tile(xx, yy);
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
						var = (byte) Random.newInt(0, 9);
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
			this.variants[i] = (byte) MathUtils.clamp(1, 7, var);
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

	private void tileUpLiquid(int x, int y, int tile, boolean flag) {
		byte count = 0;

		if (this.shouldTileLiquid(x, y + 1, tile, flag)) {
			count += 1;
		}

		if (this.shouldTileLiquid(x + 1, y, tile, flag)) {
			count += 2;
		}

		if (this.shouldTileLiquid(x, y - 1, tile, flag)) {
			count += 4;
		}

		if (this.shouldTileLiquid(x - 1, y, tile, flag)) {
			count += 8;
		}

		this.liquidVariants[toIndex(x, y)] = count;
	}

	private boolean shouldTile(int x, int y, int tile, boolean flag) {
		if (!this.isValid(x, y)) {
			return false;
		}

		byte t = this.get(x, y);

		if (flag) {
			return this.checkFor(x, y, tile) || t == Terrain.WALL || t == Terrain.CRACK;
		} else {
			return t == tile || t == Terrain.WALL || t == Terrain.CRACK;
		}
	}

	private boolean shouldTileLiquid(int x, int y, int tile, boolean flag) {
		if (!this.isValid(x, y)) {
			return false;
		}

		byte t = this.get(x, y);

		if (flag) {
			return this.checkFor(x, y, tile) || t == Terrain.WALL || t == Terrain.CRACK;
		} else {
			byte tt = this.liquidData[toIndex(x, y)];
			return tt == tile || t == Terrain.WALL || t == Terrain.CRACK;
		}
	}

	public boolean[] getPassable() {
		return this.passable;
	}

	@Override
	public void init() {
		Dungeon.level = this;

		this.alwaysRender = true;
		this.depth = -10;

		SolidLevel l = new SolidLevel();
		l.setLevel(this);

		Dungeon.area.add(l);

		LightLevel ll = new LightLevel();
		ll.setLevel(this);

		Dungeon.area.add(ll);

		WallLevel lll = new WallLevel();
		lll.setLevel(this);

		Dungeon.area.add(lll);

		LiquidLevel llll = new LiquidLevel();
		llll.setLevel(this);

		Dungeon.area.add(llll);
	}

	public String getName() {
		return "";
	}

	public void renderLight() {
		if (Dungeon.level != this) {
			return;
		}

		OrthographicCamera camera = Camera.game;

		Graphics.batch.end();
		Graphics.batch.setProjectionMatrix(Camera.game.combined);
		Graphics.shape.setProjectionMatrix(Camera.game.combined);
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
		Color color = colors[Dungeon.level.uid];

		float sp = dt * 3f;

		for (int i = 0; i < getSize(); i++) {
			this.light[i] = MathUtils.clamp(0f, 1f, this.light[i] - sp);
			this.lightR[i] = MathUtils.clamp(color.r, 1f, this.lightR[i] - sp);
			this.lightG[i] = MathUtils.clamp(color.g, 1f, this.lightG[i] - sp);
			this.lightB[i] = MathUtils.clamp(color.b, 1f, this.lightB[i] - sp);
		}

		for (int x = Math.max(0, sx); x < Math.min(fx, getWidth()); x++) {
			for (int y = Math.max(0, sy); y < Math.min(fy, getHeight()); y++) {
				int i = x + y * getWidth();

				if (i >= data.length) {
					continue;
				}

				float v = this.light[i];

				if (v == 1 || v == 0) {
					continue;
				}

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

		float s = 0.5f;
		float md = 1f / s;

		for (int x = Math.max(0, sx); x < Math.min(fx, getWidth()); x++) {
			for (int y = Math.max(0, sy); y < Math.min(fy, getHeight()); y++) {
				int i = x + y * getWidth();

				if (i >= data.length) {
					continue;
				}

				float v = this.light[i];

				if (v < s) {
					int t = (int) Math.floor((v * (md)) * 10);

					if (t < 10) {
						Graphics.batch.setColor(color);
						Graphics.render(Terrain.dither[9 - t], x * 16, y * 16 - 8, 0, 0, 0, false, false);
					}
				}
			}
		}

		Graphics.batch.setColor(1, 1, 1, 1);
	}

	private boolean isBitSet(short data, int bit) {
		return (data & (1 << bit)) != 0;
	}

	@Override
	public void update(float dt) {
		super.update(dt);


		if (this != Dungeon.level) {
			Log.error("Extra level!");
			setDone(true);
		}
	}

	// TODO: Too complex
	public void renderSolid() {
		if (Dungeon.level != this) {
			return;
		}

		for (Room room : this.rooms) {
			room.numEnemies = 0;
		}

		OrthographicCamera camera = Camera.game;

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

				if (!this.low[i] && (this.light[i] > 0 || this.light[i + getWidth()] > 0)) {
					byte tile = this.get(i);

					if (Terrain.patterns[tile] != null) {
						/*if (tile == Terrain.WALL || tile == Terrain.CRACK) {
							byte variant = this.walls[i];

							if (variant == 0) {
								continue;
							}
						}*/

						TextureRegion region = new TextureRegion(Terrain.patterns[tile]);

						int n = region.getRegionHeight() / 16;

						region.setRegionX(region.getRegionX() + x % (region.getRegionWidth() / 16) * 16);
						region.setRegionY(region.getRegionY() + (n - 1 - (y % n)) * 16);
						region.setRegionWidth(16);
						region.setRegionHeight(16);

						Graphics.render(region, x * 16, y * 16);
					}

					if (tile != Terrain.WALL && tile != Terrain.CRACK && Terrain.variants[tile] != null) {
						byte variant = this.variants[i];

						if (variant != Terrain.variants[tile].length && Terrain.variants[tile][variant] != null) {
							Graphics.render(Terrain.variants[tile][variant], x * 16, y * 16 - 8);
						}
					}

					if (tile == Terrain.WALL || tile == Terrain.CRACK) {
						short v = this.walls[i];

						for (int xx = 0; xx < 2; xx++) {
							for (int yy = 0; yy < 2; yy++) {
								int lv = 0;

								if (yy == 0 || !isBitSet(v, 0)) {
									lv += 1;
								}

								if (xx == 0 || !isBitSet(v, 1)) {
									lv += 2;
								}

								if (yy > 0 || !isBitSet(v, 2)) {
									lv += 4;
								}

								if (xx > 0 || !isBitSet(v, 3)) {
									lv += 8;
								}

								if (lv == 15) {
									lv = 0;

									if (xx == 1 && yy == 1 && isBitSet(v, 4)) {
										lv += 1;
									}

									if (xx == 1 && yy == 0 && isBitSet(v, 5)) {
										lv += 2;
									}

									if (xx == 0 && yy == 0 && isBitSet(v, 6)) {
										lv += 4;
									}

									if (xx == 0 && yy == 1 && isBitSet(v, 7)) {
										lv += 8;
									}

									int vl = Terrain.wallMapExtra[lv];

									if (vl != -1) {
										float a =  getLight(x + (xx == 0 ? -1 : 1), y + yy);

										if (a > 0.05f) {
											Graphics.batch.setColor(1, 1, 1, a);
											Graphics.render(Terrain.wallTop[this.decor[i]][vl], x * 16 + xx * 8, y * 16 + yy * 8);
										}
									}
								} else {
									int vl = Terrain.wallMap[lv];

									if (vl != -1) {
										float a =  getLight(x + (xx == 0 ? -1 : 1), y + yy);

										if (a > 0.05f) {
											Graphics.batch.setColor(1, 1, 1, a);
											Graphics.render(Terrain.wallTop[this.decor[i]][vl], x * 16 + xx * 8, y * 16 + yy * 8);
										}
									}
								}
							}
						}
					}

					Graphics.batch.setColor(1, 1, 1, 1);
				}

				// useful passable debug

				if (RENDER_PASSABLE) {
					if (this.passable[i]) {
						Graphics.batch.end();
						Graphics.shape.setProjectionMatrix(Camera.game.combined);
						Graphics.shape.setColor(0.5f, 0.5f, 0.5f, 1);
						Graphics.shape.begin(ShapeRenderer.ShapeType.Line);
						Graphics.shape.rect(x * 16 + 1, y * 16 + 1 - 8, 16 - 2, 16 - 2);
						Graphics.shape.end();
						Graphics.batch.begin();
					}
				}
			}
		}

		// Useful room debug

		if (RENDER_ROOM_DEBUG) {
			Graphics.batch.end();
			Gdx.gl.glEnable(GL20.GL_BLEND);
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			Graphics.shape.setProjectionMatrix(Camera.game.combined);
			Graphics.shape.begin(ShapeRenderer.ShapeType.Filled);
			for (Room room : this.rooms) {
				Graphics.shape.setColor(room.hidden ? 0 : 1, room == Player.instance.room ? 0 : 1, room == Player.instance.room ? 0 : 1, 0.1f);
				Graphics.shape.rect(room.left * 16 + 8, room.top * 16 + 8, room.getWidth() * 16 - 16, room.getHeight() * 16 - 16);
			}

			Graphics.shape.end();
			Gdx.gl.glDisable(GL20.GL_BLEND);
			Graphics.batch.begin();

			for (Room room : this.rooms) {
				Graphics.print(room.getClass().getSimpleName(), Graphics.small, room.left * 16 + 16, room.top * 16 + 16);
			}
		}
	}

	public static ShaderProgram maskShader;

	static {
		maskShader = new ShaderProgram( Gdx.files.internal("shaders/mask.vert").readString(),  Gdx.files.internal("shaders/mask.frag").readString());

		if (!maskShader.isCompiled()) {
			throw new GdxRuntimeException("Couldn't compile shader: " + maskShader.getLog());
		}
	}

	private void renderFloor(int sx, int sy, int fx, int fy) {
		if (this.low == null || this.light == null) {
			this.loadPassable();
		}

		boolean pause = Dungeon.game.getState().isPaused();

		for (int x = Math.max(0, sx); x < Math.min(fx, getWidth()); x++) {
			for (int y = Math.max(0, sy); y < Math.min(fy, getHeight()); y++) {
				int i = x + y * getWidth();

				if (this.low[i] && this.light[i] > 0) {
					byte tile = this.get(i);

					if (Terrain.patterns[tile] != null) {
						TextureRegion region = new TextureRegion(Terrain.patterns[tile]);

						int w = region.getRegionWidth() / 16;
						int h = region.getRegionHeight() / 16;

						region.setRegionX(region.getRegionX() + x % w * 16);
						region.setRegionY(region.getRegionY() + (h - 1 - y % h) * 16);
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

					if (tile == Terrain.CHASM && Random.chance(0.4f) && !pause) {
						Dungeon.area.add(new ChasmFx(Random.newFloat(1f) * 16 + x * 16, Random.newFloat(1f) * 16 + y * 16 - 8));
					}
				}
			}
		}
	}

	public void renderLiquids() {
		OrthographicCamera camera = Camera.game;

		float zoom = camera.zoom;

		float cx = camera.position.x - Display.GAME_WIDTH / 2 * zoom;
		float cy = camera.position.y - Display.GAME_HEIGHT / 2 * zoom;

		int sx = (int) (Math.floor(cx / 16) - 1);
		int sy = (int) (Math.floor(cy / 16) - 1);

		int fx = (int) (Math.ceil((cx + Display.GAME_WIDTH * zoom) / 16) + 1);
		int fy = (int) (Math.ceil((cy + Display.GAME_HEIGHT * zoom) / 16) + 1);

		Graphics.batch.end();
		Graphics.batch.setShader(maskShader);
		Graphics.batch.begin();

		for (int x = Math.max(0, sx); x < Math.min(fx, getWidth()); x++) {
			for (int y = Math.min(fy, getHeight()) - 1; y >= Math.max(0, sy);  y--) {
				int i = x + y * getWidth();
				if (this.light[i] == 0) {
					continue;
				}

				byte tile = this.liquidData[i];

				if (tile == Terrain.EXIT) {
					float dt = Gdx.graphics.getDeltaTime();
					Exit.al = MathUtils.clamp(0, 1, Exit.al + ((Exit.exitFx != null ? 1 : 0) - Exit.al) * dt * 10);

					if (Exit.al > 0) {
						Graphics.batch.end();
						Mob.shader.begin();
						Mob.shader.setUniformf("u_color", new Vector3(1, 1, 1));
						Mob.shader.setUniformf("u_a", Exit.al);
						Mob.shader.end();
						Graphics.batch.setShader(Mob.shader);
						Graphics.batch.begin();

						for (int xx = -1; xx < 2; xx++) {
							for (int yy = -1; yy < 2; yy++) {
								if (Math.abs(xx) + Math.abs(yy) == 1) {
									Graphics.render(Terrain.exit, x * 16 + xx, y * 16 - 8 + yy);
								}
							}
						}

						Graphics.batch.end();
						Graphics.batch.setShader(maskShader);
						Graphics.batch.begin();
					}

					Graphics.render(Terrain.exit, x * 16, y * 16 - 8);
				} else if (tile == Terrain.WATER) {
					byte variant = this.liquidVariants[i];

					TextureRegion r = new TextureRegion(Terrain.waterPattern);

					r.setRegionX(r.getRegionX() + (x % 4) * 16);
					r.setRegionY(r.getRegionY() + (3 - y % 4) * 16);

					int rx = r.getRegionX();
					int ry = r.getRegionY();

					r.setRegionHeight(16);
					r.setRegionWidth(16);

					Texture texture = r.getTexture();

					int rw = texture.getWidth();
					int rh = texture.getHeight();

					TextureRegion rr = Terrain.waterVariants[variant];
					Texture t = rr.getTexture();

					Graphics.batch.end();
					maskShader.begin();
					t.bind(1);
					maskShader.setUniformf("water", 1);
					maskShader.setUniformf("activated", 1);
					maskShader.setUniformi("u_texture2", 1);
					maskShader.setUniformf("tpos", new Vector2(((float) rr.getRegionX()) / rw, ((float) rr.getRegionY()) / rh));
					texture.bind(0);
					maskShader.setUniformi("u_texture", 1);
					maskShader.setUniformf("time", Dungeon.time);
					maskShader.setUniformf("pos", new Vector2(((float) rx) / rw, ((float) ry) / rh));
					maskShader.setUniformf("size", new Vector2(16f / rw, 16f / rh));
					maskShader.end();
					Graphics.batch.begin();

					Graphics.render(r, x * 16, y * 16 - 8);

					Graphics.batch.end();
					maskShader.begin();
					maskShader.setUniformf("activated", 0);
					maskShader.end();
					Graphics.batch.begin();

					if (variant != 15) {
						Graphics.render(Terrain.pooledge[variant], x * 16, y * 16 - 8);
					}
				} else if (tile == Terrain.LAVA) {
					byte variant = this.liquidVariants[i];

					TextureRegion r = new TextureRegion(Terrain.lavaPattern);

					r.setRegionX(r.getRegionX() + x % 4 * 16);
					r.setRegionY(r.getRegionY() + (3 - y % 4) * 16);

					int rx = r.getRegionX();
					int ry = r.getRegionY();

					r.setRegionHeight(16);
					r.setRegionWidth(16);

					Texture texture = r.getTexture();

					int rw = texture.getWidth();
					int rh = texture.getHeight();

					TextureRegion rr = Terrain.lavaVariants[variant];
					Texture t = rr.getTexture();

					Graphics.batch.end();
					maskShader.begin();
					t.bind(1);
					maskShader.setUniformf("activated", 1);
					maskShader.setUniformf("water", 1);
					maskShader.setUniformi("u_texture2", 1);
					maskShader.setUniformf("tpos", new Vector2(((float) rr.getRegionX()) / rw, ((float) rr.getRegionY()) / rh));
					texture.bind(0);
					maskShader.setUniformi("u_texture", 1);
					maskShader.setUniformf("time", Dungeon.time);
					maskShader.setUniformf("pos", new Vector2(((float) rx) / rw, ((float) ry) / rh));
					maskShader.setUniformf("size", new Vector2(16f / rw, 16f / rh));
					maskShader.end();
					Graphics.batch.begin();

					Graphics.render(r, x * 16, y * 16 - 8);

					Graphics.batch.end();
					maskShader.begin();
					maskShader.setUniformf("activated", 0);
					maskShader.end();
					Graphics.batch.begin();

					if (variant != 15) {
						Graphics.render(Terrain.lavaedge[variant], x * 16, y * 16 - 8);
					}
				}
			}
		}

		Graphics.batch.end();
		Graphics.batch.setShader(null);
		Graphics.batch.begin();

		for (int x = Math.max(0, sx); x < Math.min(fx, getWidth()); x++) {
			for (int y = Math.min(fy, getHeight()) - 1; y >= Math.max(0, sy);  y--) {
				int i = x + y * getWidth();
				byte tile = this.get(i);

				if (this.light[i] > 0 && i >= getWidth() && (tile == Terrain.WALL || tile == Terrain.CRACK)) {
					byte t = this.get(i - getWidth());

					if (t != Terrain.CRACK && t != Terrain.WALL) {
						Graphics.startShadows();
						Graphics.render(Terrain.topVariants[0], x * 16, y * 16 - 1, 0, 0, 0, false, false, 1f, -1f);
						Graphics.endShadows();
					}
				}
			}
		}

		renderShadows();
	}

	private void renderShadows() {
		if (SHADOWS) {
			float zoom = Camera.game.zoom;

			Graphics.batch.setProjectionMatrix(Camera.game.combined);
			Graphics.batch.setColor(0, 0, 0, 0.5f);
			Texture texture = Graphics.shadows.getColorBufferTexture();
			texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

			Graphics.batch.draw(texture,
				Camera.game.position.x - Display.GAME_WIDTH / 2 * zoom,
				Camera.game.position.y - Display.GAME_HEIGHT / 2 * zoom, Display.GAME_WIDTH * zoom, Display.GAME_HEIGHT * zoom,
				0, 0, texture.getWidth(), texture.getHeight(), false, true);

			Graphics.batch.setColor(1, 1, 1, 1f);
		}
	}

	public static ShaderProgram shader;

	static {
		shader = new ShaderProgram(Gdx.files.internal("shaders/fadeout.vert").readString(),  Gdx.files.internal("shaders/fadeout.frag").readString());
		if (!shader.isCompiled()) throw new GdxRuntimeException("Couldn't compile shader: " + shader.getLog());
	}


	public void renderSides() {
		OrthographicCamera camera = Camera.game;

		float zoom = camera.zoom;

		float cx = camera.position.x - Display.GAME_WIDTH / 2 * zoom;
		float cy = camera.position.y - Display.GAME_HEIGHT / 2 * zoom;

		int sx = (int) (Math.floor(cx / 16) - 1);
		int sy = (int) (Math.floor(cy / 16) - 1);

		int fx = (int) (Math.ceil((cx + Display.GAME_WIDTH * zoom) / 16) + 1);
		int fy = (int) (Math.ceil((cy + Display.GAME_HEIGHT * zoom) / 16) + 1);

		for (int x = Math.max(0, sx); x < Math.min(fx, getWidth()); x++) {
			for (int y = Math.min(fy, getHeight()) - 1; y >= Math.max(0, sy);  y--) {
				int i = x + y * getWidth();

				if (i >= data.length) {
					continue;
				}

				byte tile = this.get(i);

				if (this.light[i] > 0 && i >= getWidth()) {
					if ((tile == Terrain.WALL || tile == Terrain.CRACK)) {
						byte left = this.get(i - 1);
						byte right = this.get(i + 1);
						boolean lg = (left == Terrain.WALL || left == Terrain.CRACK);
						boolean rg = (right == Terrain.WALL || right == Terrain.CRACK);

						byte t = this.get(i - getWidth());

						if (t != Terrain.CRACK && t != Terrain.WALL) {
							Graphics.render(Terrain.topVariants[(x * 3 + y / 2 + (x + y) / 2) % 12], x * 16, y * 16 - 16);

							byte v = this.decor[i];

							if (v != 0) {
								TextureRegion s = Terrain.decor[v - 1];
								Graphics.render(s, x * 16 + (16 - s.getRegionWidth()) / 2, y * 16 - 8);
							}

							if (!lg || !rg) {
								t = 1;

								if (lg) {
									t = 0;
								} else if (rg) {
									t = 2;
								}

								Graphics.render(Terrain.sides[t], x * 16, y * 16 - 16);
							}
						}
					} else if (tile == Terrain.CHASM) {
						Graphics.render(Terrain.chasmPattern, x * 16, y * 16 - 8);
					}
				}
			}
		}


		Graphics.batch.end();
		Graphics.batch.setShader(shader);
		Graphics.batch.begin();

		for (int x = Math.max(0, sx); x < Math.min(fx, getWidth()); x++) {
			for (int y = Math.min(fy, getHeight()) - 1; y >= Math.max(0, sy);  y--) {
				int i = x + y * getWidth();
				if (i >= data.length) {
					continue;
				}

				byte tile = this.get(i);

				if (this.light[i] > 0 && i >= getWidth()) {
					if (tile == Terrain.CHASM && this.get(i + getWidth()) != Terrain.CHASM) {
						Graphics.batch.end();
						shader.begin();

						TextureRegion reg = Terrain.topVariants[(x * 3 + y / 2 + (x + y) / 2) % 12];
						Texture texture = reg.getTexture();

						shader.setUniformf("pos", new Vector2(((float) reg.getRegionX()) / texture.getWidth(), ((float) reg.getRegionY()) / texture.getHeight()));
						shader.setUniformf("size", new Vector2(((float) reg.getRegionWidth()) / texture.getWidth(), ((float) reg.getRegionHeight()) / texture.getHeight()));

						shader.end();
						Graphics.batch.begin();
						Graphics.render(reg, x * 16, y * 16 - 8);
					}
				}
			}
		}

		Graphics.batch.end();
		Graphics.batch.setShader(null);
		Graphics.batch.begin();
	}

	@Override
	public void render() {
		if (Dungeon.level != this) {
			setDone(true);
			return;
		}

		OrthographicCamera camera = Camera.game;

		float zoom = camera.zoom;

		float cx = camera.position.x - Display.GAME_WIDTH / 2 * zoom;
		float cy = camera.position.y - Display.GAME_HEIGHT / 2 * zoom;

		int sx = (int) (Math.floor(cx / 16) - 1);
		int sy = (int) (Math.floor(cy / 16) - 1);

		int fx = (int) (Math.ceil((cx + Display.GAME_WIDTH * zoom) / 16) + 1);
		int fy = (int) (Math.ceil((cy + Display.GAME_HEIGHT * zoom) / 16) + 1);

		renderFloor(sx, sy, fx, fy);

		Graphics.batch.end();
		Graphics.batch.setShader(maskShader);
		Graphics.batch.begin();

		for (int x = Math.max(0, sx); x < Math.min(fx, getWidth()); x++) {
			for (int y = Math.min(fy, getHeight()) - 1; y >= Math.max(0, sy);  y--) {
				int i = x + y * getWidth();
				if (this.light[i] == 0) {
					continue;
				}

				byte tile = this.liquidData[i];

				if (tile == Terrain.EXIT) {
					Graphics.render(Terrain.exit, x * 16, y * 16 - 8);
				} else if (tile == Terrain.DIRT) {
					byte variant = this.liquidVariants[i];

					TextureRegion r = new TextureRegion(Terrain.dirtPattern);

					r.setRegionX(r.getRegionX() + x % 4 * 16);
					r.setRegionY(r.getRegionY() + (3 - y % 4) * 16);

					int rx = r.getRegionX();
					int ry = r.getRegionY();

					r.setRegionHeight(16);
					r.setRegionWidth(16);

					Texture texture = r.getTexture();

					int rw = texture.getWidth();
					int rh = texture.getHeight();

					TextureRegion rr = Terrain.lavaVariants[variant];
					Texture t = rr.getTexture();

					Graphics.batch.end();
					maskShader.begin();
					t.bind(1);
					maskShader.setUniformf("activated", 1);
					maskShader.setUniformf("water", 0);
					maskShader.setUniformi("u_texture2", 1);
					maskShader.setUniformf("tpos", new Vector2(((float) rr.getRegionX()) / rw, ((float) rr.getRegionY()) / rh));
					texture.bind(0);
					maskShader.setUniformi("u_texture", 1);
					maskShader.setUniformf("time", Dungeon.time);
					maskShader.setUniformf("pos", new Vector2(((float) rx) / rw, ((float) ry) / rh));
					maskShader.setUniformf("size", new Vector2(16f / rw, 16f / rh));
					maskShader.end();
					Graphics.batch.begin();

					Graphics.render(r, x * 16, y * 16 - 8);

					Graphics.batch.end();
					maskShader.begin();
					maskShader.setUniformf("activated", 0);
					maskShader.end();
					Graphics.batch.begin();

					if (variant != 15) {
						Graphics.render(Terrain.dirtedge[variant], x * 16, y * 16 - 8);
					}
				} else if (tile == Terrain.GRASS) {
					byte variant = this.liquidVariants[i];

					TextureRegion r = new TextureRegion(Terrain.grassPattern);

					r.setRegionX(r.getRegionX() + x % 4 * 16);
					r.setRegionY(r.getRegionY() + (3 - y % 4) * 16);

					int rx = r.getRegionX();
					int ry = r.getRegionY();

					r.setRegionHeight(16);
					r.setRegionWidth(16);

					Texture texture = r.getTexture();

					int rw = texture.getWidth();
					int rh = texture.getHeight();

					TextureRegion rr = Terrain.lavaVariants[variant];
					Texture t = rr.getTexture();

					Graphics.batch.end();
					maskShader.begin();
					t.bind(1);
					maskShader.setUniformf("activated", 1);
					maskShader.setUniformf("water", 0);
					maskShader.setUniformi("u_texture2", 1);
					maskShader.setUniformf("tpos", new Vector2(((float) rr.getRegionX()) / rw, ((float) rr.getRegionY()) / rh));
					texture.bind(0);
					maskShader.setUniformi("u_texture", 1);
					maskShader.setUniformf("time", Dungeon.time);
					maskShader.setUniformf("pos", new Vector2(((float) rx) / rw, ((float) ry) / rh));
					maskShader.setUniformf("size", new Vector2(16f / rw, 16f / rh));
					maskShader.end();
					Graphics.batch.begin();

					Graphics.render(r, x * 16, y * 16 - 8);

					Graphics.batch.end();
					maskShader.begin();
					maskShader.setUniformf("activated", 0);
					maskShader.end();
					Graphics.batch.begin();

					if (variant != 15) {
						Graphics.render(Terrain.dirtedge[variant], x * 16, y * 16 - 8);
					}
				}
			}
		}

		Graphics.batch.end();
		Graphics.batch.setShader(null);
		Graphics.batch.begin();
	}

	public void addLight(float x, float y, float r, float g, float b, float a, float max) {
		float dt = Gdx.graphics.getDeltaTime();
		int i = (int) (Math.floor(x / 16) + Math.floor(y / 16) * getWidth());

		if (i < 0 || i >= getSize()) {
			return;
		}

		if (this.light[i] < max) {
			this.light[i] = Math.min(1, this.light[i] + a * dt);
		}

		if (this.lightR[i] < max) {
			this.lightR[i] = Math.min(1, this.lightR[i] + r * dt);
		}

		if (this.lightG[i] < max) {
			this.lightG[i] = Math.min(1, this.lightG[i] + g * dt);
		}

		if (this.lightB[i] < max) {
			this.lightB[i] = Math.min(1, this.lightB[i] + b * dt);
		}

		if (this.light[i] > 0.2f && (data[i] > 0 && data[i] != Terrain.CRACK)) {
			this.explored[i] = true;
		}
	}

	public float getLight(int i) {
		if (i < 0 || i >= getSize()) {
			return 0;
		}

		return this.light[i];
	}

	public float getLight(int x, int y) {
		int i = toIndex(x, y);

		if (i < 0 || i >= getSize()) {
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
						if (this.isValid(fx + xx, fy + yy) && this.isValid(fx, fy)) {
							byte vl = this.canSee(fx, fy, fx + xx, fy + yy);

							if (vl == 1 && yy >= 0) {
								v = 0.5f;
								see = true;
							} else { //(fy + yy > 0 && Dungeon.level.checkFor(fx, fy + yy - 1, Terrain.PASSABLE));
								see = vl == 0;
							}
						} else {
							see = false;
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
		return canSee(x, y, px, py, 0);
	}

	public byte canSee(int x, int y, int px, int py, int extra) {
		Line line = new Line(x, y, px, py);
		boolean first = false;

		for (Point point : line.getPoints()) {
			if (first) {
				return 2;
			}

			if (this.checkFor((int) point.x, (int) point.y, Terrain.BREAKS_LOS)) {
				first = true;
			} else if (extra != 0 && this.checkFor((int) point.x, (int) point.y, extra)) {
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
		if (flag == Terrain.PASSABLE && this.liquidData[i] == Terrain.LAVA) {
			return false;
		}

		return (Terrain.flags[this.get(i)] & flag) == flag;
	}

	public boolean checkFor(int x, int y, int flag) {
		return checkFor(toIndex(x, y), flag);
	}

	public void set(int i, byte v) {
		if (v == Terrain.WATER || v == Terrain.LAVA || v == Terrain.DIRT || v == Terrain.EXIT || v == Terrain.GRASS) {
			this.liquidData[i] = v;

			if (this.get(i) == Terrain.CHASM) {
				this.set(i, Terrain.FLOOR_A);
			}
		} else {
			this.data[i] = v;

			if (v == Terrain.CHASM) {
				this.liquidData[i] = 0;
			}
		}
	}

	public void set(int x, int y, byte v) {
		if (v == Terrain.WATER || v == Terrain.LAVA || v == Terrain.DIRT || v == Terrain.EXIT || v == Terrain.GRASS) {
			this.liquidData[toIndex(x, y)] = v;
		} else {
			this.data[toIndex(x, y)] = v;
		}
	}

	public byte get(int i) {
		byte t = this.data[i];
		return t < 0 ? Terrain.WALL : t;
	}

	public void hide(int x, int y) {
		this.data[toIndex(x, y)] = (byte) -this.data[toIndex(x, y)];
	}

	public byte get(int x, int y) {
		return get(toIndex(x, y));
	}

	public abstract void generate();

	@Override
	public void destroy() {
		super.destroy();
		this.body = World.removeBody(this.body);
		this.chasms = World.removeBody(this.chasms);
	}

	private Body chasms;

	public boolean same(Level level) {
		return this.getClass().isInstance(level);
	}

	public void addPhysics() {
		Log.physics("Creating level body");

		this.body = World.removeBody(this.body);
		this.chasms = World.removeBody(this.chasms);

		BodyDef def = new BodyDef();
		def.type = BodyDef.BodyType.StaticBody;

		this.body = World.world.createBody(def);

		BodyDef cdef = new BodyDef();
		cdef.type = BodyDef.BodyType.StaticBody;
		cdef.bullet = true;

		this.chasms = World.world.createBody(cdef);

		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getHeight(); y++) {
				if (this.checkFor(x, y, Terrain.SOLID)) {
					int total = 0;

					for (Vector2 vec : NEIGHBOURS8V) {
						Vector2 v = new Vector2(x + vec.x, y + vec.y);

						if (this.isValid((int) v.x, (int) v.y) && (this.checkFor((int) v.x, (int) v.y, Terrain.SOLID))) {
							total++;
						}
					}

					if (total < 8) {
						PolygonShape poly = new PolygonShape();
						int xx = x * 16;
						int yy = y * 16;


						if (this.checkFor(x, y + 1, Terrain.SOLID) || this.checkFor(x, y + 1, Terrain.HOLE)) {
							ArrayList<Vector2> array = new ArrayList<>();

							boolean bb = (!this.isValid(x, y - 1) || this.checkFor(x, y - 1, Terrain.SOLID) || this.checkFor(x, y - 1, Terrain.HOLE));

							if (bb || !this.isValid(x - 1, y) || this.checkFor(x - 1, y, Terrain.SOLID) || this.checkFor(x - 1, y, Terrain.HOLE)) {
								array.add(new Vector2(xx, yy));
							} else {
								array.add(new Vector2(xx, yy + 6));
								array.add(new Vector2(xx + 6, yy));
							}

							if (bb || !this.isValid(x - 1, y) || this.checkFor(x + 1, y, Terrain.SOLID) || this.checkFor(x + 1, y, Terrain.HOLE)) {
								array.add(new Vector2(xx + 16, yy));
							} else {
								array.add(new Vector2(xx + 16, yy + 6));
								array.add(new Vector2(xx + 10, yy));
							}

							array.add(new Vector2(xx, yy + 16));
							array.add(new Vector2(xx + 16, yy + 16));

							poly.set(array.toArray(new Vector2[]{}));
						} else {
							ArrayList<Vector2> array = new ArrayList<>();

							boolean bb = (!this.isValid(x, y - 1) || this.checkFor(x, y - 1, Terrain.SOLID) || this.checkFor(x, y - 1, Terrain.HOLE));

							if (bb || !this.isValid(x - 1, y) || this.checkFor(x - 1, y, Terrain.SOLID) || this.checkFor(x - 1, y, Terrain.HOLE)) {
								array.add(new Vector2(xx, yy));
							} else {
								array.add(new Vector2(xx, yy + 6));
								array.add(new Vector2(xx + 6, yy));
							}

							if (bb || !this.isValid(x - 1, y) || this.checkFor(x + 1, y, Terrain.SOLID) || this.checkFor(x + 1, y, Terrain.HOLE)) {
								array.add(new Vector2(xx + 16, yy));
							} else {
								array.add(new Vector2(xx + 16, yy + 6));
								array.add(new Vector2(xx + 10, yy));
							}

							if (this.checkFor(x - 1, y, Terrain.SOLID) || this.checkFor(x - 1, y, Terrain.HOLE)) {
								array.add(new Vector2(xx, yy + 12));
							} else {
								array.add(new Vector2(xx, yy + 6));
								array.add(new Vector2(xx + 6, yy + 12));
							}

							if (this.checkFor(x + 1, y, Terrain.SOLID) || this.checkFor(x + 1, y, Terrain.HOLE)) {
								array.add(new Vector2(xx + 16, yy + 12));
							} else {
								array.add(new Vector2(xx + 10, yy + 12));
								array.add(new Vector2(xx + 16, yy + 6));
							}

							poly.set(array.toArray(new Vector2[]{}));
						}

						FixtureDef fixture = new FixtureDef();

						fixture.shape = poly;
						fixture.friction = 0;

						body.createFixture(fixture);

						poly.dispose();
					}
				} else if (this.checkFor(x, y, Terrain.HOLE)) {
					int total = 0;

					for (Vector2 vec : NEIGHBOURS8V) {
						Vector2 v = new Vector2(x + vec.x, y + vec.y);

						if (this.isValid((int) v.x, (int) v.y) && (this.checkFor((int) v.x, (int) v.y, Terrain.HOLE) || this.checkFor((int) v.x, (int) v.y, Terrain.SOLID))) {
							total++;
						}
					}

					if (total < 8) {
						PolygonShape poly = new PolygonShape();
						int xx = x * 16;
						int yy = y * 16;


						if (this.checkFor(x, y + 1, Terrain.HOLE) || this.checkFor(x, y + 1, Terrain.SOLID)) {
							ArrayList<Vector2> array = new ArrayList<>();

							boolean bb = (!this.isValid(x, y - 1) || this.checkFor(x, y - 1, Terrain.HOLE) || this.checkFor(x, y - 1, Terrain.SOLID));

							if (bb || !this.isValid(x - 1, y) || this.checkFor(x - 1, y, Terrain.HOLE) || this.checkFor(x - 1, y, Terrain.SOLID)) {
								array.add(new Vector2(xx, yy));
							} else {
								array.add(new Vector2(xx, yy + 6));
								array.add(new Vector2(xx + 6, yy));
							}

							if (bb || !this.isValid(x - 1, y) || this.checkFor(x + 1, y, Terrain.HOLE) || this.checkFor(x + 1, y, Terrain.SOLID)) {
								array.add(new Vector2(xx + 16, yy));
							} else {
								array.add(new Vector2(xx + 16, yy + 4));
								array.add(new Vector2(xx + 10, yy));
							}

							array.add(new Vector2(xx, yy + 16));
							array.add(new Vector2(xx + 16, yy + 16));

							poly.set(array.toArray(new Vector2[]{}));
						} else {
							ArrayList<Vector2> array = new ArrayList<>();

							boolean bb = (!this.isValid(x, y - 1) || this.checkFor(x, y - 1, Terrain.HOLE) || this.checkFor(x, y - 1, Terrain.SOLID));

							if (bb || !this.isValid(x - 1, y) || this.checkFor(x - 1, y, Terrain.HOLE) || this.checkFor(x - 1, y, Terrain.SOLID)) {
								array.add(new Vector2(xx, yy));
							} else {
								array.add(new Vector2(xx, yy + 4));
								array.add(new Vector2(xx + 4, yy));
							}

							if (bb || !this.isValid(x - 1, y) || this.checkFor(x + 1, y, Terrain.HOLE) || this.checkFor(x + 1, y, Terrain.SOLID)) {
								array.add(new Vector2(xx + 16, yy));
							} else {
								array.add(new Vector2(xx + 16, yy + 4));
								array.add(new Vector2(xx + 12, yy));
							}

							if (this.checkFor(x - 1, y, Terrain.HOLE) || this.checkFor(x - 1, y, Terrain.SOLID)) {
								array.add(new Vector2(xx, yy + 8));
							} else {
								array.add(new Vector2(xx, yy + 4));
								array.add(new Vector2(xx + 4, yy + 8));
							}

							if (this.checkFor(x + 1, y, Terrain.HOLE) || this.checkFor(x + 1, y, Terrain.SOLID)) {
								array.add(new Vector2(xx + 16, yy + 8));
							} else {
								array.add(new Vector2(xx + 12, yy + 8));
								array.add(new Vector2(xx + 16, yy + 4));
							}

							poly.set(array.toArray(new Vector2[]{}));
						}

						FixtureDef fixture = new FixtureDef();

						fixture.shape = poly;
						fixture.friction = 0;

						chasms.createFixture(fixture);

						poly.dispose();
					}
				}
			}
		}
	}

	public void setDecor(int x, int y, byte v) {
		this.decor[toIndex(x, y)] = v;
	}

	public byte[] getVariants() {
		return this.variants;
	}

	public boolean explored(int x, int y) {
		return explored[toIndex(x, y)];
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

			if (room == null || room instanceof EntranceRoom) {
				continue;
			}

			for (int j = 0; j < 100; j++) {
				Point point = room.getRandomCell();
				int in = (int) (point.x + point.y * getWidth());

				if ((this.passable == null || this.passable[in]) && (this.free == null || !this.free[in])) {
					if (this.free != null) {
						this.free[in] = true;
					}

					return point;
				}
			}
		}

		return null;
	}

	public ArrayList<Room> getRooms() {
		return this.rooms;
	}

	public Room findRoomFor(float x, float y) {
		y += 4;

		for (Room room : this.rooms) {
			if (x > room.left * 16 + 8 && x < room.right * 16 + 8 && y > room.top * 16 + 8 && y < room.bottom * 16 + 8) {
				return room;
			}
		}

		return null;
	}

	public String getMusic() {
		return "";
	}

	@Override
	public void save(FileWriter writer) throws IOException {
		writer.writeInt32(getWidth());
		writer.writeInt32(getHeight());

		for (int i = 0; i < getSize(); i++) {
			writer.writeByte(this.data[i]);
			writer.writeByte(this.liquidData[i]);
			writer.writeByte(this.decor[i]);
			writer.writeBoolean(this.explored[i]);
		}

		writer.writeInt32(this.rooms.size());

		for (Room room : this.rooms) {
			writer.writeString(room.getClass().getName());
			writer.writeInt32(room.left);
			writer.writeInt32(room.top);
			writer.writeInt32(room.right);
			writer.writeInt32(room.bottom);
			writer.writeBoolean(room.hidden);
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

		writer.writeInt32(count);

		for (int i = 0; i < this.rooms.size(); i++) {
			Room room = this.rooms.get(i);

			for (Room n : room.neighbours) {
				int in = this.rooms.indexOf(n);

				if (in > -1) {
					writer.writeInt32(i);
					writer.writeInt32(in);
				}
			}
		}
	}

	@Override
	public void load(FileReader reader) throws IOException {
		setSize(reader.readInt32(), reader.readInt32());
		this.data = new byte[getSize()];
		this.liquidData = new byte[getSize()];
		this.decor = new byte[getSize()];
		this.explored = new boolean[getSize()];
		this.initLight();

		for (int i = 0; i < getSize(); i++) {
			this.data[i] = reader.readByte();
			this.liquidData[i] = reader.readByte();
			this.decor[i] = reader.readByte();
			this.explored[i] = reader.readBoolean();
		}

		try {
			int count = reader.readInt32();
			this.rooms = new ArrayList<>();

			for (int i = 0; i < count; i++) {
				String t = reader.readString();

				Class<?> clazz = Class.forName(t);
				Object object = clazz.newInstance();

				Room room = (Room) object;

				room.left = reader.readInt32();
				room.top = reader.readInt32();
				room.right = reader.readInt32();
				room.bottom = reader.readInt32();
				room.hidden = reader.readBoolean();

				this.rooms.add(room);
			}

			count = reader.readInt32();

			for (int i = 0; i < count; i++) {
				int in = reader.readInt32();
				this.rooms.get(in).neighbours.add(this.rooms.get(reader.readInt32()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}