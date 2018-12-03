package org.rexcellentgames.burningknight.entity.creature.mob.desert;

import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

import java.util.ArrayList;

public class Mummy extends Mob {
	public static Animation animations = Animation.make("actor-mummy", "-white");

	public Animation getAnimation() {
		return animations;
	}

	private AnimationData idle;
	private AnimationData run;
	private AnimationData hurt;
	private AnimationData killed;
	private AnimationData animation;

	@Override
	protected void deathEffects() {
		super.deathEffects();
		this.playSfx("death_mummy");
		deathEffect(killed);
	}

	@Override
	protected void onHurt(int a, Entity from) {
		super.onHurt(a, from);
		this.playSfx("damage_mummy");
	}

	public static Mummy random() {
		switch (Random.newInt(3)) {
			case 0: return new Mummy();
			case 1: return new GrayMummy();
			default: return new BrownMummy();
		}
	}

	{
		w = 12;
		hpMax = 10;
		idle = getAnimation().get("idle").randomize();
		run = getAnimation().get("run").randomize();
		hurt = getAnimation().get("hurt").randomize();
		killed = getAnimation().get("death").randomize();
		animation = this.idle;
	}

	@Override
	protected State getAi(String state) {
		switch (state) {
			case "alerted": case "chase": case "roam": return new ChaseState();
			case "idle": case "tired": return new TiredState();
		}

		return super.getAi(state);
	}

	public class MummyState extends Mob.State<Mummy> {

	}

	public class ChaseState extends MummyState {
		private float delay;
		private Point to;
		private float speed;

		@Override
		public void onEnter() {
			super.onEnter();
			speed = Random.newFloat(15, 25f);

			this.delay = Random.newFloat(15, 25);
		}

		@Override
		public void update(float dt) {
			super.update(dt);

			this.checkForPlayer();

			if (self.lastSeen != null) {
				if (this.moveTo(self.lastSeen, speed * speedModifer, 4f)) {
					if (self.target == null) {
						self.noticeSignT = 0f;
						self.hideSignT = 2f;
						self.become("idle");
					}
				}
			} else {
				if (to == null) {
					to = self.room.getRandomFreeCell();
					to.x *= 16;
					to.y *= 16;
				}

				if (this.moveTo(this.to, 10f * speedModifer, 16f)) {
					this.to = null;
				}
			}

			if (this.t >= this.delay) {
				self.become("tired");
			}
		}
	}

	public class TiredState extends MummyState {
		private float delay;

		@Override
		public void onEnter() {
			super.onEnter();

			this.delay = Random.newFloat(3, 5);
		}

		@Override
		public void update(float dt) {
			super.update(dt);

			if (this.t >= this.delay) {
				self.become("chase");
			}
		}
	}

	@Override
	public void init() {
		super.init();

		this.body = this.createSimpleBody(2, 1, 8, 12, BodyDef.BodyType.DynamicBody, false);
		World.checkLocked(this.body).setTransform(this.x, this.y, 0);

		speed = 100;
		maxSpeed = 100;
	}

	@Override
	public void destroy() {
		super.destroy();
	}

	@Override
	public void render() {
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
		super.renderStats();
	}

	private float lastHit;

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

		this.lastHit += dt;

		if (this.lastHit > 1f) {
			for (Player player : this.colliding) {
				player.modifyHp(-1, this);
			}
		}

		if (this.animation != null) {
			this.animation.update(dt * speedMod);
		}

		super.common();
	}

	protected ArrayList<Player> colliding = new ArrayList<>();

	@Override
	public void onCollision(Entity entity) {
		super.onCollision(entity);

		if (entity instanceof Player) {
			colliding.add((Player) entity);
			lastHit = 0;
			((Player) entity).modifyHp(-1, this);
			((Player) entity).knockBackFrom(this, 2f * mod);

			collide((Player) entity);
		}
	}

	protected void collide(Player player) {

	}

	@Override
	public void onCollisionEnd(Entity entity) {
		super.onCollisionEnd(entity);

		if (entity instanceof Player) {
			colliding.remove(entity);
		}
	}

	protected float mod = 1f;
	protected float speedModifer = 1f;
}