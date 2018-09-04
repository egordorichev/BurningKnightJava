package org.rexcellentgames.burningknight.entity.level.entities.fx;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

import java.util.ArrayList;

public class PoofFx extends Entity {
	private static ArrayList<Animation.Frame> animations = Animation.make("poof-particles", "-particles").getFrames("idle");
	public TextureRegion region;
	public Point vel;
	public float speed = 1f;
	public boolean shadow = true;

	{
		depth = 1;
	}

	@Override
	public void init() {
		this.alwaysActive = true;

		if (this.vel == null) {
			this.vel = new Point(
				Random.newFloat(-1f, 1f),
				Random.newFloat(-1f, 1f)
			);
		}

		this.vel.mul(60);

		a = Random.newFloat(360);
		va = Random.newFloat(-10, 10);
		region = animations.get(Random.newInt(animations.size())).frame;
		max = Random.newFloat(1f, 3f);
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		this.x += this.vel.x * dt;
		this.y += this.vel.y * dt;

		this.vel.mul(0.95f);

		this.t += dt;

		this.a += this.va * dt * 60 ;
		this.va *= 0.995f;

		if (this.t >= max) {
			this.done = true;
		}
	}

	public float t;
	private float a;
	private float va;
	private float max;

	@Override
	public void render() {
		float s = 1f - this.t / max;
		Graphics.render(region, this.x, this.y, this.a, 10, 10, false, false, s, s);
	}
}