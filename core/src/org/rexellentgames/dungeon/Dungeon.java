package org.rexellentgames.dungeon;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.physics.box2d.Box2D;
import org.rexellentgames.dungeon.assets.Assets;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.game.Game;
import org.rexellentgames.dungeon.game.GeneratorState;
import org.rexellentgames.dungeon.game.InGameState;
import org.rexellentgames.dungeon.game.input.Input;
import org.rexellentgames.dungeon.util.PathFinder;
import org.rexellentgames.dungeon.util.Tween;

public class Dungeon extends ApplicationAdapter {
	public static Game game;
	public static int level = 0;
	public static float time;

	private Color background = Color.valueOf("#323c39");

	@Override
	public void create() {
		Gdx.graphics.setTitle("Burning Knight " + Version.asString());
		Cursor customCursor = Gdx.graphics.newCursor(new Pixmap(1, 1, Pixmap.Format.RGBA8888), 0, 0);
		Gdx.graphics.setCursor(customCursor);

		PathFinder.setMapSize(Level.WIDTH, Level.HEIGHT);

		Assets.init();
		Box2D.init();

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
		Input.instance.bind("mouse0", "X");
		Input.instance.bind("mouse1", "Mouse1");
		Input.instance.bind("mouse1", "Z");
		Input.instance.bind("scroll", "MouseWheel");

		Input.instance.bind("1", "1");
		Input.instance.bind("2", "2");
		Input.instance.bind("3", "3");
		Input.instance.bind("4", "4");
		Input.instance.bind("5", "5");
		Input.instance.bind("6", "6");

		game = new Game();
		game.setState(new InGameState());
	}

	@Override
	public void render() {
		float dt = Gdx.graphics.getDeltaTime();

		time += dt;

		Tween.update(dt);
		game.update(dt);

		// Gdx.gl.glClearColor(this.background.r, this.background.g, this.background.b, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		Graphics.batch.setProjectionMatrix(Camera.instance.getCamera().combined);
		Graphics.batch.begin();

		game.render();

		Graphics.batch.end();
		Input.instance.update();
	}

	@Override
	public void resize(int width, int height) {
		Camera.instance.resize(width, height);
	}

	@Override
	public void dispose() {
		game.destroy();
		Assets.destroy();
	}

	public static void reportException(Exception e) {
		e.printStackTrace();
	}
}
