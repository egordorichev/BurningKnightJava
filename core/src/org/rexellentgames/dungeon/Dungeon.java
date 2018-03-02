package org.rexellentgames.dungeon;

import box2dLight.RayHandler;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.World;
import org.rexellentgames.dungeon.assets.Assets;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.RegularLevel;
import org.rexellentgames.dungeon.game.Area;
import org.rexellentgames.dungeon.game.Game;
import org.rexellentgames.dungeon.game.LoadState;
import org.rexellentgames.dungeon.game.input.Input;
import org.rexellentgames.dungeon.util.PathFinder;

public class Dungeon extends ApplicationAdapter {
	public static Game game;
	public static int depth = 0;
	public static float time;
	public static RegularLevel level;
	public static RayHandler light;
	public static World world;
	public static Area area;
	public static Area ui;
	public static boolean up;
	public static boolean reset;

	private static int to = -1;
	private Color background = Color.valueOf("#323c39");

	public static void reportException(Exception e) {
		e.printStackTrace();
	}

	@Override
	public void create() {
		this.setupCursor();

		PathFinder.setMapSize(Level.WIDTH, Level.HEIGHT);
		Assets.init();
		Box2D.init();

		this.initColors();
		this.initInput();

		game = new Game();
		goToLevel(0);
	}

	public static void goToLevel(int level) {
		to = level;
	}

	@Override
	public void render() {
		if (to != -1) {
			Dungeon.depth = to;
			game.setState(new LoadState());
			to = -1;
			return;
		}

		float dt = Gdx.graphics.getDeltaTime();
		time += dt;
		game.update(dt);

		Gdx.gl.glClearColor(this.background.r, this.background.g, this.background.b, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Graphics.batch.setProjectionMatrix(Camera.instance.getCamera().combined);
		Graphics.batch.begin();

		game.render();

		Graphics.batch.end();
		Input.instance.update();
	}

	@Override
	public void resize(int width, int height) {
		if (Camera.instance != null) {
			Camera.instance.resize(width, height);
		}
	}

	@Override
	public void dispose() {
		if (area != null) {
			ui.destroy();
			area.destroy();
		}

		game.destroy();
		world.dispose();
		Assets.destroy();
	}

	private void initInput() {
		new Input();

		Input.instance.bind("left", "Left");
		Input.instance.bind("left", "A");

		Input.instance.bind("right", "Right");
		Input.instance.bind("right", "D");

		Input.instance.bind("up", "Up");
		Input.instance.bind("up", "W");

		Input.instance.bind("down", "Down");
		Input.instance.bind("down", "S");

		Input.instance.bind("pickup", "Q");
		Input.instance.bind("toggle_inventory", "E");
		Input.instance.bind("drop_item", "R");

		Input.instance.bind("mouse0", "Mouse0");
		Input.instance.bind("mouse1", "Mouse1");
		Input.instance.bind("scroll", "MouseWheel");

		Input.instance.bind("action", "X");
		Input.instance.bind("1", "1");
		Input.instance.bind("2", "2");
		Input.instance.bind("3", "3");
		Input.instance.bind("4", "4");
		Input.instance.bind("5", "5");
		Input.instance.bind("6", "6");
	}

	private void setupCursor() {
		Cursor customCursor = Gdx.graphics.newCursor(new Pixmap(1, 1, Pixmap.Format.RGBA8888), 0, 0);
		Gdx.graphics.setCursor(customCursor);
	}

	private void initColors() {
		Colors.put("black", Color.valueOf("#000000"));
		Colors.put("orange", Color.valueOf("#df7126"));
		Colors.put("red", Color.valueOf("#ac3232"));
		Colors.put("green", Color.valueOf("#6abe30"));
	}
}
