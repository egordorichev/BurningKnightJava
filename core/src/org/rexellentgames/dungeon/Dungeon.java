package org.rexellentgames.dungeon;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.utils.GdxRuntimeException;
import org.rexellentgames.dungeon.assets.Assets;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.assets.Locale;
import org.rexellentgames.dungeon.assets.MusicManager;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.creature.mob.BurningKnight;
import org.rexellentgames.dungeon.entity.creature.mob.Mob;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.entities.Entrance;
import org.rexellentgames.dungeon.entity.level.entities.MagicWell;
import org.rexellentgames.dungeon.entity.level.levels.desert.DesertLevel;
import org.rexellentgames.dungeon.game.Area;
import org.rexellentgames.dungeon.game.Game;
import org.rexellentgames.dungeon.game.Ui;
import org.rexellentgames.dungeon.game.input.Input;
import org.rexellentgames.dungeon.game.state.*;
import org.rexellentgames.dungeon.physics.World;
import org.rexellentgames.dungeon.ui.UiLog;
import org.rexellentgames.dungeon.util.*;
import org.rexellentgames.dungeon.util.geometry.Point;

import java.io.File;

public class Dungeon extends ApplicationAdapter {
	public static ShaderProgram shader;

	public static Game game;
	public static int depth;
	public static float time;
	public static Level level;
	public static Area area;
	public static Area ui;
	public static Dungeon instance;
	public static boolean reset;
	public static byte ladderId;
	public static long longTime;
	public static boolean showed;
	public static Entrance.LoadType loadType = Entrance.LoadType.GO_DOWN;
	public static Type type = Type.REGULAR;
	public static float MAX_R = (float) (Math.sqrt(Display.GAME_WIDTH * Display.GAME_WIDTH + Display.GAME_HEIGHT * Display.GAME_HEIGHT) / 2);
	public static float darkR = MAX_R;
	public static float darkX = Display.GAME_WIDTH / 2;
	public static float darkY = Display.GAME_HEIGHT / 2;
	public static String[] arg;
	public static float speed = 1f;
	public static Color BLACK = Color.valueOf("#000000");
	public static Color GRAY = Color.valueOf("#696a6a");
	public static Color WHITE = Color.valueOf("#ffffff");
	public static Color ORANGE = Color.valueOf("#df7126");
	public static Color RED = Color.valueOf("#ac3232");
	public static Color GREEN = Color.valueOf("#6abe30");
	public static Color BLUE = Color.valueOf("#306082");
	public static Color YELLOW = Color.valueOf("#fbf236");
	public static Color BROWN = Color.valueOf("#8f563b");
	private static int to = -3;
	private Color background = Color.valueOf("#000000");
	private Color background2 = Color.valueOf("#323c39");
	public static SplashWorker worker;
	public static float shockTime = 10;
	public static Vector2 shockPos = new Vector2(0.5f, 0.5f);

	public static void reportException(Exception e) {
		Log.report(e);
	}

	@Override
	public void resume() {
		super.resume();
		game.getState().setPaused(wasPaused);
	}

	private boolean wasPaused;

	@Override
	public void pause() {
		super.pause();

		if (game.getState() instanceof InGameState) {
			this.wasPaused = game.getState().isPaused();
			game.getState().setPaused(true);
		}
	}

	public static void newGame() {
		reset = true;

		File file = Gdx.files.external(".bk/").file();

		for (File f : file.listFiles()) {
			f.delete();
		}

		loadType = Entrance.LoadType.GO_DOWN;

		if (to != -3) {
			Player.instance = null;
			BurningKnight.instance = null;
		}

		level = null;

		if (area != null) {
			area.destroy();
		}

		Dungeon.depth = 0;

		if (game != null) {
			game.setState(new LoadState());
		}
	}

	public static void goToLevel(int level) {
		to = level;
	}

	public static void slowDown(float a, float t) {
		Tween.to(new Tween.Task(a, 0.3f) {
			@Override
			public float getValue() {
				return speed;
			}

			@Override
			public void setValue(float value) {
				speed = value;
			}

			@Override
			public void onEnd() {
				Tween.to(new Tween.Task(1f, t, Tween.Type.BACK_IN) {
					@Override
					public float getValue() {
						return speed;
					}

					@Override
					public void setValue(float value) {
						speed = value;
					}
				});
			}
		});
	}

