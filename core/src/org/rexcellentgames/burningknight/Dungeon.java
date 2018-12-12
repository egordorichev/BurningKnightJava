package org.rexcellentgames.burningknight;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Window;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
import org.rexcellentgames.burningknight.assets.Audio;
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
import org.rexcellentgames.burningknight.entity.level.rooms.HandmadeRoom;
import org.rexcellentgames.burningknight.entity.level.save.GameSave;
import org.rexcellentgames.burningknight.entity.level.save.SaveManager;
import org.rexcellentgames.burningknight.game.Achievements;
import org.rexcellentgames.burningknight.game.Area;
import org.rexcellentgames.burningknight.game.Game;
import org.rexcellentgames.burningknight.game.Ui;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.game.state.*;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Dialog;
import org.rexcellentgames.burningknight.util.Log;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.Tween;
import org.rexcellentgames.burningknight.util.geometry.Point;

import java.io.IOException;
import java.util.Date;

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
	public static Entrance.LoadType loadType = Entrance.LoadType.LOADING;
	public static Type type = Type.REGULAR;
	public static float MAX_R = (float) (Math.sqrt(Display.GAME_WIDTH * Display.GAME_WIDTH + Display.GAME_HEIGHT * Display.GAME_HEIGHT) / 2);
	public static float darkR = MAX_R;
	public static float darkX = Display.GAME_WIDTH / 2;
	public static float darkY = Display.GAME_HEIGHT / 2;
	public static String[] arg;
	public static float speed = 1f;
	public static Color ORANGE = Color.valueOf("#df7126");
	public static Color YELLOW = Color.valueOf("#fbf236");
	public static int to = -10;
	private static Color background = Color.BLACK;
	private static Color background2 = Color.BLACK;
	public static float shockTime = 10;
	public static float glitchTime = 0;
	public static Vector2 shockPos = new Vector2(0.5f, 0.5f);
	public static float flip = -1;
	public static float colorBlind = 0f;
	public static float colorBlindFix = 1f;
	public static float grayscale = 0f;
	public static float dark = 1f;
	public static boolean goToMenu;
	public static float battleDarkness;
	public static float white;
	public static boolean goToSelect;
	public static float blood;
	public static Color flashColor;
	public static float flashTime;

	public static void flash(Color color, float time) {
		flashColor = color;
		flashTime = time * Settings.flash_frames * 2f;
	}

	public static String title;

	public static void reportException(Exception e) {
		Log.report(e);
	}

	public static Color getBackground() {
		return background;
	}

	public static void setBackground(Color background) {
		Dungeon.background = background;
	}

	public static Color getBackground2() {
		return background2;
	}

	public static void setBackground2(Color background2) {
		Dungeon.background2 = background2;
	}

	@Override
	public void resume() {
		super.resume();
		if (game.getState() instanceof InGameState && !Version.debug) {
			game.getState().setPaused(wasPaused);
		}
	}

	private boolean wasPaused;

	@Override
	public void pause() {
		super.pause();

		if (game.getState() instanceof InGameState && !Version.debug) {
			this.wasPaused = game.getState().isPaused();
			game.getState().setPaused(true);
		}
	}


	public static void newGame() {
		newGame(false, -1);
	}

	public static boolean quick;

	public static void backToCastle(boolean quick, int depth) {
		reset = true;
		Dungeon.quick = quick;
		SaveManager.delete();
		loadType = Entrance.LoadType.GO_DOWN;

		Player.instance = null;
		BurningKnight.instance = null;

		level = null;

		if (area != null) {
			area.destroy();
		}

		Dungeon.depth = quick ? depth : (Dungeon.depth == -3 ? -3 : -2);

		if (Dungeon.depth == -3) {
			quick = false;
		}

		game.setState(new LoadState());
	}
	public static Lwjgl3Window window;

	public static void newGame(boolean quick, int depth) {
		reset = true;
		Dungeon.quick = quick;
		SaveManager.delete();
		GameSave.time = 0;
		loadType = Entrance.LoadType.GO_DOWN;
		GameSave.runId ++;

		Player.instance = null;
		BurningKnight.instance = null;

		level = null;

		if (area != null) {
			area.destroy();
		}

		Dungeon.depth = quick ? depth : (Dungeon.depth == -3 ? -3 : -2);

		if (Dungeon.depth == -3) {
			quick = false;
		}

		// fixme
		if (quick && Dungeon.depth != -1) {
			ItemSelectState.depth = Dungeon.depth;
			game.setState(new ItemSelectState());
		} else {
			game.setState(new LoadState());
		}
	}

	public static boolean toInventory;

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
		if (Dungeon.game.getState() instanceof MainMenuState) {
			DiscordRPC.discordUpdatePresence(new DiscordRichPresence.Builder("Main menu").setDetails(Version.string).setBigImage("hero_mercy", "").setSmallImage("hero_mercy", "").build());
		} else if (Dungeon.game.getState() instanceof InventoryState) {
			DiscordRPC.discordUpdatePresence(new DiscordRichPresence.Builder("Portal").setDetails(Version.string).setBigImage("hero_mercy", "").setSmallImage("hero_mercy", "").build());
		} else if (Dungeon.level != null) {
			String type = Player.instance.getType().toString().toLowerCase();
			type = type.substring(0, 1).toUpperCase() + type.substring(1);

			DiscordRPC.discordUpdatePresence(new DiscordRichPresence.Builder(Dungeon.level.formatDepth()).setDetails(Version.string)
				.setBigImage("hero_mercy", type).setTimestamps(startTime, 0)
				.setSmallImage("hero_mercy", type).build());
		}
	}

	private static long startTime = System.currentTimeMillis() / 1000;

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
		Log.info("Burning knight " + Version.string);
		Log.info(new Date().toString());
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

		ui = new Area(true);
		area = new Area(true);

		game = new Game();
		game.setState(new AssetLoadState());

		area.add(camera);
	}

	public static int lastDepth;
	private float lastUpdate;

	@Override
	public void render() {
		if (goToMenu) {
			goToMenu = false;

			Tween.to(new Tween.Task(0, 0.2f) {
				@Override
				public float getValue() {
					return Dungeon.dark;
				}

				@Override
				public void setValue(float value) {
					Dungeon.dark = value;
				}

				@Override
				public void onEnd() {
					game.setState(new MainMenuState());
				}

				@Override
				public boolean runWhenPaused() {
					return true;
				}
			});

			return;
		}

		if (goToSelect) {
			goToSelect = false;
			ItemSelectState.depth = 1;
			game.setState(new ItemSelectState());

			return;
		}

		if (Version.debug && Input.instance.wasPressed("1")) {
			InventoryState.depth = Dungeon.depth + 1;
			GameSave.inventory = true;
			toInventory = true;
		}

		if (toInventory) {
			toInventory = false;
			game.setState(Dungeon.depth == 4 ? new WonState() : new InventoryState());
			return;
		}

		if (AssetLoadState.done && to > -10) {
			Dungeon.lastDepth = depth;
			Dungeon.depth = to;

			game.setState(to == 5 ? new WonState() : new LoadState());

			Gdx.gl.glClearColor(0, 0, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));

			to = -10;
			return;
		}

		update();

		Gdx.gl.glClearColor(getBackground().r, getBackground().g, getBackground().b, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));

		renderGame();
		renderUi();

		if (Input.instance != null) {
			Input.instance.update();
		}
	}

	public static float fpsY;

	private void update() {
		if (Graphics.delayTime > 0) {
			Graphics.delayTime -= Gdx.graphics.getDeltaTime();
			return;
		}

		float dt = Math.min(0.04f, Gdx.graphics.getDeltaTime()) * speed;
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

		if (AssetLoadState.done) {
			Input.instance.updateMousePosition();
		}

		Audio.update(dt);
		Tween.update(dt);
		shockTime += dt;
		glitchTime = Math.max(0, glitchTime - dt);

		if (Ui.ui != null) {
			Ui.ui.update(dt);
		}

		if (Input.instance.wasPressed("F2")) {
			Tween.to(new Tween.Task(fpsY == 0 ? 18 : 0, 0.3f, Tween.Type.BACK_OUT) {
				@Override
				public float getValue() {
					return fpsY;
				}

				@Override
				public void setValue(float value) {
					fpsY = value;
				}

				@Override
				public boolean runWhenPaused() {
					return true;
				}
			});
		} else if (Input.instance.wasPressed("F11")) {
			Settings.fullscreen = !Settings.fullscreen;

			if (Settings.fullscreen) {
				Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
			} else {
				Gdx.graphics.setWindowedMode(Display.UI_WIDTH_MAX * 2, Display.UI_HEIGHT_MAX * 2);
			}
		} else if (Input.instance.wasPressed("F7")) {
			if (Settings.music > 0.1f) {
				Settings.music = 0;
				Audio.update();
			} else {
				Settings.music = 0.5f;
				Audio.update();
			}
		} else if (Input.instance.wasPressed("F8")) {
			if (Settings.sfx > 0.1f) {
				Settings.sfx = 0;
			} else {
				Settings.sfx = 0.5f;
			}
		} else if (Input.instance.wasPressed("pause") && Dungeon.darkR == Dungeon.MAX_R && game.getState() instanceof InGameState && !Player.instance.isDead() && Dialog.active == null && !game.getState().isPaused()) {
			game.getState().setPaused(true);
			Audio.playSfx("menu/select");
		}

		// Stopped working
		/*if (Input.instance.wasPressed("F10")) {
			colorBlindFix = colorBlindFix > 0.5f ? 0f : 1f;
		}*/

		boolean paused = game.getState() != null && game.getState().isPaused();

		if (!(game.getState() instanceof LoadState) && !paused) {
			area.update(dt);
		}


		Dungeon.ui.update(dt);
		Achievements.update(dt);

		game.update(dt);

		if (AssetLoadState.done) {
			updateMouse(dt);
		}
	}

	public static TextureRegion[] sideArt;

	private void renderGame() {
		if (sideArt == null) {
			sideArt = new TextureRegion[] {
				Graphics.getTexture("side_art-0")
			};
		}

		final float upscale = Math.min(((float) Gdx.graphics.getWidth()) / Display.GAME_WIDTH, ((float) Gdx.graphics.getHeight()) / Display.GAME_HEIGHT) * Ui.upscale;
		Camera.applyShake();

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

		if (Settings.side_art != 0 && Settings.side_art <= sideArt.length) {
			Graphics.batch.begin();
			Graphics.batch.setProjectionMatrix(Camera.viewportCamera.combined);

			int sz = 64 * 2;
			for (float x = 0; x < Gdx.graphics.getWidth(); x += sz) {
				for (float y = 0; y < Gdx.graphics.getHeight(); y += sz) {
					Graphics.batch.draw(sideArt[Settings.side_art - 1], x - Gdx.graphics.getWidth() * 0.5f, y - Gdx.graphics.getHeight() * 0.5f,
						sz, sz);
				}
			}

			Graphics.batch.end();
		}

		Graphics.batch.setProjectionMatrix(Camera.game.combined);
		Graphics.shape.setProjectionMatrix(Camera.game.combined);

		if (game.getState() instanceof InGameState && Level.SHADOWS) {
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

		Gdx.gl.glClearColor(getBackground2().r, getBackground2().g, getBackground2().b, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		Graphics.batch.begin();

		if (!(game.getState() instanceof LoadState)) {
			area.render();
		}

		game.render(false);

		Graphics.batch.end();
		Graphics.surface.end();
		Camera.removeShake();

		Graphics.batch.setProjectionMatrix(Camera.viewportCamera.combined);

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
		shader.setUniformf("grayscale", grayscale);
		shader.setUniformf("blood", blood);
		shader.setUniformf("ui", 0);
		shader.setUniformf("white", white);
		shader.setUniformf("battle", battleDarkness);
		shader.setUniformf("heat", 0); // level instanceof DesertLevel ? 1 :
		shader.setUniformf("time", Dungeon.time);
		shader.setUniformf("transR", darkR / MAX_R);
		shader.setUniformf("dark", dark);
		shader.setUniformf("transPos", new Vector2(darkX / Display.GAME_WIDTH, darkY / Display.GAME_HEIGHT));
		shader.setUniformf("cam", new Vector2(Camera.game.position.x / 1024f, Camera.game.position.y / 1024f));

		Graphics.batch.setColor(1, 1, 1, 1);

		Graphics.batch.setProjectionMatrix(Graphics.batch.getProjectionMatrix().rotate(0, 0, 1, Camera.ma));
		Graphics.batch.draw(texture, -Display.GAME_WIDTH * upscale / 2, (flip * -1) * Display.GAME_HEIGHT * upscale / 2, (Display.GAME_WIDTH) * upscale,
			(flip) * (Display.GAME_HEIGHT) * upscale);
		Graphics.batch.setProjectionMatrix(Graphics.batch.getProjectionMatrix().rotate(0, 0, 1, -Camera.ma));


		Graphics.batch.end();
		Graphics.batch.setShader(null);

		// Gdx.gl20.glDisable(GL20.GL_SCISSOR_TEST);
	}

	public static float timerY = 0;

	public static void tweenTimer(boolean on) {
		Tween.to(new Tween.Task(on ? 18 : 0, 0.3f, Tween.Type.BACK_OUT) {
			@Override
			public float getValue() {
				return timerY;
			}

			@Override
			public void setValue(float value) {
				timerY = value;
			}

			@Override
			public boolean runWhenPaused() {
				return true;
			}
		});
	}

	public void renderUi() {
		Graphics.batch.setProjectionMatrix(Camera.ui.combined);
		Graphics.shape.setProjectionMatrix(Camera.ui.combined);

		final float upscale = Math.min(((float) Gdx.graphics.getWidth()) / Display.UI_WIDTH, ((float) Gdx.graphics.getHeight()) / Display.UI_HEIGHT);

		Graphics.shadows.begin();

		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Graphics.batch.begin();

		game.renderUi();
		Ui.renderSaveIcon(upscale);

		if (fpsY > 0) {
			int f = Gdx.graphics.getFramesPerSecond();

			if (f >= 59) {
				Graphics.small.setColor(0, 1, 0, 1);
			} else if (f >= 49) {
				Graphics.small.setColor(1, 0.5f, 0, 1);
			} else {
				Graphics.small.setColor(1, 0, 0, 1);
			}

			Graphics.print(Integer.toString(f), Graphics.small, 2, Display.UI_HEIGHT - fpsY + 8);
			Graphics.small.setColor(1, 1, 1, 1);
		}

		if (timerY > 0 && !(game.getState() instanceof MainMenuState)) {
			String time = String.format("%02d:%02d:%02d:%02d", (int) Math.floor(GameSave.time / 3600), (int)
				Math.floor(GameSave.time / 60), (int) Math.floor(GameSave.time % 60), (int) Math.floor(GameSave.time % 1 * 100));

			Graphics.print(time, Graphics.small, 2 + fpsY, Display.UI_HEIGHT - timerY + 8);
		}

		Achievements.render();

		Graphics.batch.end();
		Graphics.shadows.end();

		Graphics.batch.setProjectionMatrix(Camera.viewportCamera.combined);

		Texture texture = Graphics.shadows.getColorBufferTexture();
		texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

		Graphics.batch.begin();
		Graphics.batch.setShader(shader);

		if (flashTime > 0) {
			flashTime -= Gdx.graphics.getDeltaTime();
			shader.setUniformf("ft", 1);
			shader.setUniformf("fr", flashColor.r);
			shader.setUniformf("fg", flashColor.g);
			shader.setUniformf("fb", flashColor.b);
		} else {
			shader.setUniformf("ft", 0);
		}

		shader.setUniformf("u_sampleProperties", 0, 0, 0, 0);
		shader.setUniformf("shockTime", 10);
		shader.setUniformf("glitchT", 0);
		shader.setUniformf("heat", 0);
		shader.setUniformf("ui", 1);
		shader.setUniformf("white", white);
		shader.setUniformf("battle", 0);
		shader.setUniformf("grayscale", 0);

		Graphics.batch.setColor(1, 1, 1, 1);
		Graphics.batch.draw(texture, -Display.UI_WIDTH * upscale / 2, Display.UI_HEIGHT * upscale / 2, Display.UI_WIDTH * upscale,
			-Display.UI_HEIGHT * upscale);

		Graphics.batch.setShader(null);
		Graphics.batch.end();
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
		}
	}

	@Override
	public void resize(int width, int height) {
		Camera.resize(width, height);

		Input.instance.mouse.x = Gdx.input.getX();
		Input.instance.mouse.y = Gdx.input.getY();

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
		HandmadeRoom.destroy();

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
			InGameState.shader.dispose();
		}

		shutdownDiscord();
		SteamAPI.shutdown();
	}

	private void initInput() {
		Controllers.addListener(new Input());
	}

	public static Cursor cursor;

	private void setupCursor() {
		Pixmap pm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		pm.setBlending(null);
		pm.setColor(0, 0, 0, 0);
		cursor = Gdx.graphics.newCursor(pm, 0, 0);
		Gdx.graphics.setCursor(cursor);
		pm.dispose();
	}

	private void initColors() {
		Colors.put("black", Color.valueOf("#000000"));
		Colors.put("gray", Color.valueOf("#b4b4b4"));
		Colors.put("white", Color.valueOf("#ffffff"));
		Colors.put("orange", Color.valueOf("#ff5000"));
		Colors.put("red", Color.valueOf("#ff0040"));
		Colors.put("green", Color.valueOf("#5ac54f"));
		Colors.put("blue", Color.valueOf("#0069aa"));
		Colors.put("cyan", Color.valueOf("#00cdf9"));
		Colors.put("yellow", Color.valueOf("#ffc825"));
		Colors.put("brown", Color.valueOf("#8a4836"));
	}

	public enum Type {
		REGULAR,
		INTRO,
		ARCADE
	}
}
