package org.rexellentgames.dungeon.entity.creature.mob;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.Settings;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.creature.Creature;
import org.rexellentgames.dungeon.entity.creature.fx.BloodFx;
import org.rexellentgames.dungeon.entity.creature.fx.GoreFx;
import org.rexellentgames.dungeon.entity.item.Item;
import org.rexellentgames.dungeon.entity.item.accessory.hat.KnightHat;
import org.rexellentgames.dungeon.entity.item.weapon.Weapon;
import org.rexellentgames.dungeon.entity.item.weapon.bow.BowA;
import org.rexellentgames.dungeon.entity.item.weapon.gun.BadGun;
import org.rexellentgames.dungeon.entity.item.weapon.gun.GunA;
import org.rexellentgames.dungeon.entity.item.weapon.gun.bullet.BadBullet;
import org.rexellentgames.dungeon.entity.item.weapon.rocketlauncher.RocketLauncherA;
import org.rexellentgames.dungeon.entity.item.weapon.rocketlauncher.rocket.Rocket;
import org.rexellentgames.dungeon.entity.item.weapon.sword.SwordA;
import org.rexellentgames.dungeon.entity.item.weapon.sword.Sword;
import org.rexellentgames.dungeon.entity.item.weapon.magic.DefenseBook;
import org.rexellentgames.dungeon.entity.item.weapon.magic.FireBook;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.game.input.Input;
import org.rexellentgames.dungeon.util.Animation;
import org.rexellentgames.dungeon.util.AnimationData;
import org.rexellentgames.dungeon.util.Random;
import org.rexellentgames.dungeon.util.geometry.Point;

import java.util.ArrayList;

public class Knight extends Mob {
	public static Animation animations = Animation.make("actor-towelknight");
	protected Item sword;
	private AnimationData idle;
	private AnimationData run;
	private AnimationData hurt;
	private AnimationData killed;
	private AnimationData animation;

	{
		hpMax = 3;
		speed = 5;
		guard = true;

		idle = animations.get("idle").randomize();
		run = animations.get("run").randomize();
		hurt = animations.get("hurt").randomize();
		killed = animations.get("dead").randomize();
		animation = this.idle;
	}

	@Override
	protected void onHurt(float a, Creature creature) {
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
			this.animation.update(dt);
		}

		this.sword.update(dt);
		super.common();
	}

	@Override
	public void render() {
		if (this.target != null) {
			float dx = this.x + this.w / 2 - this.target.x - this.target.w / 2;
			this.flipped = dx >= 0;
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

		// Graphics.print(this.state, Graphics.small, this.x, this.y);

		/*
		if (this.ai.nextPathPoint != null) {
			Graphics.batch.end();
			Graphics.shape.setColor(1, 0, 1, 1);
			Graphics.shape.begin(ShapeRenderer.ShapeType.Filled);
			Graphics.shape.line(this.x + 8, this.y + 8, this.ai.nextPathPoint.x + 8, this.ai.nextPathPoint.y + 16);
			Graphics.shape.end();
			Graphics.shape.setColor(1, 1, 1, 1);
			Graphics.batch.begin();
		}
		if (this.ai.targetPoint != null) {
			Graphics.batch.end();
			Graphics.shape.begin(ShapeRenderer.ShapeType.Filled);
			Graphics.shape.line(this.x + 8, this.y + 8, this.ai.targetPoint.x + 8, this.ai.targetPoint.y + 8);
			Graphics.shape.end();
			Graphics.batch.begin();
		}*/
	}

	@Override
	protected ArrayList<Item> getDrops() {
		ArrayList<Item> items = super.getDrops();

		if (Random.chance(10)) {
			items.add(new FireBook());
		}

		if (Random.chance(10)) {
			items.add(new DefenseBook());
		}

		if (Random.chance(10)) {
			items.add(new KnightHat());
		}

		if (Random.chance(5)) {
			// items.add(new SwordA());
		}

		if (Random.chance(25)) {
			// items.add(new AnotherSword());
		}

		if (Random.chance(25)) {
			// items.add(new WoodenBow());
		}

		if (Random.chance(20)) {
			// items.add(new Arrow().setCount(Random.newInt(3, 5)));
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
			case "fleeing": return new FleeingState();
			case "roam": return new RoamState();
			case "dash": return new DashState();
		}

		return super.getAi(state);
	}

	public class KnightState extends State<Knight> {

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

	public class FleeingState extends KnightState {
		@Override
		public void onEnter() {
			super.onEnter();
			if ((self.mind == Mind.DEFENDER && Random.chance(75)) || Random.chance(25)) {
				self.sword.secondUse();
			}
		}

		@Override
		public void update(float dt) {
			if (self.sword.getDelay() == 0 && ( self.mind == Mind.DEFENDER || self.mind == Mind.RAT)) {
				self.sword.secondUse();
			}

			this.findNearbyPoint();
			self.flee = Math.max(0, self.flee - (self.mind == Mind.ATTACKER ? 0.03f : 0.01f));

			if (this.targetPoint == null) {
				self.become("idle");
			} else if (this.moveTo(this.targetPoint, 6f, 8f)) {
				self.flee = 0;
				self.become("idle");
				return;
			} else if (self.flee <= 0.3f) {
				self.flee = 0;
				self.become("idle");
			}

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
		Dungeon.level.removeSaveable(this);

		if (Settings.gore) {
			for (Animation.Frame frame : killed.getFrames()) {
				GoreFx fx = new GoreFx();

				fx.texture = frame.frame;
				fx.x = this.x + this.w / 2;
				fx.y = this.y + this.h / 2;

				Dungeon.area.add(fx);
			}
		}

		BloodFx.add(this, 20);
	}

	public class ChaseState extends KnightState {
		public static final float ATTACK_DISTANCE = 16f;
		public static final float DASH_DIST = 48f;
		public float delay;
		private float att;

		@Override
		public void onEnter() {
			super.onEnter();
			this.delay = Random.newFloat(8f, 10f);

			if (self.sword instanceof Sword) {
				this.att = ATTACK_DISTANCE;
			} else {
				this.att = 128f;
			}
		}

		@Override
		public void update(float dt) {
			this.checkForPlayer();

			if (self.lastSeen == null) {
				self.become("idle");
				return;
			} else {
				if (this.moveTo(self.lastSeen, 5f,this.att)) {
					if (self.target != null && self.getDistanceTo((int) (self.target.x + self.target.w / 2),
						(int) (self.target.y + self.target.h / 2)) <= this.att) {

						self.become("attack");
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
		}

		@Override
		public void update(float dt) {
			if (self.sword.getDelay() == 0) {
				self.become(self.mind == Mind.RAT ? "fleeing" : "chase");
				this.checkForPlayer();
				return;
			}

			if (Random.chance(0.1f)) {
				self.become("dash");
			}

			super.update(dt);
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
			this.vel.x = dx / d * 300;
			this.vel.y = dy / d * 300;

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

			self.vel.x = this.vel.x;
			self.vel.y = this.vel.y;

			if (this.t >= 1f) {
				self.become("chase");
			}
		}
	}
}