	@Override
	public void create() {
		instance = this;

		if (worker != null) {
			// worker.closeSplashScreen();
		}

		if (arg.length > 0 && arg[0].startsWith("reset")) {
			Dungeon.newGame();
		}

		Log.init();
		Settings.load();

		long seed = System.currentTimeMillis();

		Camera camera = new Camera();

		Log.info("Setting random seed to " + seed + "...");
		Random.random.setSeed(seed);

		Log.info("Loading locale...");
		Locale.load("en");

		this.setupCursor();
		Assets.init();

		String vertexShader;
		String fragmentShader;
		vertexShader = Gdx.files.internal("shaders/main.vert").readString();
		fragmentShader = Gdx.files.internal("shaders/main.frag").readString();
		shader = new ShaderProgram(vertexShader, fragmentShader);
		if (!shader.isCompiled()) throw new GdxRuntimeException("Couldn't compile shader: " + shader.getLog());

		Box2D.init();

		this.initColors();
		this.initInput();

		ui = new Area();
		area = new Area();

		game = new Game();
		game.setState(new AssetLoadState());

		area.add(camera);

		MusicManager.play("Gobbeon");
	}

	@Override
	public void render() {
		if (to > -2) {
			Dungeon.depth = to;

			game.setState(new LoadState());

			to = -2;
			return;
		}

		float dt = Gdx.graphics.getDeltaTime() * speed;
		time += dt;
		longTime += 1;

		if (Input.instance != null) {
			Input.instance.updateMousePosition();

			if (Input.instance.wasPressed("debug")) {
				Log.UI_LOG = !Log.UI_LOG;
				UiLog.instance.print(Log.UI_LOG ? "[orange]Debug logging is now on!" : "[green]Debug logging is now off!");
			}
		}

		Tween.update(dt);
		shockTime += dt;

		if (Ui.ui != null) {
			Ui.ui.update(dt);
		}

		boolean paused = game.getState().isPaused() || (game.getState() instanceof InGameState && InGameState.map);

		if (Input.instance.wasPressed("pause")) {
			game.getState().setPaused(!game.getState().isPaused());
		}

		if (!(game.getState() instanceof LoadState) && !paused) {
			area.update(dt);
		}

		Dungeon.ui.update(dt);

		if (!paused) {
			game.update(dt);
		}
		
		updateMouse(dt);

		Gdx.gl.glClearColor(this.background.r, this.background.g, this.background.b, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));

		renderGame();
		
