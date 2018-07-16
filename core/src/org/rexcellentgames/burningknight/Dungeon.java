package org.rexcellentgames.burningknight;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.codedisaster.steamworks.SteamAPI;
import com.codedisaster.steamworks.SteamException;
import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import net.arikia.dev.drpc.DiscordUser;
import net.arikia.dev.drpc.callbacks.ReadyCallback;
import org.rexcellentgames.burningknight.assets.Assets;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.mob.boss.BurningKnight;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.weapon.WeaponBase;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.fx.RectFx;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.entities.Entrance;
import org.rexcellentgames.burningknight.entity.level.entities.MagicWell;
import org.rexcellentgames.burningknight.entity.level.levels.desert.DesertLevel;
import org.rexcellentgames.burningknight.entity.level.save.SaveManager;
import org.rexcellentgames.burningknight.game.Achievements;
import org.rexcellentgames.burningknight.game.Area;
import org.rexcellentgames.burningknight.game.Game;
import org.rexcellentgames.burningknight.game.Ui;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.game.state.AssetLoadState;
import org.rexcellentgames.burningknight.game.state.InGameState;
import org.rexcellentgames.burningknight.game.state.LoadState;
import org.rexcellentgames.burningknight.game.state.State;
import org.rexcellentgames.burningknight.mod.ModManager;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Log;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.Tween;
import org.rexcellentgames.burningknight.util.geometry.Point;

import java.io.IOException;

public class Dungeon extends ApplicationAdapter {
	public static ShaderProgram shader;

	public static boolean notLoaded = true;
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
	public static Color background = Color.BLACK;
	public static Color background2 = Color.BLACK;
	public static float shockTime = 10;
	public static float glitchTime = 0;
	public static Vector2 shockPos = new Vector2(0.5f, 0.5f);
	public static boolean flip;
	public static float colorBlind = 0f;
	public static float colorBlindFix = 1f;

	public static String title;

	public static void reportException(Exception e) {
		Log.report(e);
	}

	@Override
	public void resume() {
		super.resume();
		if (game.getState() instanceof InGameState) {
			game.getState().setPaused(wasPaused);
		}
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

		SaveManager.delete();

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

	public static void slowDown(float a, final float t) {
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

	public static boolean steam = true;

	private static void initDiscord() {
		DiscordEventHandlers handlers = new DiscordEventHandlers.Builder().setReadyEventHandler(new ReadyCallback() {
			@Override
			public void apply(DiscordUser user) {

			}
		}).build();

		DiscordRPC.discordInitialize("459603244256198657", handlers, true);
	}

	private static void shutdownDiscord() {
		DiscordRPC.discordShutdown();
	}

	public static void buildDiscordBadge() {
		if (Dungeon.level != null) {
			String type = Player.instance.getType().toString().toLowerCase();
			type = type.substring(0, 1).toUpperCase() + type.substring(1);

			DiscordRPC.discordUpdatePresence(new DiscordRichPresence.Builder(Dungeon.level.formatDepth()).setDetails(Version.string)
				.setBigImage("hero_mercy", type)
				.setSmallImage("hero_mercy", type).build());
		} else {
			DiscordRPC.discordUpdatePresence(new DiscordRichPresence.Builder("Main menu").setDetails(Version.string).setBigImage("hero_mercy", "").setSmallImage("hero_mercy", "").build());
		}
	}

	@Override
	public void create() {
		try {
			if (!SteamAPI.init()) {
				steam = false;
			}
		} catch (SteamException e) {
			e.printStackTrace();
			steam = false;
		}

		instance = this;

		if (arg.length > 0 && arg[0].startsWith("reset")) {
			Dungeon.newGame();
		}

		Log.init();
		Log.info("Loading from " + (steam ? "Steam" : "native"));

		initDiscord();

		loadGlobal();
		Achievements.init();
		Settings.load();

		long seed = System.currentTimeMillis();

		org.rexcellentgames.burningknight.entity.Camera camera = new org.rexcellentgames.burningknight.entity.Camera();

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
	}

	public static int lastDepth;
	private float lastUpdate;

	@Override
	public void render() {
		if (AssetLoadState.done && to > -2) {
			Dungeon.lastDepth = depth;
			Dungeon.depth = to;

			game.setState(new LoadState());

			to = -2;
			return;
		}

		float dt = Gdx.graphics.getDeltaTime() * speed;
		time += dt;
		longTime += 1;
		this.lastUpdate += dt;

		if (this.lastUpdate >= 0.06f) {
			if (SteamAPI.isSteamRunning()) {
				SteamAPI.runCallbacks();
			}

			DiscordRPC.discordRunCallbacks();
			this.lastUpdate = 0;
		}

		if (Input.instance != null) {
			Input.instance.updateMousePosition();
		}

		Tween.update(dt);
		shockTime += dt;
		glitchTime = Math.max(0, glitchTime - dt);

		if (Ui.ui != null) {
			Ui.ui.update(dt);
		}

		if (Input.instance.wasPressed("switch_colorblind")) {
			colorBlind = (colorBlind + 1) % 4;
		}

		if (Input.instance.wasPressed("toggle_correction")) {
			colorBlindFix = colorBlindFix > 0.5f ? 0f : 1f;
		}

		if (Input.instance.wasPressed("pause") && game.getState() instanceof InGameState) {
			game.getState().setPaused(!game.getState().isPaused());
		}
		boolean paused = game.getState().isPaused() || (game.getState() instanceof InGameState && InGameState.map);

		if (!(game.getState() instanceof LoadState) && !paused) {
			area.update(dt);
		}

		Dungeon.ui.update(dt);

		game.update(dt);
		ModManager.INSTANCE.update(dt);

		updateMouse(dt);

		Gdx.gl.glClearColor(background.r, background.g, background.b, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));

		renderGame();
		renderUi();

		if (Input.instance != null) {
			Input.instance.update();
		}
	}

	private void renderGame() {
		Camera.applyShake();

		final float upscale = Math.min(((float) Gdx.graphics.getWidth()) / Display.GAME_WIDTH, ((float) Gdx.graphics.getHeight()) / Display.GAME_HEIGHT);

		float sceneX = Camera.game.position.x;
		float sceneY = Camera.game.position.y;

		float sceneIX = MathUtils.floor(sceneX);
		float sceneIY = MathUtils.floor(sceneY);

		float upscaleOffsetX = (sceneX - sceneIX) * upscale;
		float upscaleOffsetY = (sceneY - sceneIY) * upscale;

		float subpixelX = 0;
		float subpixelY = 0;

		upscaleOffsetX -= subpixelX;
		upscaleOffsetY -= subpixelY;

		Camera.game.position.set(sceneIX, sceneIY, 0);
		Camera.game.update();

		Graphics.batch.setProjectionMatrix(Camera.game.combined);
		Graphics.shape.setProjectionMatrix(Camera.game.combined);

		if (Level.SHADOWS) {
			// Clear shadows

			Graphics.shadows.begin();

			Gdx.gl.glClearColor(0, 0, 0, 0);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));
			Graphics.shape.begin(ShapeRenderer.ShapeType.Filled);
			Graphics.shape.setProjectionMatrix(Camera.game.combined);
			Graphics.shape.setColor(1, 1, 1, 1);

			for (int i = 0; i < area.getEntities().size(); i++) {
				Entity entity = area.getEntities().get(i);

				if (!entity.isActive()) {
					continue;
				}

				if (entity.onScreen || entity.alwaysRender) {
					entity.renderShadow();
				}
			}

			Graphics.shape.end();

			Graphics.shadows.end(Camera.viewport.getScreenX(), Camera.viewport.getScreenY(),
				Camera.viewport.getScreenWidth(), Camera.viewport.getScreenHeight());
		}

