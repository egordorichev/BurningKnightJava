package org.rexcellentgames.burningknight.entity.creature.fx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.level.SaveableEntity;
import org.rexcellentgames.burningknight.util.Random;

public class Firefly extends SaveableEntity {
	{
		depth = 14;
		w = 12;
		h = 12;
	}

	private float t;

	@Override
	public void init() {
		super.init();
		this.t = Random.newFloat(1024);
	}

	@Override
	public void update(float dt) {
		super.update(dt);
		this.t += dt * 2;
	}

	private float rd = 6;

	@Override
	public void render() {
		int r = 6;
		boolean on = this.t % 20 <= 16f;

		rd += ((on ? 6 : 0) - rd) * Gdx.graphics.getDeltaTime() * 3;

		float x = (float) (this.x + Math.cos(this.t / 8) * Math.sin(this.t / 9) * 32);
		float y = (float) (this.y + Math.sin(this.t / 7) * Math.cos(this.t / 10) * 32);

		Graphics.startAlphaShape();

		if (rd > 2) {
			Graphics.shape.setColor(color.r, color.g, color.b, 0.3f);
			Graphics.shape.circle(x + r, y + r, rd);
		}

		Graphics.shape.setColor(on ? color.r : off.r, on ? color.g : off.g, on ? color.b : off.b, 1);
		Graphics.shape.circle(x + r, y + r, 2);
		Graphics.endAlphaShape();
	}

	private static Color color = Color.valueOf("#99e65f");
	private static Color off = Color.valueOf("#134c4c");
}