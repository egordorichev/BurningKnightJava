package org.rexellentgames.dungeon.ui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import org.rexellentgames.dungeon.Display;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.util.Animation;
import org.rexellentgames.dungeon.util.Random;

public class ExpFx extends UiEntity {
	private static Vector2 target = new Vector2(9, Display.GAME_HEIGHT - 24);
	private static Animation animations = Animation.make("fx_exp");
	private TextureRegion region;

	public ExpFx(float x, float y) {
		this.x = this.fromWorldX(x);
		this.y = this.fromWorldY(y);
		this.region = animations.getFrames("idle").get(Random.newInt(animations.getFrames("idle").size())).frame;
	}

	@Override
	public void update(float dt) {
		this.lerp(target, 0.03f);

		if (Math.abs(this.x - target.x) + Math.abs(this.y - target.y) < 3) {
			this.done = true;
			Player.instance.addExperience(1);
		}
	}

	@Override
	public void render() {
		Graphics.render(region, this.x - 2, this.y - 2);
	}
}