		Graphics.surface.begin();

		Gdx.gl.glClearColor(background.r, background.g, background.b, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		Graphics.batch.begin();

		area.render();
		game.render(false);

		Graphics.batch.end();
		Graphics.surface.end();
		Camera.removeShake();

		Graphics.batch.setProjectionMatrix(Camera.viewportCamera.combined);
		//HdpiUtils.glScissor((int) (upscale / 2), (int) (upscale / 2), (int) (Display.GAME_WIDTH * upscale - upscale), (int) (Display.GAME_HEIGHT * upscale - upscale));

		Texture texture = Graphics.surface.getColorBufferTexture();
		texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

		Graphics.batch.begin();
		Graphics.batch.setShader(shader);

		shader.setUniformf("u_textureSizes", Display.GAME_WIDTH, Display.GAME_HEIGHT, upscale, 0.0f);
		shader.setUniformf("u_sampleProperties", subpixelX, subpixelY, upscaleOffsetX, upscaleOffsetY);
		shader.setUniformf("shockTime", shockTime);
		shader.setUniformf("glitchT", glitchTime);
		shader.setUniformf("shockPos", shockPos);
		shader.setUniformf("colorBlind", colorBlind);
		shader.setUniformf("correct", colorBlindFix);
		shader.setUniformf("heat", level instanceof DesertLevel ? 1 : 0);
		shader.setUniformf("time", Dungeon.time);
		shader.setUniformf("transR", darkR / MAX_R);
		shader.setUniformf("transPos", new Vector2(darkX / Display.GAME_WIDTH, darkY / Display.GAME_HEIGHT));
		shader.setUniformf("cam", new Vector2(Camera.game.position.x / 1024f, Camera.game.position.y / 1024f));

		Graphics.batch.setColor(1, 1, 1, 1);

		Graphics.batch.draw(texture, -Display.GAME_WIDTH * upscale / 2, Display.GAME_HEIGHT * upscale / 2, Display.GAME_WIDTH * upscale,
			-Display.GAME_HEIGHT * upscale);
		Graphics.batch.end();
		Graphics.batch.setShader(null);

		Gdx.gl20.glDisable(GL20.GL_SCISSOR_TEST);
	}

