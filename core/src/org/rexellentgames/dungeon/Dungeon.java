package org.rexellentgames.dungeon;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.World;
import com.bitfire.postprocessing.PostProcessor;
import com.bitfire.postprocessing.effects.CrtMonitor;
import com.bitfire.postprocessing.filters.Combine;
import com.bitfire.postprocessing.filters.CrtScreen;
import org.rexellentgames.dungeon.assets.Assets;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.assets.Locale;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.entity.creature.mob.BurningKnight;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.entities.Entrance;
import org.rexellentgames.dungeon.game.Area;
import org.rexellentgames.dungeon.game.Game;
import org.rexellentgames.dungeon.game.Ui;
import org.rexellentgames.dungeon.game.input.Input;
import org.rexellentgames.dungeon.game.state.*;
import org.rexellentgames.dungeon.net.Network;
import org.rexellentgames.dungeon.net.Packets;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.Random;
import org.rexellentgames.dungeon.util.Tween;

import java.io.File;

public class Dungeon extends ApplicationAdapter {
	public static Game game;
	public static int depth;
	public static float time;
	public static Level level;
	public static World world;
	public static Area area;
	public static Area ui;
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

	private ShaderProgram shader;

	public enum Type {
		REGULAR,
		INTRO,
		ARCADE
	}

	private static int to = -3;
	private Color background = Color.valueOf("#000000"); // #323c39

