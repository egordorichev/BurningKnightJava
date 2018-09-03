package org.rexcellentgames.burningknight.entity.creature.mob.hall;

import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.level.entities.Door;
import org.rexcellentgames.burningknight.entity.level.entities.SolidProp;
import org.rexcellentgames.burningknight.entity.trap.RollingSpike;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class DashingKnight extends Knight {
	public static Animation animations = Animation.make("actor-knight", "-orange");

	public Animation getAnimation() {
		return animations;
	}

	{
		hpMax = 15;
	}

	@Override
	protected State getAi(String state) {
		switch (state) {
			case "stun": return new StunState();
			case "chase": return new DashChaseState();
		}

		return super.getAi(state);
	}

	public class DashingKnightState extends Mob.State<DashingKnight> {

	}

	public class StunState extends DashingKnightState {
		@Override
		public void update(float dt) {
			super.update(dt);

			if (this.t >= 2f) {
				this.checkForPlayer();

				if (self.target == null) {
					self.become("idle");
				} else {
					self.become("chase");
				}
			}
		}
	}

	public class DashChaseState extends DashingKnightState {
		private Point dir = new Point();

		@Override
		public void onEnter() {
			super.onEnter();

			double a = self.getAngleTo(self.target.x + self.target.w / 2, self.target.y + self.target.h / 2);
			float f = 60f;

			dir.x = (float) (Math.cos(a) * f);
			dir.y = (float) (Math.sin(a) * f);
		}

		@Override
		public void update(float dt) {
			super.update(dt);

			self.acceleration.x = dir.x;
			self.acceleration.y = dir.y;
		}
	}

	@Override
	public void onCollision(Entity entity) {
		if (entity instanceof Player || entity == null || entity instanceof Door || entity instanceof SolidProp || entity instanceof RollingSpike) {
			this.become("stun");
		}

		super.onCollision(entity);
	}
}