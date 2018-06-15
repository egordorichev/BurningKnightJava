package org.rexcellentgames.burningknight.entity.creature.mob.hall;

import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.weapon.dagger.DaggerA;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;
import org.rexcellentgames.burningknight.util.Random;

public class Thief extends Mob {
	public static Animation animations = Animation.make("actor-thief", "-purple");
	protected Item sword;
	private AnimationData idle;
	private AnimationData run;
	private AnimationData hurt;
	private AnimationData killed;
	private AnimationData animation;

	public Animation getAnimation() {
		return animations;
	}

	{
		hpMax = 1;
		blockChance = 90;

		idle = getAnimation().get("idle").randomize();
		run = getAnimation().get("run").randomize();
		hurt = getAnimation().get("hurt").randomize();
		killed = getAnimation().get("death").randomize();
		animation = this.idle;
	}

	@Override
	public void init() {
		super.init();

		this.sword = new DaggerA();
		this.sword.setOwner(this);

		this.body = this.createSimpleBody(2, 1, 12, 12, BodyDef.BodyType.DynamicBody, false);
		this.body.setTransform(this.x, this.y, 0);

		speed = 100;
		maxSpeed = 100;
	}

	@Override
	public void render() {
		if (Math.abs(this.vel.x) > 1f) {
			this.flipped = this.vel.x < 0;
		}

		if (this.falling) {
			this.renderFalling(this.animation);
			return;
		}

		float v = Math.abs(this.vel.x) + Math.abs(this.vel.y);

		if (this.dead) {
			this.animation = killed;
		} else if (this.invt > 0) {
			this.animation = hurt;
		} else if (v > 9.9) {
			this.animation = run;
		} else {
			this.animation = idle;
		}

		this.renderWithOutline(this.animation);
		Graphics.batch.setColor(1, 1, 1, this.a);

		this.sword.render(this.x, this.y, this.w, this.h, this.flipped);
		Graphics.batch.setColor(1, 1, 1, 1);
	}

	public class ThiefState extends Mob.State<Thief> {

	}

	@Override
	protected State getAi(String state) {
		switch (state) {
			case "idle": case "roam": return new IdleState();
			case "chase": case "alerted": return new ChaseState();
			case "unchase": return new UnchaseState();
			case "attack": return new AttackState();
			case "preattack": return new PreattackState();
			case "wait": return new WaitState();
		}

		return super.getAi(state);
	}

	public class IdleState extends ThiefState {
		@Override
		public void update(float dt) {
			super.update(dt);
			this.checkForPlayer();
		}
	}

	public class ChaseState extends ThiefState {
		public static final float ATTACK_DISTANCE = 24f;

		@Override
		public void update(float dt) {
			this.checkForPlayer();

			if (self.lastSeen == null) {
				return;
			} else {
				if (this.moveTo(self.lastSeen, 30f, ATTACK_DISTANCE)) {
					if (self.target != null) {
						self.become("preattack");
					} else {
						self.noticeSignT = 0f;
						self.hideSignT = 2f;
						self.become("idle");
					}

					return;
				}
			}

			super.update(dt);
		}
	}

	public class UnchaseState extends ThiefState {
		private float delay;

		@Override
		public void onEnter() {
			super.onEnter();

			delay = Random.newFloat(2f, 5f);
			self.blockChance = 10;
		}

		@Override
		public void onExit() {
			super.onExit();
			self.blockChance = 90;
		}

		@Override
		public void update(float dt) {
			super.update(dt);

			if (this.t >= delay) {
				self.become("wait");
			} else {
				this.moveFrom(self.lastSeen, 40f, 4f);
			}
		}
	}

	public class WaitState extends ThiefState {
		private float delay;

		@Override
		public void onEnter() {
			super.onEnter();
			delay = Random.newFloat(1f, 5f);
		}

		@Override
		public void update(float dt) {
			super.update(dt);

			if (this.t >= delay) {
				self.become("chase");
			}
		}
	}

	public class AttackState extends ThiefState {
		@Override
		public void onEnter() {
			super.onEnter();
			self.sword.use();
			self.become(Random.chance(40) ? "unchase" : "chase");
		}
	}

	public class PreattackState extends ThiefState {
		@Override
		public void update(float dt) {
			super.update(dt);

			if (this.t >= 0.7f) {
				self.become("attack");
			}
		}
	}

	@Override
	public void destroy() {
		super.destroy();
		this.sword.destroy();
	}


	@Override
	public void update(float dt) {
		super.update(dt);

		if (this.freezed) {
			return;
		}

		if (this.dead) {
			super.common();
			return;
		}

		if (this.animation != null) {
			this.animation.update(dt * speedMod);
		}

		this.sword.update(dt * speedMod);

		super.common();
	}

	@Override
	protected void die(boolean force) {
		super.die(force);

		this.playSfx("death_clown");

		this.done = true;
		deathEffect(killed);
	}
}