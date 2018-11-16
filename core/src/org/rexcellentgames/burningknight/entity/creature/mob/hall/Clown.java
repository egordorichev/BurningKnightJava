package org.rexcellentgames.burningknight.entity.creature.mob.hall;

import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.fx.Note;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.item.Bomb;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.accessory.equippable.ProtectiveBand;
import org.rexcellentgames.burningknight.entity.item.accessory.hat.UshankaHat;
import org.rexcellentgames.burningknight.entity.item.entity.BombEntity;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.Guitar;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;
import org.rexcellentgames.burningknight.util.Random;

import java.util.ArrayList;

public class Clown extends Mob {
	public static Animation animations = Animation.make("actor-clown", "-purple");
	private AnimationData idle;
	private AnimationData run;
	private AnimationData hurt;
	private AnimationData killed;
	private AnimationData animation;
	private Guitar guitar;

	public Animation getAnimation() {
		return animations;
	}

	{
		hpMax = 6;

		idle = getAnimation().get("idle").randomize();
		run = getAnimation().get("run").randomize();
		hurt = getAnimation().get("hurt").randomize();
		killed = getAnimation().get("death").randomize();
		animation = this.idle;
	}

	@Override
	public void initStats() {
		super.initStats();
		this.setStat("blockChance", 0.7f);
	}

	@Override
	public float getWeight() {
		return 0.7f;
	}

	@Override
	public void init() {
		super.init();

		this.body = this.createSimpleBody(2, 1, 12, 12, BodyDef.BodyType.DynamicBody, false);
		World.checkLocked(this.body).setTransform(this.x, this.y, 0);

		speed = 100;
		maxSpeed = 100;

		if (!(this instanceof BurningClown)) {
			this.guitar = new Guitar();
			this.guitar.setOwner(this);
		}
	}

	@Override
	protected ArrayList<Item> getDrops() {
		ArrayList<Item> items = super.getDrops();

		if (Random.chance(15)) {
			items.add(new Bomb());
		}

		if (Random.chance(1)) {
			items.add(new Guitar());
		}

		if (Random.chance(5)) {
			items.add(new UshankaHat());
		}

		if (Random.chance(5)) {
			items.add(new ProtectiveBand());
		}

		return items;
	}

	@Override
	protected void onHurt(int a, Creature creature) {
		super.onHurt(a, creature);
		this.playSfx("damage_clown");
	}

	@Override
	public boolean rollBlock() {
		this.become("chase");
		return super.rollBlock();
	}

	@Override
	protected void deathEffects() {
		super.deathEffects();
		this.playSfx("death_clown");

		this.done = true;
		deathEffect(killed);
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (this.freezed) {
			return;
		}

		if (Math.abs(this.velocity.x) > 1f) {
			this.flipped = this.velocity.x < 0;
		}

		if (this.dead) {
			super.common();
			return;
		}

		if (this.animation != null) {
			this.animation.update(dt * speedMod);
		}

		if (guitar != null) {
			this.guitar.update(dt * speedMod);
		}

		super.common();
	}

	@Override
	public void render() {
		Graphics.batch.setColor(1, 1, 1, this.a);

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

		if (guitar != null) {
			Graphics.batch.setColor(1, 1, 1, this.a);
			this.guitar.render(this.x, this.y, this.w, this.h, this.flipped);
		}

		Graphics.batch.setColor(1, 1, 1, 1);
		super.renderStats();

		// Graphics.print(this.state, Graphics.small, this.x, this.y + 16);
	}

	@Override
	protected State getAi(String state) {
		switch (state) {
			case "getout": return new GetOutState();
			case "idle": return new IdleState();
			case "alerted": return new AlertedState();
			case "chase": return new ChasingState();
			case "wait": return new WaitState();
			case "laugh": return new LaughState();
			case "attack": return new AttackState();
			case "roam": return new RoamState();
			case "rangedAttack": return new RangedAttack();
		}

		return super.getAi(state);
	}

	public class ClownState extends State<Clown> {

	}

	public class WaitState extends ClownState {
		private float delay;

		@Override
		public void onEnter() {
			super.onEnter();
			delay = Random.newFloat(4, 12f);
		}

		@Override
		public void update(float dt) {
			super.update(dt);

			if (this.t >= delay) {
				self.become("chase");
			}
		}
	}

	private boolean placedBomb;

	public class LaughState extends ClownState {
		@Override
		public void onEnter() {
			super.onEnter();
			self.placedBomb = false;
			self.playSfx("laugh_" + Random.newInt(1, 3));
		}

		@Override
		public void update(float dt) {
			super.update(dt);

			if (t >= 15f) {
				self.become("chase");
			}
		}
	}

