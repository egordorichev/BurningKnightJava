package org.rexcellentgames.burningknight.game.fx;

import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.util.Random;

public class BackgroundFx extends Entity {
	private float size;
	private float alpha;
	private float val;
	private float speed;

	{
		alwaysActive = true;
	}

	@Override
	public void init() {
		y = -32 - Random.newFloat(32f);
		x = Random.newFloat(0, Display.GAME_WIDTH);

		size = Random.newFloat(8, 24) / 2f;
		val = Random.newFloat(0.7f, 1f);
		speed = size * 3f;
		alpha = Random.newFloat(0.3f, 0.8f);
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		this.y += speed * dt;

		if (this.y >= Display.GAME_HEIGHT + 32) {
			this.init();
		}
	}

	@Override
	public void render() {
		Graphics.startAlphaShape();
		Graphics.shape.setColor(this.val, this.val, this.val, this.alpha / 3);
		Graphics.shape.circle(this.x, this.y, this.size * 1.5f);
		Graphics.shape.setColor(this.val, this.val, this.val, this.alpha);
		Graphics.shape.circle(this.x, this.y, this.size);
		Graphics.endAlphaShape();
	}
}