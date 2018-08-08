package org.rexcellentgames.burningknight.entity.creature.mob.desert;

import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.weapon.Weapon;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.BulletProjectile;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.tool.PickaxeA;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.tool.ShovelA;
import org.rexcellentgames.burningknight.entity.level.save.LevelSave;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class Archeologist extends Mob {
	public static Animation animations = Animation.make("actor-archeologist", "-green");
	private AnimationData idle;
	private AnimationData run;
	private AnimationData hurt;
	private AnimationData killed;
	private AnimationData animation;

	public Animation getAnimation() {
		return animations;
	}

	{
		hpMax = 10;
		idle = getAnimation().get("idle").randomize();
		run = getAnimation().get("run").randomize();
		hurt = getAnimation().get("hurt").randomize();
		killed = getAnimation().get("death").randomize();
		animation = this.idle;
		speed = 100;
		maxSpeed = 100;
	}

	private Item weapon;

	@Override
	protected State getAi(String state) {
		switch (state) {
			case "idle": case "roam": return new IdleState();
			case "alerted": return new AlertedState();
			case "move": case "chase": return new MoveState();
			case "dig": return new DigState();
			case "predig": return new PredigState();
		}

		return super.getAi(state);
	}

	public class ArcheologistState extends Mob.State<Archeologist> {

	}

	public class IdleState extends ArcheologistState {
		@Override
		public void update(float dt) {
			super.update(dt);
			this.checkForPlayer();
		}
	}

	public class AlertedState extends ArcheologistState {
		@Override
		public void onEnter() {
			super.onEnter();

			for (Mob mob : Mob.all) {
				if (mob != self && mob.room == self.room) {
					if (!mob.saw && (mob.getState().equals("idle") || mob.getState().equals("roam"))) {
						mob.target = self.target;
						mob.saw = true;
						mob.become("chase");
					}
				}
			}
		}

		@Override
		public void update(float dt) {
			super.update(dt);

			if (this.t >= 0.5f) {
				self.become("move");
			}
		}
	}

	protected boolean toPlayer;

	public class MoveState extends ArcheologistState {
		private Point to;

		@Override
		public void onEnter() {
			super.onEnter();

			if (toPlayer) {
				to = new Point(self.lastSeen.x, self.lastSeen.y);
			} else {
				to = self.room.getRandomFreeCell();

				to.x *= 16;
				to.y *= 16;
			}
		}

		@Override
		public void update(float dt) {
			super.update(dt);
			this.checkForPlayer();

			if (this.moveTo(this.to, 8, 16f)) {
				self.become("predig");
			}
		}
	}

	protected boolean triple;
	protected float skeletonChance = 5;

	public class PredigState extends ArcheologistState {
		@Override
		public void update(float dt) {
			super.update(dt);

			if (this.t >= 2f) {
				self.become("dig");
			}
		}
	}

	public class DigState extends ArcheologistState {
		@Override
		public void onEnter() {
			super.onEnter();

			self.weapon.use();
		}

		private void sendBone(float a) {
			BulletProjectile ball = new BulletProjectile();

			ball.vel = new Point((float) Math.cos(a) / 2f, (float) Math.sin(a) / 2f).mul(60f * shotSpeedMod);

			ball.x = (float) (self.x + self.w / 2 + Math.cos(a) * 8);
			ball.damage = 2;
			ball.owner = self;
			ball.circleShape = true;
			ball.rotates = true;
			ball.y = (float) (self.y + Math.sin(a) * 8 + 6);

			ball.letter = "bone";
			ball.bad = true;

			Dungeon.area.add(ball);
		}

		private boolean did;

		@Override
		public void update(float dt) {
			super.update(dt);
			this.checkForPlayer();

			if (self.target == null) {
				self.become("idle");
				return;
			}

			if (!this.did && this.t > ((Weapon) self.weapon).timeA) {
				if (Random.chance(skeletonChance)) {
					Mummy skeleton = Mummy.random();

					skeleton.x = self.x;
					skeleton.y = self.y;
					skeleton.generate();

					Dungeon.area.add(skeleton);
					LevelSave.add(skeleton);
				} else {
					Point aim = self.getAim();
					float a = (float) (self.getAngleTo(aim.x, aim.y) - Math.PI * 2);
					float ac = 2f;

					if (triple) {
						this.sendBone((float) (a + Math.toRadians(Random.newFloat(-ac, ac) + 20f)));
						this.sendBone((float) (a + Math.toRadians(Random.newFloat(-ac, ac) - 20f)));
					}

					this.sendBone((float) (a + Math.toRadians(Random.newFloat(-ac, ac))));
				}

				this.did = true;
			}

			if (this.t >= 5f) {
				self.become("move");
			}
		}
	}

	@Override
	public void init() {
		super.init();

		this.body = this.createSimpleBody(2, 1, 12, 12, BodyDef.BodyType.DynamicBody, false);
		this.body.setTransform(this.x, this.y, 0);

		this.weapon = Random.chance(50) ? new PickaxeA() : new ShovelA();
		this.weapon.setOwner(this);
	}

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

		if (this.animation != null) {
			this.animation.update(dt * speedMod);
		}

		this.weapon.update(dt * speedMod);

		super.common();

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
		this.weapon.render(this.x, this.y, this.w, this.h, this.flipped);
	}

	@Override
	public void destroy() {
		super.destroy();

		if (this.weapon != null) {
			this.weapon.destroy();
		}
	}

	@Override
	protected void die(boolean force) {
		super.die(force);

		this.playSfx("death_clown");

		this.done = true;
		deathEffect(killed);
	}
}