	public static void reportException(Exception e) {
		Log.report(e);
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

	@Override
	public void create() {
		if (arg.length > 0 && arg[0].startsWith("reset")) {
			Dungeon.newGame();
		}

		Log.init();

		long seed = System.currentTimeMillis();

		Camera camera = new Camera();

		Log.info("Setting random seed to " + seed + "...");
		Random.random.setSeed(seed);

		Log.info("Loading locale...");
		Locale.load("en");

		if (!Network.SERVER) {
			this.setupCursor();
			Assets.init();
		}

		Box2D.init();

		this.initColors();
		this.initInput();

		ui = new Area();
		area = new Area();
		new Ui();

		game = new Game();

		if (!Network.SERVER) {
			if (!Network.NONE) {
				game.setState(new LoginState());
			} else {
				// For debug, @Nufflee
				// game.setState(new MainMenuState());

				Dungeon.goToLevel(0);
			}
		} else {
			game.setState(new HubState());
		}

		area.add(camera);

		// Todo: better way to do this
		Graphics.getMusic("gobbeon").play();

		/*
		ShaderProgram.pedantic = false;

		shader = new ShaderProgram(
			"attribute vec4 "+ShaderProgram.POSITION_ATTRIBUTE+";\n" +
				"attribute vec4 "+ShaderProgram.COLOR_ATTRIBUTE+";\n" +
				"attribute vec2 "+ShaderProgram.TEXCOORD_ATTRIBUTE+"0;\n" +
				"uniform mat4 u_projTrans;\n" +
				"varying vec4 vColor;\n" +
				"varying vec2 vTexCoord;\n" +
				"void main() {\n" +
				"	vColor = "+ShaderProgram.COLOR_ATTRIBUTE+";\n" +
				"	vTexCoord = "+ShaderProgram.TEXCOORD_ATTRIBUTE+"0;\n" +
				"	gl_Position =  u_projTrans * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" +
				"}",
			"#ifdef GL_ES\n"
				+ "#define LOWP lowp\n"
				+ "precision mediump float;\n"
				+ "#else\n"
				+ "#define LOWP \n"
				+ "#endif\n" + //
				"uniform sampler2D u_texture;\n" +
				"uniform vec2 resolution;\n" +
				"varying LOWP vec4 vColor;\n" +
				"varying vec2 vTexCoord;\n" +
				"const float RADIUS = 0.75;\n" +
				"const float SOFTNESS = 0.65;\n" +
				"void main() {\n" +
				"	vec4 texColor = texture2D(u_texture, vTexCoord);\n" +
				"	vec2 position = (gl_FragCoord.xy / resolution.xy) - vec2(0.5);\n" +
				"	float len = length(position);\n" +
				"	float vignette = smoothstep(RADIUS, RADIUS-SOFTNESS, len);\n" +
				"	texColor.rgb = mix(texColor.rgb, texColor.rgb * vignette, 0.5);\n" +
				"	gl_FragColor = texColor * vColor;\n" +
				"}");

		Graphics.batch.setShader(shader);*/

		postProcessor = new PostProcessor(false, true, Gdx.app.getType() == Application.ApplicationType.Desktop);
		int vpW = Gdx.graphics.getWidth();
		int vpH = Gdx.graphics.getHeight();
		int effects = CrtScreen.Effect.TweakContrast.v |  CrtScreen.Effect.PhosphorVibrance.v | CrtScreen.Effect.Scanlines.v | CrtScreen.Effect.Tint.v;

		CrtMonitor crt = new CrtMonitor( vpW, vpH, false, false, CrtScreen.RgbMode.ChromaticAberrations, effects );
		Combine combine = crt.getCombinePass();
		combine.setSource1Intensity( 0f );
		combine.setSource2Intensity( 1f );
		combine.setSource1Saturation( 0f );
		combine.setSource2Saturation( 1f );

		postProcessor.addEffect(crt);
	}

	private PostProcessor postProcessor;

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
	public void render() {
		if (to > -2) {
			if (Network.SERVER || Network.NONE) {
				Dungeon.depth = to;

				game.setState(new LoadState());

				if (Network.SERVER) {
					Network.server.getServerHandler().sendToAll(Packets.makeChatMessage("[green]We are starting the game..."));
				}
			}

			to = -2;
			return;
		}

		float dt = Gdx.graphics.getDeltaTime() * speed;
		time += dt;
		longTime += 1;

		if (Input.instance != null && !Network.SERVER) {
			Input.instance.updateMousePosition();
		}

		if (Network.server != null) {
			Network.server.update(dt);
		} else if (Network.client != null) {
			Network.client.update(dt);
		}

		Tween.update(dt);

		if (!(game.getState() instanceof LoadState)) {
			area.update(dt);
		}

		game.update(dt);

		if (!Network.SERVER) {
			Gdx.gl.glClearColor(this.background.r, this.background.g, this.background.b, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));

			boolean draw = (darkR < MAX_R);

			Graphics.surface.begin();

			if (draw) {
				Graphics.shape.setProjectionMatrix(Camera.ui.combined);
				Gdx.gl.glDepthFunc(GL20.GL_LESS);
				Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
				Gdx.gl.glDepthMask(true);

				Gdx.gl.glColorMask(false, false, false, false);

				Graphics.shape.begin(ShapeRenderer.ShapeType.Filled);
				Graphics.shape.circle(darkX, darkY, darkR);
				Graphics.shape.end();
			}

			if (Camera.instance != null) {
				Camera.instance.applyShake();
				Graphics.batch.setProjectionMatrix(Camera.instance.getCamera().combined);
				Graphics.shape.setProjectionMatrix(Camera.instance.getCamera().combined);
			}

			Graphics.batch.begin();

			if (draw) {
				Gdx.gl.glColorMask(true, true, true, true);
				Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
				Gdx.gl.glDepthFunc(GL20.GL_EQUAL);
			}

			if (game.getState() instanceof HubState) {
				Graphics.medium.draw(Graphics.batch, "[gray]Hub", 0, 12);
				Graphics.medium.draw(Graphics.batch, "[green]Start", 0, 60);
			}

			if (!(game.getState() instanceof ComicsState)) {
				area.render();
			}

			game.render();

			if (Camera.instance != null) {
				Camera.instance.removeShake();
			}

			Graphics.batch.end();

			if (draw) {
				Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
			}

			Graphics.surface.end();
			Texture texture = Graphics.surface.getColorBufferTexture();

			float zoom = Camera.instance.getCamera().zoom;

			postProcessor.capture();
			Graphics.batch.begin();
			Graphics.batch.setProjectionMatrix(Camera.instance.getCamera().combined);
			Graphics.batch.setColor(1, 1, 1, 1f);

			Graphics.batch.draw(texture,
				Camera.instance.getCamera().position.x - Display.GAME_WIDTH / 2 * zoom,
				Camera.instance.getCamera().position.y - Display.GAME_HEIGHT / 2 * zoom, Display.GAME_WIDTH * zoom, Display.GAME_HEIGHT * zoom,
				0, 0, texture.getWidth(), texture.getHeight(), false, true);
			Graphics.batch.end();
			postProcessor.render();

			Graphics.surface.begin();
			Graphics.batch.begin();
			Gdx.gl.glClearColor(0, 0, 0, 0);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));
			Graphics.batch.end();
			Graphics.surface.end();

			if (Input.instance != null) {
				for (Input input : Input.inputs.values()) {
					input.update();
				}
			}
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

		/*shader.begin();
		shader.setUniformf("resolution", width, height);
		shader.end();*/
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

		if (world != null) {
			world.dispose();
		}

		Assets.destroy();

		LoadState.writeDepth();
		Log.close();

		postProcessor.dispose();
		// shader.dispose();
	}

	private void initInput() {
		if (!Network.SERVER) {
			new Input(0);
		}
	}

	private void setupCursor() {
		Cursor customCursor = Gdx.graphics.newCursor(new Pixmap(1, 1, Pixmap.Format.RGBA8888), 0, 0);
		Gdx.graphics.setCursor(customCursor);
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

	public static Color BLACK = Color.valueOf("#000000");
	public static Color GRAY = Color.valueOf("#696a6a");
	public static Color WHITE = Color.valueOf("#ffffff");
	public static Color ORANGE = Color.valueOf("#df7126");
	public static Color RED = Color.valueOf("#ac3232");
	public static Color GREEN = Color.valueOf("#6abe30");
	public static Color BLUE = Color.valueOf("#306082");
	public static Color YELLOW = Color.valueOf("#fbf236");
	public static Color BROWN = Color.valueOf("#8f563b");
}
