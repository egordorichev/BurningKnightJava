package org.rexcellentgames.burningknight.entity.creature.mob.forest;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.BulletAtom;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.BulletProjectile;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.NanoBullet;
import org.rexcellentgames.burningknight.entity.level.entities.SolidProp;
import org.rexcellentgames.burningknight.entity.trap.RollingSpike;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class Wombat extends Mob {
	public static Animation animations = Animation.make("actor-wombat", "-brown");
	private AnimationData idle;
	private AnimationData sucking;
	private AnimationData blowing;
	private AnimationData killed;
	private AnimationData hurt;
	private AnimationData animation;

	public Animation getAnimation() {
		return animations;
	}

	{
		hpMax = 8;

		idle = getAnimation().get("idle");
		hurt = getAnimation().get("hurt");
		sucking = getAnimation().get("sucking");
		blowing = getAnimation().get("blowing");
		killed = getAnimation().get("dead");
		animation = idle;
	}

	@Override
	public void init() {
		super.init();

		hx = 4;
		hy = 3;
		hw = 12;
		hh = 9;
		flying = true;

		this.body = World.createSimpleBody(this, hx, hy, hw, hh, BodyDef.BodyType.DynamicBody, false, 1);
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
			this.flipped = state.equals("blowing") ? this.velocity.x > 0 : this.velocity.x < 0;
		}

		if (this.dead) {
			this.animation = killed;
		} else if (this.invt > 0) {
			this.animation = hurt;
		} else if (state.equals("blowing")) {
			this.animation = blowing;
		} else if (state.equals("sucking")) {
			this.animation = sucking;
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
	public void update(float dt) {
		super.update(dt);

		if (!freezed && (state.equals("idle") || state.equals("tired"))) {
			animation.update(dt);
		}

		if (body != null) {
			this.velocity.x = this.body.getLinearVelocity().x;
			this.velocity.y = this.body.getLinearVelocity().y;

			float a = (float) Math.atan2(this.velocity.y, this.velocity.x);
			this.body.setLinearVelocity(((float) Math.cos(a)) * 32 * Mob.speedMod + knockback.x * 0.2f, ((float) Math.sin(a)) * 32 * Mob.speedMod + knockback.y * 0.2f);
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
			case "blowing": return new BlowingState();
			case "sucking": return new SuckingState();
			case "tired": return new TiredState();
			case "idle": case "roam": case "alerted": return new IdleState();
		}

		return super.getAi(state);
	}

	public class WombatState extends Mob.State<Wombat> {

	}

	public class BlowingState extends WombatState {
		@Override
		public void onEnter() {
			super.onEnter();

			self.blowing.setFrame(0);

			float a = (float) (self.getAngleTo(self.target.x + 8, self.target.y + 8) - Math.PI);
			float speed = 100f;

			self.body.setLinearVelocity(new Point((float) Math.cos(a) * speed, (float) Math.sin(a) * speed));
		}

		@Override
		public void update(float dt) {
			super.update(dt);

			Vector2 vel = self.body.getLinearVelocity();

			if (vel.len2() < 300f) {
				vel.x += vel.x * dt;
				vel.y += vel.y * dt;
				self.body.setLinearVelocity(vel);
			}

			if (Random.chance(30)) {
				boolean atom = Random.chance(40);
				BulletProjectile ball = atom ? new BulletAtom() : new NanoBullet();

				float a = (float) (Math.atan2(vel.y, vel.x) - Math.PI);
				a += Random.newFloat(-1, 1);
				ball.velocity = new Point((float) Math.cos(a) / 2f, (float) Math.sin(a) / 2f).mul((atom ? 60f : 40f) * Mob.shotSpeedMod * Random.newFloat(0.9f, 1.1f));

				ball.x = (self.x + (self.flipped ? 0 : self.w));
				ball.y = (self.y + 4);
				ball.damage = 2;
				ball.renderCircle = false;
				ball.bad = true;

				Dungeon.area.add(ball);
			}

			if (t >= 6f) {
				self.become("tired");
			} else {
				self.blowing.setFrame((int) (t * 0.5f));
			}
		}
	}

	public class TiredState extends WombatState {
		@Override
		public void update(float dt) {
			super.update(dt);

			Vector2 vel = self.body.getLinearVelocity();
			vel.x -= vel.x * dt * 4f;
			vel.y -= vel.y * dt * 4f;
			self.body.setLinearVelocity(vel);

			if (t >= 3f) {
				self.become("sucking");
			}
		}
	}

	public class SuckingState extends WombatState {
		@Override
		public void onEnter() {
			super.onEnter();

			self.sucking.setFrame(0);
		}

		@Override
		public void update(float dt) {
			super.update(dt);

			if (t >= 1f) {
				self.become("blowing");
			} else {
				self.sucking.setFrame((int) (t * 3f));
			}
		}
	}

	public class IdleState extends WombatState {
		@Override
		public void update(float dt) {
			super.update(dt);
			checkForPlayer();

			if (self.target != null && self.target.room == self.room) {
				self.become("sucking");
			}
		}
	}
}