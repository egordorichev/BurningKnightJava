package org.rexcellentgames.burningknight.entity.item.weapon.gun;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.fx.FireFx;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.util.Random;

public class FlameThrower extends Gun {
	{
		sprite = "item-gun_a";
	}

	@Override
	public void use() {
		this.down = true;
	}

	private float last;

	@Override
	public void update(float dt) {
		super.update(dt);

		if (this.down) {
			last += dt;

			if (last >= 0.05f) {
				last = 0;

				float x = this.owner.x + this.owner.w / 2 + (flipped ? -7 : 7);
				float y = this.owner.y + this.owner.h / 4 + this.owner.z;

				x += this.getAimX(0, 0);
				y += this.getAimY(0, 0);

				FireFx fx = new FireFx();

				fx.x = x + Random.newFloat(-4, 4);
				fx.y = y + Random.newFloat(-4, 4);

				float f = 120f;

				fx.vel.x = (float) (Math.cos(this.lastAngle) * f);
				fx.vel.y = (float) (Math.sin(this.lastAngle) * f);

				Dungeon.area.add(fx);
			}

			if (Input.instance.wasReleased("use")) {
				this.down = false;
			}
		}
	}

	private boolean down;
}