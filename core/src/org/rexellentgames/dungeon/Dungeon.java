package org.rexellentgames.dungeon;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.physics.box2d.Box2D;
import org.rexellentgames.dungeon.assets.Assets;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.game.Game;
import org.rexellentgames.dungeon.game.GeneratorState;
import org.rexellentgames.dungeon.game.InGameState;

public class Dungeon extends ApplicationAdapter {
	private Game game;
	public static int level = 0;

	@Override
	public void create() {
		Gdx.graphics.setTitle("Dungeon " + Version.asString());

		Assets.init();
		Box2D.init();

		this.game = new Game();
		this.game.setState(new GeneratorState());
	}

	@Override
	public void render() {
		this.game.update(Gdx.graphics.getDeltaTime());

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		Graphics.batch.setProjectionMatrix(Camera.instance.getCamera().combined);
		Graphics.batch.begin();
		this.game.render();
		Graphics.batch.end();
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
