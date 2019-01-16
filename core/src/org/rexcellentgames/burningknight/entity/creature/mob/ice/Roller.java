package org.rexcellentgames.burningknight.entity.creature.mob.ice;

import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;
import org.rexcellentgames.burningknight.util.Random;

public class Roller extends Mob {
	public static Animation animations = Animation.make("actor-rolling-snowball", "-white");
	private AnimationData idle;
	private AnimationData killed;
	private AnimationData hurt;
	private AnimationData roll;
	private AnimationData animation;

	public Animation getAnimation() {
		return animations;
	}

	{
		hpMax = 16;

		idle = getAnimation().get("idle");
		hurt = getAnimation().get("hurt");
		killed = getAnimation().get("dead");
		roll = getAnimation().get("roll");
		animation = idle;

		w = 20;
		h = 20;
	}

	@Override
	public void init() {
		super.init();

		flying = true;
		this.body = World.createCircleBody(this, 0, 0, 10, BodyDef.BodyType.DynamicBody, false, 0.9f);
		World.checkLocked(this.body).setTransform(this.x, this.y, 0);
	}

	@Override
	public void destroy() {
		super.destroy();
		this.body = World.removeBody(this.body);
	}

	@Override
	public void render() {
		if (Math.abs(this.velocity.x) > 1f) {
			this.flipped = this.velocity.x < 0;
		}

		if (this.dead) {
			this.animation = killed;
		} else if (this.invt > 0) {
			this.animation = hurt;
		} else if (this.state.equals("roll")) {
			this.animation = roll;
		} else {
			this.animation = idle;
		}

		this.renderWithOutline(this.animation);
		Graphics.batch.setColor(1, 1, 1, 1);
		super.renderStats();
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (this.body != null) {
			this.velocity.x = this.body.getLinearVelocity().x;
			this.velocity.y = this.body.getLinearVelocity().y;

			float m = dt * (state.equals("roll") ? 0.3f : 8f);

			velocity.x -= velocity.x * m;
			velocity.y -= velocity.y * m;

			float a = (float) Math.atan2(this.velocity.y, this.velocity.x);
			this.body.setLinearVelocity(((float) Math.cos(a)) * 32 * Mob.speedMod + knockback.x * 0.2f, ((float) Math.sin(a)) * 32 * Mob.speedMod + knockback.y * 0.2f);
		}

		if (state.equals("roll")) {
			animation.update(dt * (velocity.len() / 80f + 0.2f));
		} else {
			animation.update(dt);
		}

		common();
	}

	@Override
	protected void deathEffects() {
		super.deathEffects();

		this.playSfx("death_clown");
		deathEffect(killed);
	}

	@Override
	protected void onHurt(int a, Entity creature) {
		super.onHurt(a, creature);
		this.playSfx("damage_clown");
	}

	@Override
	protected State getAi(String state) {
		switch (state) {
			case "wait": return new WaitState();
			case "roll": return new RollState();
			case "idle": case "roam": case "alerted": return new IdleState();
		}

		return super.getAi(state);
	}

	public class RollerState extends Mob.State<Roller> {

	}

	public class IdleState extends RollerState {
		@Override
		public void update(float dt) {
			checkForPlayer();

			if (self.target != null && self.target.room == self.room) {
				self.become(Random.chance(50) ? "roll" : "wait");
			}
		}
	}

	public class WaitState extends RollerState {
		private float delay;

		@Override
		public void onEnter() {
			super.onEnter();

			delay = Random.newFloat(1f, 6f);
		}

		@Override
		public void update(float dt) {
			super.update(dt);

			if (t >= delay) {
				self.become("roll");
			}
		}
	}

	public class RollState extends RollerState {
		@Override
		public void onEnter() {
			super.onEnter();

			float angle = Random.newAngle();
			float f = Random.newFloat(200, 300);

			self.velocity.x = (float) (Math.cos(angle) * f);
			self.velocity.y = (float) (Math.sin(angle) * f);
			self.body.setLinearVelocity(self.velocity);
		}

		@Override
		public void update(float dt) {
			super.update(dt);

			if (self.velocity.len() < 30f) {
				self.become("wait");
			}
		}
	}
}