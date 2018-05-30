package org.rexellentgames.dungeon.entity.item.weapon.gun.bullet;

import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.util.Animation;
import org.rexellentgames.dungeon.util.AnimationData;
import org.rexellentgames.dungeon.util.Random;
import org.rexellentgames.dungeon.util.geometry.Point;

public class Part extends Entity {
	private static Animation animations = Animation.make("fx-part");
	public AnimationData animation;
	public Point vel;
	public float speed = 1f;
	public boolean shadow = true;

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

		if (this.animation == null) {
			this.animation = animations.get("idle");
			this.animation.setFrame(Random.newInt(0, 2));
		}
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		this.x += this.vel.x * dt;
		this.y += this.vel.y * dt;

		this.vel.mul(0.95f);

		if (this.animation.update(dt / this.speed)) {
			this.done = true;
		}
	}

	@Override
	public void render() {
		if (shadow) {
			Graphics.startShadows();
			this.animation.render(this.x, this.y - 4, false, false);
			Graphics.endShadows();
		} else {
			Graphics.batch.setProjectionMatrix(Camera.ui.combined);
		}

		this.animation.render(this.x, this.y, false, false);
	}
}