package org.rexcellentgames.burningknight.entity.creature.mob.tutorial;

import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Audio;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.buff.BurningBuff;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.item.Explosion;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.Gun;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.BulletProjectile;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.game.state.InGameState;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class Ninjia extends Mob {
	public static Animation animations = Animation.make("actor-ninja", "-gray");
	private AnimationData hurt;
	private AnimationData killed;
	private AnimationData idle;
	private AnimationData run;
	private AnimationData animation;

	public Animation getAnimation() {
		return animations;
	}

	{
		hpMax = 12;

		idle = getAnimation().get("idle");
		run = getAnimation().get("run");
		hurt = getAnimation().get("hurt");
		killed = getAnimation().get("death");
		animation = this.idle;
	}

	@Override
	public void init() {
		super.init();

		w = 14;

		this.body = this.createSimpleBody(1, 0, 12, 16, BodyDef.BodyType.DynamicBody, false);
		World.checkLocked(this.body).setTransform(this.x, this.y, 0);
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

		if (doTp) {
			poof();

			if (this.hasBuff(BurningBuff.class)) {
				boolean found = false;

				for (int i = 0; i < 100; i++) {
					Point point = this.room.getRandomFreeCell();

					if (Dungeon.level.liquidData[Level.toIndex((int) point.x, (int) point.y)] == Terrain.WATER) {
						this.tp(point.x * 16, point.y * 16 - 8);
						found = true;
						break;
					}
				}

				if (!found) {
					Point point = this.room.getRandomFreeCell();
					this.tp(point.x * 16, point.y * 16 - 8);
				}
			} else {
				Point point = this.room.getRandomFreeCell();
				this.tp(point.x * 16, point.y * 16 - 8);
			}

			poof();

			doTp = false;
		}

		if (this.animation != null) {
			this.animation.update(dt * speedMod * 1.3f);
		}

		super.common();
	}

	@Override
	public void render() {
		if (Math.abs(this.velocity.x) > 1f) {
			this.flipped = this.velocity.x < 0;
		} else if (this.target != null) {
			this.flipped = this.target.x + this.target.w / 2 < this.x + this.w / 2;
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

		Graphics.batch.setColor(1, 1, 1, this.a * 0.75f);
		this.renderWithOutline(this.animation);
		Graphics.batch.setColor(1, 1, 1, 1);
		super.renderStats();
	}

	@Override
	protected void onHurt(int a, Creature from) {
		super.onHurt(a, from);
		this.playSfx("damage_clown");

		this.waitT = 1f;

		if (this.hp > 0) {
			this.doTp = true;
		}
	}

	@Override
	public void initStats() {
		super.initStats();

		setStat("knockback", 0);
	}

	protected float waitT;
	private boolean doTp;

	@Override
	protected void deathEffects() {
		super.deathEffects();
		this.playSfx("death_clown");

		this.done = true;
		deathEffect(killed);

		if (Dungeon.depth == -3) {
			Dungeon.area.add(new Explosion(this.x + this.w / 2, this.y + this.h / 2));
			this.playSfx("explosion");

			Audio.highPriority("Reckless");
		}
	}

	@Override
	protected State getAiWithLow(String state) {
		return getAi(state);
	}

	public class NinjiaState extends Mob.State<Ninjia> {

	}

	@Override
	protected State getAi(String state) {
		switch (state) {
			case "idle": return new IdleState();
			case "chase": case "roam": case "alerted": return new AttackState();
		}

		return super.getAi(state);
	}

	public class IdleState extends NinjiaState {
		@Override
		public void update(float dt) {
			super.update(dt);

			checkForPlayer();

			if (target != null) {
				self.become("attack");
				self.waitT = 1f;
			}
		}
	}

	@Override
	public void destroy() {
		super.destroy();
		InGameState.forceBoss = false;
	}

	public class AttackState extends NinjiaState {
		private float last;

		@Override
		public void update(float dt) {
			super.update(dt);

			if (self.waitT > 0) {
				self.waitT -= dt;
				return;
			}

			if (this.t >= 0.5f) {
				this.t = 0;

				if (!self.canSee(self.target)) {
					// InGameState.forceBoss = false;
					return;
				}

				InGameState.forceBoss = true;

				float an = self.getAngleTo(self.target.x + self.target.w / 2, self.target.y + self.target.h / 2);
				an = Gun.angleLerp(this.last, an, 0.55f, self.freezed);
				this.last = an;

				BulletProjectile ball = new BulletProjectile();

				ball.velocity = new Point((float) Math.cos(an) / 2f, (float) Math.sin(an) / 2f).mul(60f * 1.5f);

				ball.x = (float) (self.x + self.w / 2 + Math.cos(an) * 8);
				ball.damage = 4;
				ball.y = (float) (self.y + Math.sin(an) * 8 + 6);
				ball.owner = self;
				ball.bad = true;
				ball.letter = "item-small_shuriken";
				ball.rotates = true;
				ball.second = false;

				Dungeon.area.add(ball);

				self.playSfx("throw");
			}
		}
	}
}