	public class IdleState extends ClownState {
		@Override
		public void update(float dt) {
			super.update(dt);
			this.checkForPlayer();

			if (this.target != null) {
				float d = self.getDistanceTo(self.target.x + 8, self.target.y + 8);
				self.become((d > 32f && Random.chance(75) && !(self instanceof BurningClown)) ? "rangedAttack" : "chase");
			}
		}
	}

	public class AlertedState extends ClownState {
		private static final float DELAY = 1f;

		@Override
		public void update(float dt) {
			super.update(dt);

			if (self.t >= DELAY) {
				self.become("wait");
			}
		}
	}

	public class RangedAttack extends ClownState {
		private float lastAttack;

		@Override
		public void update(float dt) {
			super.update(dt);

			this.lastAttack += dt;
			this.checkForPlayer();

			if (!self.canSee(self.target)) {
				if (!this.moveTo(self.lastSeen, 10f, 64f)) {
					return;
				}
			}

			if (self.target != null && this.lastAttack >= 1f && Random.chance(75)) {
				Note note = new Note();
				this.lastAttack = 0;

				float dx = self.x + self.w / 2 - self.target.x - self.target.w / 2 + Random.newFloat(-10f, 10f);
				float dy = self.y + self.h / 2 - self.target.y - self.target.h / 2 + Random.newFloat(-10f, 10f);

				note.a = (float) Math.atan2(-dy, -dx);
				note.x = self.x + (self.w) / 2;
				note.y = self.y + (self.h) / 2;
				note.bad = !self.stupid;

				Dungeon.area.add(note);
			}

			if (self.target != null) {
				float d = self.getDistanceTo(self.target.x + self.target.w / 2, self.target.y + self.target.h / 2);

				if (d < 64f) {
					self.become("chase");
				}
			}

			if (this.t >= 3f) {
				self.become("chase");
				// I know
				if (Random.chance(75) && !(self instanceof BurningClown)) {
					self.become("rangedAttack");
				}
			}
		}
	}

	public class ChasingState extends ClownState {
		public static final float ATTACK_DISTANCE = 24f;

		@Override
		public void update(float dt) {
			this.checkForPlayer();

			if (self.lastSeen == null) {
				self.become("idle");
				return;
			} else {
				float d = 32f;

				if (this.target != null) {
					d = self.getDistanceTo((int) (self.target.x + self.target.w / 2),
						(int) (self.target.y + self.target.h / 2));
				}

				if (this.moveTo(self.lastSeen, d < 48f ? 30f : 10f, ATTACK_DISTANCE)) {
					self.become("attack");
				}
			}

			super.update(dt);
		}
	}

	@Override
	public void destroy() {
		super.destroy();

		if (guitar != null) {
			this.guitar.destroy();
		}
	}

	public class GetOutState extends Mob.GetOutState {
		@Override
		protected String getState() {
			return ((Clown) self).placedBomb ? "laugh" : "wait";
		}
	}

	public class AttackState extends ClownState {
		private int step;

		@Override
		public void onEnter() {
			super.onEnter();
		}

		private void doAttack() {
			if (!(self instanceof BurningClown) && this.step < 3 && false) {
				self.guitar.use();
			} else {
				BombEntity e = new BombEntity(self.x, self.y).velTo(self.lastSeen.x + 8, self.lastSeen.y + 8, 60f);

				self.placedBomb = true;
				self.apply(e);
				Dungeon.area.add(e);

				for (Mob mob : Mob.all) {
					if (mob.room == self.room) {
						mob.become("getout");
					}
				}
			}

			this.step ++;
		}

		@Override
		public void update(float dt) {
			super.update(dt);

			doAttack();

			/*
			float d = 32f;

			if (self.target != null) {
				d = self.getDistanceTo((int) (self.target.x + self.target.w / 2),
					(int) (self.target.y + self.target.h / 2));
			}

			if (d < 16 || this.moveTo(self.target, d < 48f ? 20f : 10f, 16)) {
				if (d > 16) {
					this.step = 0;
				}

				if (this.t > 1f) {
					this.t = 0;
					this.doAttack();
				}
			}*/
		}
	}

	public void apply(BombEntity bomb) {

	}

	public class RoamState extends ClownState {
		@Override
		public void onEnter() {
			super.onEnter();
			this.findNearbyPoint();
		}

		@Override
		public void update(float dt) {
			if (this.targetPoint != null && this.moveTo(this.targetPoint, 6f, 8f)) {
				self.become("idle");
				return;
			}

			this.checkForPlayer();
			super.update(dt);
		}
	}
}