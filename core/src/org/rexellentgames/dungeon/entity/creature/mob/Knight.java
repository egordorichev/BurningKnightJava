package org.rexellentgames.dungeon.entity.creature.mob;

import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.util.Animation;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.file.FileReader;
import org.rexellentgames.dungeon.util.geometry.Point;

import java.io.IOException;

public class Knight extends Mob {
	{
		hpMax = 10;
	}

	private static Animation idle = new Animation(Graphics.sprites, 0.08f, 16, 224, 225, 226, 227,
		228, 229, 230, 231);

	private static Animation run = new Animation(Graphics.sprites, 0.08f, 16, 232, 233, 234, 235,
		236, 237, 238, 239);

	private Point point;

	@Override
	public void init() {
		super.init();

		this.body = this.createBody(1, 2, 12, 14, BodyDef.BodyType.DynamicBody, false);
		this.body.setTransform(this.x, this.y, 0);
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);
		this.body.setTransform(this.x, this.y, 0);
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (this.target != null) {
			if (this.t % 1 <= 0.017) {
				if (this.point != null) {
					this.x = this.point.x;
					this.y = this.point.y;
					this.body.setTransform(x, y, 0);
					this.point = this.getCloser(this.target);
				} else {
					this.point = this.getCloser(this.target);
				}
			}
		} else {
			this.assignTarget();
		}

		// todo: cap
		/*float v = Math.abs(this.vel.x) + Math.abs(this.vel.y);

		if (v > 9.9) {
			this.become("run");
		} else {
			this.become("idle");

			this.vel.x = 0;
			this.vel.y = 0;
		}*/

		super.common();
	}

	@Override
	public void render() {
		if (this.state.equals("run")) {
			run.render(this.x, this.y, this.t, this.flipped);
		} else {
			idle.render(this.x, this.y, this.t, this.flipped);
		}
	}
}