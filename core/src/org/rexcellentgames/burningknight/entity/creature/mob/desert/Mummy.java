package org.rexcellentgames.burningknight.entity.creature.mob.desert;

import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
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
	protected void die(boolean force) {
		super.die(force);

		this.playSfx("death_mummy");

		this.done = true;
		deathEffect(killed);
	}

	@Override
	protected void onHurt(float a, Creature from) {
		super.onHurt(a, from);
		this.playSfx("damage_mummy");
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

		@Override
		public void onEnter() {
			super.onEnter();

			this.delay = Random.newFloat(7, 15);
		}

		@Override
		public void update(float dt) {
			super.update(dt);

			this.checkForPlayer();

			if (self.target != null) {
				this.moveTo(self.lastSeen, 3f * speedModifer, 8f);
			} else {
				if (to == null) {
					to = self.room.getRandomFreeCell();
					to.x *= 16;
					to.y *= 16;
				}

				if (this.moveTo(this.to, 2f * speedModifer, 16f)) {
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

		this.body = this.createSimpleBody(2, 1, 12, 12, BodyDef.BodyType.DynamicBody, false);
		this.body.setTransform(this.x, this.y, 0);

		speed = 100;
		maxSpeed = 100;
	}

	@Override
	public void destroy() {
		super.destroy();
	}

	@Override
	public void render() {
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
	}

	private float lastHit;

	@Override
	public void update(float dt) {
		super.update(dt);

		if (this.freezed) {
			return;
		}

		if (Math.abs(this.vel.x) > 1f) {
			this.flipped = this.vel.x < 0;
		}

		if (this.dead) {
			super.common();
			return;
		}

		this.lastHit += dt;

		if (this.lastHit > 1f) {
			for (Player player : this.colliding) {
				player.modifyHp(-4, this);
				player.knockBackFrom(this, 1000f * mod);
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
			((Player) entity).modifyHp(-4, this);
			((Player) entity).knockBackFrom(this, 1000f * mod);

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