package org.rexcellentgames.burningknight.entity.creature.mob.tech;

import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.mob.common.MovingFly;
import org.rexcellentgames.burningknight.entity.pool.MobHub;
import org.rexcellentgames.burningknight.entity.pool.MobPool;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;
import org.rexcellentgames.burningknight.util.Random;

public class Factory extends Bot {
	public static Animation animations = Animation.make("actor-factory", "-normal");
	private AnimationData idle;
	private AnimationData killed;
	private AnimationData hurt;
	private AnimationData spawn;
	private AnimationData animation;

	public Animation getAnimation() {
		return animations;
	}

	{
		hpMax = 16;

		idle = getAnimation().get("idle");
		hurt = getAnimation().get("hurt");
		killed = getAnimation().get("dead");
		spawn = getAnimation().get("spawn");
		animation = idle;

		w = 25;
		h = 19;
	}

	@Override
	public void init() {
		super.init();

		this.body = this.createSimpleBody(1, 0, (int) w - 2, (int) h - 2, BodyDef.BodyType.DynamicBody, false);
		World.checkLocked(this.body).setTransform(this.x, this.y, 0);
	}

	@Override
	public void destroy() {
		super.destroy();
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
		} else if (this.state.equals("spawn")) {
			this.animation = spawn;
		} else {
			this.animation = idle;
		}

		this.renderWithOutline(this.animation);
		Graphics.batch.setColor(1, 1, 1, 1);
		super.renderStats();
	}

	@Override
	public void update(float dt) {
		super.update(dt);

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
			case "idle": case "alerted": case "roam": return new IdleState();
			case "spawn": return new SpawnState();
		}

		return super.getAi(state);
	}

	public class IdleState extends Mob.State<Factory> {
		private float delay;

		@Override
		public void onEnter() {
			super.onEnter();
			delay = Random.newFloat(12f, 20f);
		}

		@Override
		public void update(float dt) {
			checkForPlayer();

			if (self.target != null && self.target.room == self.room) {
				t += dt;

				if (t >= delay) {
					self.become("spawn");
				}
			} else {
				t = 0;
			}
		}
	}

	@Override
	public void knockBackFrom(Entity from, float force) {

	}

	public class SpawnState extends Mob.State<Factory> {
		@Override
		public void onEnter() {
			super.onEnter();
			self.spawn.setFrame(0);
			self.spawn.setAutoPause(true);
			self.spawn.setPaused(false);
		}

		@Override
		public void update(float dt) {
			super.update(dt);

			if (self.spawn.isPaused() && t >= 0.4f) {
				self.poof();
				self.become("idle");

				Mob mob = generateMob();
				mob.x = self.x + (self.w - mob.w) / 2;
				mob.y = self.y - 1;
				mob.noLoot = true;
				Dungeon.area.add(mob.add());
			}
		}
	}

	public static Mob generateMob() {
		MobPool.instance.initForFloor();
		MobHub hub = MobPool.instance.generate();

		for (Class<? extends Mob> type : hub.types) {
			if (type == Factory.class) {
				return generateMob();
			}
		}

		try {
			return hub.types.get(Random.newInt(hub.types.size())).newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return new MovingFly();
	}
}