package org.rexcellentgames.burningknight.entity.creature.mob.library;

import box2dLight.PointLight;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.BulletProjectile;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;
import org.rexcellentgames.burningknight.util.Random;

public class Cuok extends Mob {
	public static Animation animations = Animation.make("actor-cuok", "-red");
	public Animation getAnimation() {
		return animations;
	}

	private AnimationData idle;
	private AnimationData killed;
	private AnimationData hurt;
	private AnimationData bet;
	private AnimationData attack;
	private AnimationData animation;

	private PointLight light;

	{
		hpMax = 16;

		idle = getAnimation().get("idle");
		hurt = getAnimation().get("hurt");
		killed = getAnimation().get("dead");
		bet = getAnimation().get("anim");
		attack = getAnimation().get("rage");
		animation = idle;
		w = 14;
	}

	@Override
	public void renderShadow() {
		Graphics.shadow(x + 1, y, w, h, z + 8);
	}

	@Override
	public void init() {
		super.init();

		tt = Random.newFloat(8);


		light = World.newLight(32, new Color(1, 1, 1, 1f), 64, x, y);
		light.setIgnoreAttachedBody(true);

		flying = true;
		this.body = this.createSimpleBody(0, 0, 14, 16, BodyDef.BodyType.DynamicBody, false);
		World.checkLocked(this.body).setTransform(this.x, this.y, 0);
	}

	@Override
	public void destroy() {
		super.destroy();
		World.removeLight(light);
		this.body = World.removeBody(this.body);
	}

	@Override
	public void render() {
		if (this.target != null) {
			this.flipped = this.target.x < this.x;
		} else {
			if (Math.abs(this.velocity.x) > 1f) {
				this.flipped = this.velocity.x < 0;
			}
		}

		if (this.dead) {
			this.animation = killed;
		} else if (this.invt > 0) {
			this.animation = hurt;
		} else if (this.state.equals("attack")) {
			this.animation = attack;
		} else if (this.state.equals("anim")) {
			this.animation = bet;
		} else {
			this.animation = idle;
		}

		this.renderWithOutline(this.animation);
		Graphics.batch.setColor(1, 1, 1, 1);
		super.renderStats();
	}

	private float tt;

	@Override
	public void update(float dt) {
		super.update(dt);

		light.setActive(true);
		light.attachToBody(body, 8, 8, 0);
		light.setPosition(x + 8, y + 8);
		light.setDistance(64);

		tt += dt;
		z = (float) Math.cos(tt * 3) * 2;

		animation.update(dt);
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
			case "anim": return new AnimState();
			case "preattack": return new PreattackState();
			case "attack": return new AttackState();
			case "idle": case "roam": case "alerted": return new IdleState();
		}

		return super.getAi(state);
	}

	public class CuokState extends Mob.State<Cuok> {

	}

	public float calcAngle(float a, int num) {
		return (float) Math.toRadians(Math.round(a / 90f) * 90f);
	}

	public class AttackState extends CuokState {
		private int num;
		private float a;

		@Override
		public void onEnter() {
			super.onEnter();

			a = (float) Math.toDegrees(self.getAngleTo(self.target.x + self.target.w / 2, self.target.y + self.target.h / 2));
			a = self.calcAngle(a, 0);
			num = -1;
		}

		@Override
		public void update(float dt) {
			super.update(dt);

			if (num == -1) {
				if (t >= 1f) {
					num = 0;
					t = 0;
				}

				return;
			}

			if (t >= 0.4f) {
				t = 0;

				if (num == getMax()) {
					self.become("anim");
					return;
				}

				if (recalc()) {
					a = (float) Math.toDegrees(self.getAngleTo(self.target.x + self.target.w / 2, self.target.y + self.target.h / 2));
					a = self.calcAngle(a, num);
				}

				self.spawnBullet(a);

				num++;
			}
		}
	}

	public boolean recalc() {
		return false;
	}

	public void spawnBullet(float a) {
		BulletProjectile bullet = new BulletProjectile();

		boolean atom = Random.chance(50);

		bullet.letter = atom ? "bullet-nano" : (Random.chance(30) ? "bullet-atom" : "bullet-rect");
		bullet.bad = true;
		bullet.owner = this;
		bullet.x = x + w / 2;
		bullet.y = y + 4;
		bullet.renderCircle = false;

		float d = 30f;

		bullet.velocity.x = (float) (Math.cos(a) * d);
		bullet.velocity.y = (float) (Math.sin(a) * d);

		Dungeon.area.add(bullet);
	}

	public int getMax() {
		return 5;
	}

	public boolean toAttack;

	public class AnimState extends CuokState {
		@Override
		public void update(float dt) {
			super.update(dt);

			if (t >= 0.15f) {
				self.become(self.toAttack ? "attack" : "preattack");
				self.toAttack = false;
			}
		}
	}

	public class PreattackState extends CuokState {
		private float delay;

		@Override
		public void onEnter() {
			super.onEnter();
			delay = Random.newFloat(1f, 3f);
		}

		@Override
		public void update(float dt) {
			super.update(dt);

			if (delay < t) { // && self.getDistanceTo(self.target.x + 8, self.target.y + 8) < 128) {
				self.toAttack = true;
				self.become("anim");
			}
		}
	}

	public class IdleState extends CuokState {
		@Override
		public void update(float dt) {
			if (self.target != null && self.target.room == self.room) {
				self.become("preattack");
			}
		}
	}
}