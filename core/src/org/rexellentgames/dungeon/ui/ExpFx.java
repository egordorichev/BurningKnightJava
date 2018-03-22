package org.rexellentgames.dungeon.ui;

import com.badlogic.gdx.math.Vector2;
import org.rexellentgames.dungeon.Display;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.creature.player.Player;

public class ExpFx extends UiEntity {
	private static Vector2 target = new Vector2(9, Display.GAME_HEIGHT - 22);

	public ExpFx(float x, float y) {
		this.x = this.fromWorldX(x);
		this.y = this.fromWorldY(y);
	}

	@Override
	public void update(float dt) {
		this.lerp(target, 0.05f);

		if (Math.abs(this.x - target.x) + Math.abs(this.y - target.y) < 3) {
			this.done = true;
			Player.instance.addExperience(1);
		}
	}

	@Override
	public void render() {
		// todo
		// Graphics.batch.draw(Graphics.effects, this.x, this.y, 3, 3, 112, 0, 3, 3, false, false);
	}
}