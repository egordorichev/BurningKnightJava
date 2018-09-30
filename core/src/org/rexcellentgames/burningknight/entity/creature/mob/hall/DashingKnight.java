package org.rexcellentgames.burningknight.entity.creature.mob.hall;

import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.level.entities.Door;
import org.rexcellentgames.burningknight.entity.level.entities.SolidProp;
import org.rexcellentgames.burningknight.entity.trap.RollingSpike;
import org.rexcellentgames.burningknight.game.Ui;
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

			if (this.t >= 5f) {
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
			float f = 1f;

			dir.x = (float) (Math.cos(a) * f);
			dir.y = (float) (Math.sin(a) * f);
		}

		@Override
		public void update(float dt) {
			super.update(dt);

			float f = Math.min(120f, this.t * 60f * 2f);

			self.acceleration.x = dir.x * (f);
			self.acceleration.y = dir.y * (f);
		}
	}

	private float al;

	@Override
	public void renderSigns() {
		if (this.al > 0.05f && !Ui.hideUi) {
			Graphics.startAlphaShape();
			Graphics.shape.setColor(1, 1, 1, this.al);

			for (int i = 0; i < 5; i++) {
				float a = (float) (((float) i) / 6 * (Math.PI * 2) + this.t);

				float x = (float) (Math.cos(a) * 6f);
				float y = (float) (Math.sin(a) * 3f);

				Graphics.shape.circle(this.x + this.w / 2 + x,
					this.y + this.h + 3 + y, (float) (2 - Math.sin(a)));
			}

			Graphics.endAlphaShape();
		}

		super.renderSigns();
	}

	private boolean use;

	@Override
	public void update(float dt) {
		super.update(dt);

		this.al += ((this.state.equals("stun") ? 1 : 0) - this.al) * dt * 5;

		if (this.use) {
			this.sword.use();
			this.use = false;
		}
	}

	@Override
	public boolean rollBlock() {
		if (this.state.equals("stun")) {
			return false;
		}

		return true;
	}

	@Override
	public void onCollision(Entity entity) {
		if (entity instanceof Player || entity == null || entity instanceof Door || entity instanceof SolidProp || entity instanceof RollingSpike) {
			if (entity instanceof Player) {
				this.use = true;
				this.become("stun"); // sorry, dirty trick
				this.become("chase");
			} else {
				this.become("stun");
			}
		}

		super.onCollision(entity);
	}
}