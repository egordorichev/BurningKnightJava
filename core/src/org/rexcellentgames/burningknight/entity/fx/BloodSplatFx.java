package org.rexcellentgames.burningknight.entity.fx;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.Random;

import java.util.ArrayList;

public class BloodSplatFx extends Entity {
	private static ArrayList<Animation.Frame> animations = Animation.make("blood").getFrames("idle");
	private float a;
	private float r;
	private float g;
	private float b;
	private TextureRegion region;
	private float al;

	{
		depth = -9;
	}

	@Override
	public void init() {
		super.init();

		this.r = Random.newFloat(0.4f, 1f);
		this.a = Random.newFloat(360);
		this.g = Random.newFloat(0, 0.1f);
		this.b = Random.newFloat(0, 0.1f);
		this.region = animations.get(Random.newInt(animations.size())).frame;
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (this.al < 1f) {
			this.al = Math.min(1f, this.al + dt * 4f);
		}
	}

	@Override
	public void render() {
		Graphics.batch.setColor(this.r, this.g, this.b, 0.8f);
		Graphics.render(region, this.x + 8, this.y + 8, this.a, 8, 8, false, false, this.al, this.al);
		Graphics.batch.setColor(1, 1, 1, 1);
	}
}