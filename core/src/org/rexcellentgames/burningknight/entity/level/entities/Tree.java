package org.rexcellentgames.burningknight.entity.level.entities;

import com.badlogic.gdx.math.Rectangle;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.util.Tween;

public class Tree extends SolidProp {
	{
		sprite = "props-tree";
		collider = new Rectangle(4, 8, 30 - 4 * 2, 30 - 8 * 2);
		w = 30;
		h = 30;
	}

	private float am = 1f;

	@Override
	public void onCollision(Entity entity) {
		super.onCollision(entity);

		if (entity instanceof Creature) {
			Tween.to(new Tween.Task(4, 0.2f) {
				@Override
				public float getValue() {
					return am;
				}

				@Override
				public void setValue(float value) {
					am = value;
				}
			});
		}
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		am = Math.max(1, am - dt * 2f);
	}

	@Override
	public void init() {
		super.init();
	}

	@Override
	public void renderShadow() {
		Graphics.shadow(x, y + 8, w, h, 14);
	}

	@Override
	public void render() {
		float a = (float) (Math.cos(this.t + this.y * 0.1f + this.x * 0.2f) * am * 6);
		Graphics.render(region, this.x + 15, this.y + 2, a, 15, 2, false, false);
	}
}