		if (Input.instance != null) {
			Input.instance.update();
		}
	}

	private void renderGame() {
		Graphics.surface.begin();
		Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		Graphics.batch.begin();

		if (Level.SHADOWS) {
			// Clear shadows

			Graphics.startShadows();
			Gdx.gl.glClearColor(0, 0, 0, 0);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));
			Graphics.endShadows();

			for (int i = 0; i < area.getEntities().size(); i++) {
				Entity entity = area.getEntities().get(i);

				if (!entity.isActive()) {
					continue;
				}

				if (entity.onScreen || entity.alwaysRender) {
					entity.renderShadow();
				}
			}
		}

		if (Camera.instance != null) {
			Camera.instance.applyShake();
			Graphics.batch.setProjectionMatrix(Camera.instance.getCamera().combined);
			Graphics.shape.setProjectionMatrix(Camera.instance.getCamera().combined);
		}

		area.render();
		game.render(false);

		if (Camera.instance != null) {
			Camera.instance.removeShake();
		}

		Graphics.batch.end();
		Graphics.surface.end();
		Texture texture = Graphics.surface.getColorBufferTexture();

		Graphics.batch.setProjectionMatrix(Camera.ui.combined);

		Graphics.batch.begin();

		Graphics.batch.end();
		shader.begin();

		shader.setUniformf("shockTime", shockTime);
		shader.setUniformf("shockPos", shockPos);
		shader.setUniformf("heat", level instanceof DesertLevel ? 1 : 0);
		shader.setUniformf("time", Dungeon.time);
		shader.setUniformf("transR", darkR / MAX_R);
		shader.setUniformf("transPos", new Vector2(darkX / Display.GAME_WIDTH, darkY / Display.GAME_HEIGHT));
		shader.setUniformf("cam", new Vector2(Camera.instance.getCamera().position.x / 1024f, Camera.instance.getCamera().position.y / 1024f));
		shader.end();
		Graphics.batch.setShader(shader);
		Graphics.batch.begin();

		Graphics.batch.draw(texture, 0, 0, 0, 0, Display.GAME_WIDTH, Display.GAME_HEIGHT, 1, 1, 0, 0, 0, texture.getWidth(), texture.getHeight(),false, true);

		Graphics.batch.end();
		Graphics.batch.setShader(null);
		Graphics.batch.begin();

		game.renderUi();
		Graphics.batch.end();
	}

	private Point inputVel = new Point();

	private void updateMouse(float dt) {
		inputVel.mul(dt * 53f);

		float s = ((float) Gdx.graphics.getWidth()) / Display.GAME_WIDTH;

		if (Input.instance.wasPressed("circle")) {
			Input.instance.circle = !Input.instance.circle;
		}

		if (Input.instance.wasPressed("mouse_left")) {
			Log.info("left");
		}
//
//		if (Player.instance != null && Input.instance.circle) {
//			float ix = Input.instance.getAxis("mouseX") * s;
//			float iy = -Input.instance.getAxis("mouseY") * s;
//
//			// fixme; doesnt work
//			if (Input.instance.isDown("mouse_left")) {
//				ix = Math.max(-1, ix - 1);
//			}
//
//			if (Input.instance.isDown("mouse_right")) {
//				ix = Math.min(1, ix + 1);
//			}
//
//			if (Input.instance.isDown("mouse_down")) {
//				iy = Math.max(-1, iy - 1);
//			}
//
//			if (Input.instance.isDown("mouse_up")) {
//				iy = Math.min(1, iy + 1);
//			}
//
//			if (ix != 0 || iy != 0) {
//				float a = (float) Math.atan2(iy, ix);
//				angle.lerp(new Vector2((float) Math.cos(a), (float) Math.sin(a)), 0.08f);
//			}
//
//			float d = 64f;
//
//			Vector3 input = Camera.instance.getCamera().project(new Vector3(
//				Player.instance.x + Player.instance.w / 2 + angle.x * d,
//				Player.instance.y + Player.instance.h / 2 + angle.y * d, 0
//			));
//
//			Input.instance.mouse.x = input.x;
//			Input.instance.mouse.y = Gdx.graphics.getHeight() - input.y;
//
//			return;
//		}

		inputVel.x += Input.instance.getAxis("mouseX") * s;
		inputVel.y += Input.instance.getAxis("mouseY") * s;

		Input.instance.mouse.x += inputVel.x;
		Input.instance.mouse.y += inputVel.y;

		Input.instance.mouse.x = MathUtils.clamp(0, Gdx.graphics.getWidth(), Input.instance.mouse.x);
		Input.instance.mouse.y = MathUtils.clamp(0, Gdx.graphics.getHeight(), Input.instance.mouse.y);

		if (Input.instance.wasPressed("catch")) {
			Gdx.input.setCursorCatched(!Gdx.input.isCursorCatched());
		}
	}

	@Override
	public void resize(int width, int height) {
		if (Camera.instance != null) {
			Camera.instance.resize(width, height);
		}

		State state = game.getState();

		if (state != null) {
			state.resize(width, height);
		}

		Graphics.resize(width, height);
	}

	@Override
	public void dispose() {
		if (Player.instance != null && Player.instance.isDead()) {
			newGame();
		}

		if (area != null) {
			ui.destroy();
			area.destroy();
		}

		game.destroy();

		World.destroy();
		Assets.destroy();

		LoadState.writeDepth();
		Settings.save();
		Log.close();

		Mob.shader.dispose();
		BurningKnight.shader.dispose();
		Level.waterShader.dispose();
		MagicWell.shader.dispose();
		shader.dispose();
	}

	private void initInput() {
		Controllers.addListener(new Input());
	}

	private void setupCursor() {
		Pixmap pm = new Pixmap(8, 8, Pixmap.Format.RGBA8888);
		pm.setBlending(null);
		pm.setColor(0, 0, 0, 0);
		Gdx.graphics.setCursor(Gdx.graphics.newCursor(pm, 0, 0));
		pm.dispose();
	}

	private void initColors() {
		Colors.put("black", Color.valueOf("#000000"));
		Colors.put("gray", Color.valueOf("#696a6a"));
		Colors.put("white", Color.valueOf("#ffffff"));
		Colors.put("orange", Color.valueOf("#df7126"));
		Colors.put("red", Color.valueOf("#ac3232"));
		Colors.put("green", Color.valueOf("#6abe30"));
		Colors.put("blue", Color.valueOf("#306082"));
		Colors.put("yellow", Color.valueOf("#fbf236"));
		Colors.put("brown", Color.valueOf("#8f563b"));
	}

	public enum Type {
		REGULAR,
		INTRO,
		ARCADE
	}
}
