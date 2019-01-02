package org.rexcellentgames.burningknight.entity.creature.mob.forest;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.entities.SolidProp;
import org.rexcellentgames.burningknight.entity.trap.RollingSpike;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;
import org.rexcellentgames.burningknight.util.Random;

public class Hedgehog extends Mob {
	public static Animation animations = Animation.make("actor-hedgehog", "-gray");
	private AnimationData idle;
	private AnimationData roll;
	private AnimationData circle;
	private AnimationData uncircle;
	private AnimationData hurt;
	private AnimationData killed;
	private AnimationData animation;

	public Animation getAnimation() {
		return animations;
	}

	{
		hpMax = 16;

		idle = getAnimation().get("idle");
		hurt = getAnimation().get("hurt");
		roll = getAnimation().get("roll");
		circle = getAnimation().get("circle");
		killed = getAnimation().get("dead");
		uncircle = getAnimation().get("uncircle");
		animation = idle;
	}

	@Override
	public void init() {
		super.init();

		this.body = this.createSimpleBody(2, 1, 10, 9, BodyDef.BodyType.DynamicBody, false);
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
		} else if (state.equals("roll")) {
			this.animation = roll;
		} else if (state.equals("circle")) {
			this.animation = circle;
		} else if (state.equals("uncircle")) {
			this.animation = uncircle;
		} else {
			this.animation = idle;
		}

		this.renderWithOutline(this.animation);
		Graphics.batch.setColor(1, 1, 1, 1);
		super.renderStats();
	}

	@Override
	public boolean shouldCollide(Object entity, Contact contact, Fixture fixture) {
		if (entity instanceof SolidProp || entity instanceof RollingSpike) {
			return false;
		}

		return super.shouldCollide(entity, contact, fixture);
	}

	@Override
	public void onCollision(Entity entity) {
		if (entity instanceof Level || entity == null) {
			if (state.equals("roll") && body != null) {
				body.setLinearVelocity(new Vector2());
				become("uncircle");
			}
		}

		super.onCollision(entity);
	}

	@Override
	public void renderShadow() {
		Graphics.shadowSized(this.x, this.y + 3, this.w, this.h, 0);
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (!freezed && (state.equals("idle") || state.equals("tired"))) {
			animation.update(dt);
		}

		if (target != null && target.room != room) {
			become("idle");
		}

		super.common();
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
			case "circle": return new CircleState();
			case "uncircle": return new UncircleState();
			case "roll": return new RollState();
			case "tired": return new TiredState();
			case "idle": case "roam": case "alerted": return new IdleState();
		}

		return super.getAi(state);
	}

	public class HedgehogState extends Mob.State<Hedgehog> {

	}

	public class RollState extends HedgehogState {
		@Override
		public void onEnter() {
			super.onEnter();

			self.roll.setFrame(0);

			float a = (float) (self.getAngleTo(self.target.x + 8, self.target.y + 8));

			c = (float) Math.cos(a);
			s = (float) Math.sin(a);
		}

		private float c;
		private float s;

		@Override
		public void update(float dt) {
			super.update(dt);

			float sp = 5000f * dt;
			self.velocity.x += c * sp;
			self.velocity.y += s * sp;

			if (t >= 6f) {
				self.become("uncircle");
			} else {
				self.roll.setFrame((int) (t * 4f) % 4);
			}
		}
	}

	public class TiredState extends HedgehogState {
		@Override
		public void onEnter() {
			super.onEnter();


			float a = Random.newFloat((float) (Math.PI * 2));

			c = (float) Math.cos(a);
			s = (float) Math.sin(a);
		}

		private float c;
		private float s;

		@Override
		public void update(float dt) {
			super.update(dt);


			self.velocity.x -= self.velocity.x * dt * 4f;
			self.velocity.y -= self.velocity.y * dt * 4f;

			float sp = 400f * dt;
			self.velocity.x += c * sp;
			self.velocity.y += s * sp;

			if (t >= 3f) {
				self.become("circle");
			}
		}
	}

	public class CircleState extends HedgehogState {
		@Override
		public void onEnter() {
			super.onEnter();

			self.circle.setFrame(0);
		}

		@Override
		public void update(float dt) {
			super.update(dt);
			float t = this.t * 0.5f;

			if (t >= 0.9f) {
				self.become("roll");
			} else {
				self.circle.setFrame((int) (t * 9f));
			}
		}
	}

	public class UncircleState extends HedgehogState {
		@Override
		public void onEnter() {
			super.onEnter();

			self.uncircle.setFrame(0);
		}

		@Override
		public void update(float dt) {
			super.update(dt);
			float t = this.t * 0.5f;

			if (t >= 0.9f) {
				self.become("tired");
			} else {
				self.uncircle.setFrame((int) (t * 9f));
			}
		}
	}

	public class IdleState extends HedgehogState {
		@Override
		public void update(float dt) {
			super.update(dt);
			checkForPlayer();

			if (self.target != null && self.target.room == self.room) {
				self.become("circle");
			}
		}
	}
}