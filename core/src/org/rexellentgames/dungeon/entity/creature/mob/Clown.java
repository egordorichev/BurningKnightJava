package org.rexellentgames.dungeon.entity.creature.mob;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.creature.fx.Fireball;
import org.rexellentgames.dungeon.entity.creature.fx.Note;
import org.rexellentgames.dungeon.entity.item.Bomb;
import org.rexellentgames.dungeon.entity.item.Item;
import org.rexellentgames.dungeon.entity.item.entity.BombEntity;
import org.rexellentgames.dungeon.entity.item.weapon.Guitar;
import org.rexellentgames.dungeon.entity.item.weapon.magic.FireBook;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.util.Animation;
import org.rexellentgames.dungeon.util.AnimationData;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.Random;

import java.util.ArrayList;

public class Clown extends Mob {
	private static Animation animations = Animation.make("actor-clown");
	private AnimationData idle;
	private AnimationData run;
	private AnimationData hurt;
	private AnimationData killed;
	private AnimationData laugh;
	private AnimationData animation;
	private boolean toLaugh;
	private float laughT = 3f;
	private Guitar guitar;

	{
		hpMax = 10;
		hide = true;

		idle = animations.get("idle").randomize();
		run = animations.get("run").randomize();
		hurt = animations.get("hurt").randomize();
		killed = animations.get("dead").randomize();
		laugh = animations.get("laugh").randomize();
		animation = this.idle;
	}

	@Override
	public void init() {
		super.init();

		this.body = this.createBody(2, 1, 12, 12, BodyDef.BodyType.DynamicBody, false);
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
			items.add(new FireBook());
		}

		if (Random.chance(10)) {
			items.add(new Guitar());
		}

		return items;
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (this.dead) {
			super.common();
			return;
		}

		if (this.animation != null) {
			this.animation.update(dt);
		}

		this.guitar.update(dt);

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
		} else if (this.laughT > 0) {
			this.laughT -= Gdx.graphics.getDeltaTime();
			this.animation = this.laugh;
		} else {
			this.animation = idle;
		}

		this.animation.render(this.x, this.y, this.flipped);
		Graphics.batch.setColor(1, 1, 1, this.a);
		this.guitar.render(this.x, this.y, this.w, this.h, this.flipped);
	}

	@Override
	protected State getAi(String state) {
		if (state.equals("idle") || state.equals("laugh")) {
			return new IdleState();
		} else if (state.equals("alerted")) {
			return new AlertedState();
		} else if (state.equals("chase")) {
			return new ChasingState();
		} else if (state.equals("attack")) {
			return new AttackState();
		} else if (state.equals("fleeing")) {
			return new FleeingState();
		} else if (state.equals("roam")) {
			return new RoamState();
		} else if (state.equals("rangedAttack")) {
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

			if (self.target == null) {
				self.become("fleeing");
				return;
			}

			if (self.t >= DELAY) {
				float d = self.getDistanceTo(self.target.x + 8, self.target.y + 8);
				self.become((d > 32f && Random.chance(75)) ? "rangedAttack" : "chase");
			}
		}
	}

	public class RangedAttack extends ClownState {
		@Override
		public void update(float dt) {
			super.update(dt);

			if (this.t % 0.5f < 0.0175f && Random.chance(75)) {
				Note note = new Note();

				float dx = self.x + self.w / 2 - self.target.x - self.target.w / 2 + Random.newFloat(-10f, 10f);
				float dy = self.y + self.h / 2 - self.target.y - self.target.h / 2 + Random.newFloat(-10f, 10f);
				float a = (float) Math.atan2(-dy, -dx);

				note.a = a;
				note.x = self.x + 2;
				note.y = self.y + 2;

				Dungeon.area.add(note);
			}

			if (this.t >= 3f) {
				if (self.mind == Mind.RAT || self.mind == Mind.COWARD) {
					self.become("fleeing");
					return;
				}

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

	public class AttackState extends ClownState {
		@Override
		public void onEnter() {
			super.onEnter();

			if (Random.chance(25)) {
				self.guitar.use();
			} else {
				self.flee = 1.5f;
				self.become("fleeing");
				self.laughT = 3f;
				// fixme: it places TooO MANY
				Dungeon.area.add(new BombEntity(self.x, self.y).velTo(self.lastSeen.x + 8, self.lastSeen.y + 8));
			}
		}

		@Override
		public void update(float dt) {
			if (self.guitar.getDelay() == 0) {
				self.become(self.mind == Mind.COWARD || self.mind == Mind.RAT ? "fleeing" : "chase");
			}
		}
	}

	public class FleeingState extends ClownState {
		@Override
		public void onEnter() {
			super.onEnter();
			if ((self.mind == Mind.DEFENDER && Random.chance(75)) || Random.chance(25)) {
				self.guitar.secondUse();
			}
		}

		@Override
		public void update(float dt) {
			if (self.guitar.getDelay() == 0 && ( self.mind == Mind.DEFENDER || self.mind == Mind.RAT)) {
				self.guitar.secondUse();
			}

			this.findNearbyPoint();
			self.flee = Math.max(0, self.flee - (self.mind == Mind.ATTACKER ? 0.03f : 0.01f));

			if (this.targetPoint != null && this.moveTo(this.targetPoint, 16f, 8f)) {
				self.flee = 0;
				self.become(self.toLaugh ? "laugh" : "idle");
				self.toLaugh = false;

				return;
			} else if (self.flee == 0f) {
				self.become("idle");
			}

			super.update(dt);
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