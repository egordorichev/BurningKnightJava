package org.rexellentgames.dungeon;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import org.rexellentgames.dungeon.assets.Assets;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.game.Game;
import org.rexellentgames.dungeon.game.InGameState;

public class Dungeon extends ApplicationAdapter {
	private Game game;

	@Override
	public void create() {
		Assets.init();

		this.game = new Game();
		this.game.setState(new InGameState());
	}

	@Override
	public void render() {
		this.game.update(Gdx.graphics.getDeltaTime());

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

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
