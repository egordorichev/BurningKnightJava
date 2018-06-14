package org.rexcellentgames.burningknight.entity.creature.mob;

import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.fx.Note;
import org.rexcellentgames.burningknight.entity.item.Bomb;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.accessory.hat.UshankaHat;
import org.rexcellentgames.burningknight.entity.item.entity.BombEntity;
import org.rexcellentgames.burningknight.entity.item.weapon.Guitar;
import org.rexcellentgames.burningknight.entity.item.weapon.magic.NoteBook;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;
import org.rexcellentgames.burningknight.util.Random;

import java.util.ArrayList;

public class Clown extends Mob {
	public static Animation animations = Animation.make("actor-clown-v2", "-purple");
	private AnimationData idle;
	private AnimationData run;
	private AnimationData hurt;
	private AnimationData killed;
	private AnimationData animation;
	private Guitar guitar;

	@Override
	public float getWeight() {
		return 0.7f;
	}

	{
		hpMax = 3;
		experienceDropped = 2;

		idle = animations.get("idle").randomize();
		run = animations.get("run").randomize();
		hurt = animations.get("hurt").randomize();
		killed = animations.get("death").randomize();
		animation = this.idle;
	}

	@Override
	public void init() {
		super.init();

		this.body = this.createSimpleBody(2, 1, 12, 12, BodyDef.BodyType.DynamicBody, false);
		this.body.setTransform(this.x, this.y, 0);

		speed = 100;
		maxSpeed = 100;

		this.guitar = new Guitar();
		this.guitar.setOwner(this);
	}

	@Override
	protected ArrayList<Item> getDrops() {
		ArrayList<Item> items = super.getDrops();

		if (Random.chance(50)) {
			items.add(new Bomb());
		}

		if (Random.chance(10)) {
			items.add(new NoteBook());
		}

		if (Random.chance(10)) {
			items.add(new Guitar());
		}

		if (Random.chance(5)) {
			items.add(new UshankaHat());
		}

		return items;
	}

	@Override
	protected void onHurt(float a, Creature creature) {
		super.onHurt(a, creature);
		this.playSfx("damage_clown");
	}

	@Override
	protected void die(boolean force) {
		super.die(force);

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

		if (this.dead) {
			super.common();
			return;
		}

		if (this.animation != null) {
			this.animation.update(dt * speedMod);
		}

		this.guitar.update(dt * speedMod);

		super.common();
	}

	@Override
	public void render() {
		Graphics.batch.setColor(1, 1, 1, this.a);

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
		this.guitar.render(this.x, this.y, this.w, this.h, this.flipped);
		Graphics.batch.setColor(1, 1, 1, 1);
	}

	@Override
	protected State getAi(String state) {
		switch (state) {
			case "idle":
				return new IdleState();
			case "alerted":
				return new AlertedState();
			case "chase":
				return new ChasingState();
			case "attack":
				return new AttackState();
			case "roam":
				return new RoamState();
			case "rangedAttack":
				return new RangedAttack();
		}

		return super.getAi(state);
	}

	public class ClownState extends State<Clown> {

	}

	public class IdleState extends ClownState {
		@Override
		public void update(float dt) {
			super.update(dt);
			this.checkForPlayer();
		}
	}

	public class AlertedState extends ClownState {
		private static final float DELAY = 1f;

		@Override
		public void update(float dt) {
			super.update(dt);

			if (self.t >= DELAY) {
				float d = self.getDistanceTo(self.target.x + 8, self.target.y + 8);
				self.become((d > 32f && Random.chance(75)) ? "rangedAttack" : "chase");
			}
		}
	}

	public class RangedAttack extends ClownState {
		private float lastAttack;

		@Override
		public void update(float dt) {
			super.update(dt);

			this.lastAttack += dt;

			if (this.lastAttack >= 0.5f && Random.chance(75)) {
				Note note = new Note();
				this.lastAttack = 0;

				float dx = self.x + self.w / 2 - self.target.x - self.target.w / 2 + Random.newFloat(-10f, 10f);
				float dy = self.y + self.h / 2 - self.target.y - self.target.h / 2 + Random.newFloat(-10f, 10f);

				note.a = a;
				note.x = self.x + 2;
				note.y = self.y + 2;
				note.bad = !self.stupid;

				Dungeon.area.add(note);
			}

			if (this.t >= 3f) {
				self.become("chase");
				// I know
				if (Random.chance(75)) {
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
				return;
			} else {
				float d = 32f;

				if (this.target != null) {
					d = self.getDistanceTo((int) (self.target.x + self.target.w / 2),
						(int) (self.target.y + self.target.h / 2));
				}

				if (this.moveTo(self.lastSeen, d < 48f ? 20f : 10f, ATTACK_DISTANCE)) {
					if (self.target != null) {

						self.become("attack");
					} else if (self.target == null) {
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

	@Override
	public void destroy() {
		super.destroy();
		this.guitar.destroy();
	}

	public class AttackState extends ClownState {
		private float time;

		@Override
		public void onEnter() {
			super.onEnter();

			if (Random.chance(75)) {
				self.guitar.use();
			} else {
				BombEntity e = new BombEntity(self.x, self.y).velTo(self.lastSeen.x + 8, self.lastSeen.y + 8);

				Dungeon.area.add(e);
				time = 0.5f;
			}
		}

		@Override
		public void update(float dt) {
			time -= dt;
			if (self.guitar.getDelay() == 0 && time <= 0) {
				self.become("chase");
			}
		}
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