	public void renderUi() {
		Graphics.batch.setProjectionMatrix(Camera.ui.combined);
		Graphics.shape.setProjectionMatrix(Camera.ui.combined);

		final float upscale = Math.min(((float) Gdx.graphics.getWidth()) / Display.GAME_WIDTH, ((float) Gdx.graphics.getHeight()) / Display.GAME_HEIGHT);

		Graphics.shadows.begin();
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Graphics.batch.begin();
		game.renderUi();
		ModManager.INSTANCE.draw();
		Graphics.batch.end();
		Graphics.shadows.end();

		Graphics.batch.setProjectionMatrix(Camera.viewportCamera.combined);
		//HdpiUtils.glScissor((int) upscale / 2, (int) upscale / 2, (int) (Display.GAME_WIDTH * upscale - upscale), (int) (Display.GAME_HEIGHT * upscale - upscale));

		Texture texture = Graphics.shadows.getColorBufferTexture();
		texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

		Graphics.batch.begin();
		Graphics.batch.setShader(shader);

		shader.setUniformf("u_textureSizes", Display.GAME_WIDTH, Display.GAME_HEIGHT, upscale, 0.0f);
		shader.setUniformf("u_sampleProperties", 0, 0, 0, 0);
		shader.setUniformf("shockTime", 10);
		shader.setUniformf("glitchT", 0);
		shader.setUniformf("shockPos", shockPos);
		shader.setUniformf("colorBlind", colorBlind);
		shader.setUniformf("correct", colorBlindFix);
		shader.setUniformf("heat", 0);
		shader.setUniformf("time", Dungeon.time);
		shader.setUniformf("transR", darkR / MAX_R);
		shader.setUniformf("transPos", new Vector2(darkX / Display.GAME_WIDTH, darkY / Display.GAME_HEIGHT));
		shader.setUniformf("cam", new Vector2(Camera.game.position.x / 1024f, Camera.game.position.y / 1024f));

		Graphics.batch.setColor(1, 1, 1, 1);
		Graphics.batch.draw(texture, -Display.GAME_WIDTH * upscale / 2, Display.GAME_HEIGHT * upscale / 2, Display.GAME_WIDTH * upscale,
			-Display.GAME_HEIGHT * upscale);
		Graphics.batch.end();
		Graphics.batch.setShader(null);
	}

	private Point inputVel = new Point();
	private Vector2 angle = new Vector2(0.0001f, 1.0f);

	private void updateMouse(float dt) {
		inputVel.mul(dt * 53f);

		float s = ((float) Gdx.graphics.getWidth()) / Display.GAME_WIDTH;

		if (Player.instance != null && Input.instance.activeController != null) {
			float ix = Input.instance.getAxis("mouseX") * s;
			float iy = -Input.instance.getAxis("mouseY") * s;

			if (Math.sqrt(ix * ix + iy * iy) > 0.1) {
        float a = (float) Math.atan2(iy, ix);
				angle.lerp(new Vector2((float) Math.cos(a), (float) Math.sin(a)), 0.08f);
			}

			float d = 64f;

			Vector3 input = Camera.game.project(new Vector3(
				Player.instance.x + Player.instance.w / 2 + angle.x * d,
				Player.instance.y + Player.instance.h / 2 + angle.y * d, 0
			));

			Input.instance.mouse.x = input.x;
			Input.instance.mouse.y = Gdx.graphics.getHeight() - input.y;

			return;
		}

		/*inputVel.x += Input.instance.getAxis("mouseX") * s;
		inputVel.y += Input.instance.getAxis("mouseY") * s;

		Input.instance.mouse.x += inputVel.x;
		Input.instance.mouse.y += inputVel.y;

		Input.instance.mouse.x = MathUtils.clamp(0, Gdx.graphics.getWidth(), Input.instance.mouse.x);
		Input.instance.mouse.y = MathUtils.clamp(0, Gdx.graphics.getHeight(), Input.instance.mouse.y);*/
	}

	@Override
	public void resize(int width, int height) {
		if (org.rexcellentgames.burningknight.entity.Camera.instance != null) {
			Camera.resize(width, height);
		}

		State state = game.getState();

		if (state != null) {
			state.resize(width, height);
		}

		Graphics.resize(width, height);
	}

	public void saveGlobal() {
		SaveManager.save(SaveManager.Type.GLOBAL, false);
	}

	public void loadGlobal() {
		try {
			SaveManager.load(SaveManager.Type.GLOBAL);
		} catch (IOException e) {
			e.printStackTrace();
			Log.error("Failed to load global save, generating a new one");
			SaveManager.generate(SaveManager.Type.GLOBAL);
		}
	}

	@Override
	public void dispose() {
		ModManager.INSTANCE.destroy();

		if (area != null) {
			ui.destroy();
			area.destroy();
		}

		game.destroy();

		World.destroy();
		Assets.destroy();

		Settings.save();
		Achievements.dispose();
		saveGlobal();
		Log.close();

		if (Player.shader != null) {
			Player.shader.dispose();
			Mob.shader.dispose();
			Mob.frozen.dispose();
			BurningKnight.shader.dispose();
			Level.maskShader.dispose();
			Level.shader.dispose();
			MagicWell.shader.dispose();
			WeaponBase.shader.dispose();
			RectFx.shader.dispose();
			shader.dispose();
		}

		shutdownDiscord();
		SteamAPI.shutdown();
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
