package org.rexellentgames.dungeon;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import org.rexellentgames.dungeon.game.Game;
import org.rexellentgames.dungeon.game.InGameState;

public class Dungeon extends ApplicationAdapter {
	private Game game;

	@Override
	public void create() {
		this.game = new Game();
		this.game.setState(new InGameState());
	}

	@Override
	public void render() {
		this.game.update(Gdx.graphics.getDeltaTime());
		this.game.render();
	}
	
	@Override
	public void dispose() {
		this.game.destroy();
	}
}
