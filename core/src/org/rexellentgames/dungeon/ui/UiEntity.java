package org.rexellentgames.dungeon.ui;

import org.rexellentgames.dungeon.Display;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.entity.Entity;

public class UiEntity extends Entity {
	{
		alwaysRender = true;
		alwaysActive = true;
	}

	protected float fromWorldX(float x) {
		return x - Camera.game.position.x + Display.GAME_WIDTH / 2;
	}

	protected float fromWorldY(float y) {
		return y - Camera.game.position.y + Display.GAME_HEIGHT / 2;
	}
}