package org.rexellentgames.dungeon;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.physics.box2d.Box2D;
import org.rexellentgames.dungeon.assets.Assets;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.game.Game;
import org.rexellentgames.dungeon.game.GeneratorState;
import org.rexellentgames.dungeon.game.InGameState;
import org.rexellentgames.dungeon.game.input.Input;
import org.rexellentgames.dungeon.util.Log;

public class Dungeon extends ApplicationAdapter {
	private Game game;
	public static int level = 0;

	private Color background = Color.valueOf("#323c39");

	@Override
	public void create() {
		Gdx.graphics.setTitle("Dungeon " + Version.asString());

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

		this.game = new Game();
		this.game.setState(new InGameState());
	}

	@Override
	public void render() {
		this.game.update(Gdx.graphics.getDeltaTime());
		Input.instance.update();

		Gdx.gl.glClearColor(this.background.r, this.background.g, this.background.b, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		Graphics.batch.setProjectionMatrix(Camera.instance.getCamera().combined);
		Graphics.batch.begin();

		this.game.render();

		Graphics.batch.end();
	}

	@Override
	public void resize(int width, int height) {
		Camera.instance.resize(width, height);
	}

	@Override
	public void dispose() {
		this.game.destroy();
		Assets.destroy();
	}

	public static void reportException(Exception e) {
		e.printStackTrace();
	}
}
