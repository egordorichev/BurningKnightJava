package org.rexcellentgames.burningknight.entity.creature.mob.hall;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.accessory.hat.KnightHat;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.Sword;
import org.rexcellentgames.burningknight.entity.item.weapon.throwing.ThrowingDagger;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;
import org.rexcellentgames.burningknight.util.Random;

import java.util.ArrayList;

public class Knight extends Mob {
	public static Animation animations = Animation.make("actor-knight-v2", "-blue");
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
		hpMax = 5;
		speed = 5;

		idle = getAnimation().get("idle").randomize();
		run = getAnimation().get("run").randomize();
		hurt = getAnimation().get("hurt").randomize();
		killed = getAnimation().get("death").randomize();
		animation = this.idle;
	}

	@Override
	public void initStats() {
		super.initStats();
		this.setStat("block_chance", 0.3f);
	}

	@Override
	protected void onHurt(int a, Creature creature) {
		super.onHurt(a, creature);

		this.playSfx("damage_towelknight");
	}

	@Override
	public void init() {
		super.init();

		this.sword = new Sword();
		this.sword.setOwner(this);

		this.body = this.createSimpleBody(2, 1,12, 12, BodyDef.BodyType.DynamicBody, false);
		this.body.setTransform(this.x, this.y, 0);
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
	public void render() {
		if (Math.abs(this.vel.x) > 1f) {
			this.flipped = this.vel.x < 0;
		}

		float v = Math.abs(this.acceleration.x) + Math.abs(this.acceleration.y);

		if (this.dead) {
			this.animation = killed;
		} else if (this.invt > 0) {
			this.animation = hurt;
		} else if (v > 1f) {
			this.animation = run;
		} else {
			this.animation = idle;
		}

		this.renderWithOutline(this.animation);
		Graphics.batch.setColor(1, 1, 1, this.a);

		this.sword.render(this.x, this.y, this.w, this.h, this.flipped);
		Graphics.batch.setColor(1, 1, 1, 1);
	}

	@Override
	protected ArrayList<Item> getDrops() {
		ArrayList<Item> items = super.getDrops();

		if (Random.chance(5)) {
			items.add(new KnightHat());
		}

		return items;
	}

	@Override
	public void destroy() {
		super.destroy();

		this.sword.destroy();
	}

	@Override
	protected State getAi(String state) {
		switch (state) {
			case "idle": return new IdleState();
			case "alerted": return new AlertedState();
			case "chase": return new ChaseState();
			case "attack": return new AttackingState();
			case "preattack": return new PreAttackState();
			case "roam": return new RoamState();
			case "dash": return new DashState();
		}

		return super.getAi(state);
	}

	public class KnightState extends State<Knight> {

	}

	@Override
	public void renderShadow() {
		Graphics.shadowSized(this.x, this.y, this.w, this.h, 6);
	}

	public class IdleState extends KnightState {
		public float delay;

		@Override
		public void onEnter() {
			super.onEnter();
			this.delay = Random.newFloat(5f, 10f);
		}

		@Override
		public void update(float dt) {
			if (this.t >= this.delay) {
				self.become("roam");
				return;
			}

			this.checkForPlayer();
			super.update(dt);
		}
	}

	public class RoamState extends KnightState {
		@Override
		public void onEnter() {
			super.onEnter();
			this.findNearbyPoint();
		}

		@Override
		public void update(float dt) {
			if (this.targetPoint != null && this.moveTo(this.targetPoint, 2.5f, 8f)) {
				self.become("idle");
				return;
			}

			this.checkForPlayer();
			super.update(dt);
		}
	}

	public class AlertedState extends KnightState {
		public static final float DELAY = 1f;

		@Override
		public void update(float dt) {
			if (this.t >= DELAY) {
				self.become("chase");
			}

			super.update(dt);
		}
	}

	@Override
	protected void die(boolean force) {
		super.die(force);
		this.playSfx("death_towelknight");

		this.done = true;
		deathEffect(killed);
	}

	public float minAttack = 130f;

	public class ChaseState extends KnightState {
		public static final float ATTACK_DISTANCE = 20f;
		public static final float DASH_DIST = 48f;
		public float delay;
		private float att;

		@Override
		public void onEnter() {
			super.onEnter();
			this.delay = Random.newFloat(8f, 10f);

			if (self.sword instanceof Sword) {
				this.att = ATTACK_DISTANCE;
			} else if (self.sword instanceof ThrowingDagger) {
				// dagger knights
				this.att = 80f;
			} else {
				// ranged knights
				this.att = 180f;
			}
		}

		@Override
		public void update(float dt) {
			this.checkForPlayer();

			if (self.lastSeen == null) {
				self.become("idle");
				return;
			} else {
				if (this.moveTo(self.lastSeen, 5f, this.att)) {
					if (self.target != null && self.getDistanceTo((int) (self.target.x + self.target.w / 2),
						(int) (self.target.y + self.target.h / 2)) <= this.att) {

						self.become("preattack");
					} else {
						self.noticeSignT = 0f;
						self.hideSignT = 2f;
						self.become("idle");
					}
				} else {
					if (self.target != null && Random.chance(1)) {
						float d = self.getDistanceTo((int) (self.target.x + self.target.w / 2),
							(int) (self.target.y + self.target.h / 2));

						if (d >= DASH_DIST) {
							self.become("dash");
							return;
						}
					}
				}
			}

			super.update(dt);
		}
	}

	public class AttackingState extends KnightState {
		@Override
		public void onEnter() {
			super.onEnter();
			self.sword.use();

			float r = Random.newFloat();

			if (r < 0.1f) {
				self.become("dash");
			} else if (r < 0.55f) {
				self.become("preattack");
			}
		}

		@Override
		public void update(float dt) {
			super.update(dt);

			if (self.target != null) {
				self.flipped = self.target.x + self.target.w / 2 < self.x + self.w / 2;
			}

			if (self.sword.getDelay() == 0) {
				self.become("chase");
				this.checkForPlayer();
			}
		}
	}

	public class PreAttackState extends KnightState {
		@Override
		public void update(float dt) {
			super.update(dt);

			if (this.t > 1f) {
				self.become("attack");
			}
		}
	}

	public class DashState extends KnightState {
		private Vector2 vel;

		@Override
		public void onEnter() {
			super.onEnter();

			if (self.lastSeen == null) {
				self.become("idle");
			}

			float dx = self.lastSeen.x + 8 - self.x - self.w / 2;
			float dy = self.lastSeen.y + 8 - self.y - self.h / 2;
			float d = (float) Math.sqrt(dx * dx + dy * dy);

			this.vel = new Vector2();
			self.modifySpeed(100);
			this.vel.x = dx / (d + Random.newFloat(-d / 3, d / 3)) * 300;
			this.vel.y = dy / (d + Random.newFloat(-d / 3, d / 3)) * 300;

			//self.sword.setAdded(a);
			self.sword.use();
		}

		@Override
		public void onExit() {
			super.onExit();

			//self.sword.setAdded(0);
			self.modifySpeed(-100);
		}

		@Override
		public void update(float dt) {
			super.update(dt);

			this.vel.x *= 0.97;
			this.vel.y *= 0.97;

			self.acceleration.x = this.vel.x;
			self.acceleration.y = this.vel.y;

			if (this.t >= 1f) {
				self.become("chase");
			}
		}
	}
}