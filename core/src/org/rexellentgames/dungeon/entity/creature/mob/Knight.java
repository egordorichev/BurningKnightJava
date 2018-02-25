package org.rexellentgames.dungeon.entity.creature.mob;

import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.util.Animation;

public class Knight extends Mob {
	{
		hpMax = 10;
	}

	private static Animation idle = new Animation(Graphics.sprites, 0.08f, 16, 224, 225, 226, 227,
		228, 229, 230, 231);

	private static Animation run = new Animation(Graphics.sprites, 0.08f, 16, 232, 233, 234, 235,
		236, 237, 238, 239);

	@Override
	public void init() {
		super.init();

		this.assignTarget();
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (this.target != null) {

		}

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