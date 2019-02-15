package org.rexcellentgames.burningknight.entity.creature.mob.common;

import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.Random;

public class MovingFly extends Fly {
	public static Animation animations = Animation.make("actor-fly", "-red");

	public Animation getAnimation() {
		return animations;
	}

	{
		hpMax = 5;
	}

	@Override
	public float getWeight() {
		return 0.3f;
	}

	@Override
	protected State getAi(String state) {
		return new FollowState();
	}

	public class FollowState extends Mob.State<MovingFly> {
		@Override
		public void update(float dt) {
			super.update(dt);

			if (Player.instance.room == self.room) {
				flyTo(Player.instance, 2f, 4f);
			}

			if (self.room != null && Player.instance.room == self.room) {
				for (Mob mob : Mob.all) {
					if (mob != self && mob.room == self.room && mob instanceof Fly) {
						float x = mob.x + mob.w / 2 + mob.velocity.x * dt * 10;
						float y = mob.y + mob.h / 2 + mob.velocity.y * dt * 10;
						float d = self.getDistanceTo(x, y);

						if (d < 16) {
							float a = d <= 1 ? Random.newFloat((float) (Math.PI * 2)) : self.getAngleTo(x, y);
							float f = 500 * dt;

							self.velocity.x -= Math.cos(a) * f;
							self.velocity.y -= Math.sin(a) * f;
						}
					}
				}
			}
		}
	}
}