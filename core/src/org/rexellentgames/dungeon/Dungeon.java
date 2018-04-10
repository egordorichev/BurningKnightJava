package org.rexellentgames.dungeon;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.World;
import org.rexellentgames.dungeon.assets.Assets;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.assets.Locale;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.entity.creature.mob.BurningKnight;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.item.consumable.plant.Plant;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.entities.Entrance;
import org.rexellentgames.dungeon.game.Area;
import org.rexellentgames.dungeon.game.Game;
import org.rexellentgames.dungeon.game.Ui;
import org.rexellentgames.dungeon.game.input.Input;
import org.rexellentgames.dungeon.game.state.ComicsState;
import org.rexellentgames.dungeon.game.state.HubState;
import org.rexellentgames.dungeon.game.state.LoadState;
import org.rexellentgames.dungeon.game.state.LoginState;
import org.rexellentgames.dungeon.net.Network;
import org.rexellentgames.dungeon.net.Packets;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.Tween;

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

	public static Mode mode = Mode.NORMAL;
	private static int to = -2;
	private Color background = Color.valueOf("#000000"); // #323c39

	public static void reportException(Exception e) {
		e.printStackTrace();
	}

	public static void newGame() {
		reset = true;

		loadType = Entrance.LoadType.GO_DOWN;

		Player.instance = null;
		BurningKnight.instance = null;

		level = null;
		area.destroy();
		goToLevel(0);
	}

	public static void goToLevel(int level) {
		to = level;
	}

	@Override
	public void create() {
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
				Dungeon.goToLevel(0);
			}
		} else {
			game.setState(new HubState());
		}

		area.add(new Camera());
	}

	@Override
	public void render() {
		if (to != -2) {
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

		float dt = Gdx.graphics.getDeltaTime();
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

			if (Camera.instance != null) {
				Camera.instance.applyShake();
				Graphics.batch.setProjectionMatrix(Camera.instance.getCamera().combined);
			}

			Graphics.batch.begin();

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

	public enum Mode {
		NORMAL
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
