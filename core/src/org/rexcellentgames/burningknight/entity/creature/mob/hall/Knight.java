package org.rexcellentgames.burningknight.entity.creature.mob.hall;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.Gun;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.Sword;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.entities.fx.PoofFx;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.Tween;

import java.util.ArrayList;

public class Knight extends Mob {
	public static Animation animations = Animation.make("actor-knight", "-blue");
	private AnimationData idle;
	private AnimationData run;
	private AnimationData hurt;
	private AnimationData killed;
	private AnimationData animation;

	public Animation getAnimation() {
		return animations;
	}
	protected Item sword;

	{
		hpMax = 7;
		speed = 5;

		idle = getAnimation().get("idle").randomize();
		run = getAnimation().get("run").randomize();
		hurt = getAnimation().get("hurt").randomize();
		killed = getAnimation().get("death").randomize();
		animation = this.idle;
	}

	@Override
	protected void onHurt(int a, Entity creature) {
		super.onHurt(a, creature);
		this.playSfx("damage_towelknight");
	}

	@Override
	public boolean rollBlock() {
		if (!this.state.equals("preattack") && !this.state.equals("attack")) {
			this.become("chase");
			return false;
		}

		return super.rollBlock();
	}

	@Override
	public void init() {
		super.init();

		this.sword = new Sword();
		this.sword.setOwner(this);

		this.body = this.createSimpleBody(2, 1,12, 12, BodyDef.BodyType.DynamicBody, false);
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

		if (this.animation != null) {
			this.animation.update(dt * speedMod);
		}

		this.sword.update(dt * speedMod);
		this.sword.updateInHands(dt * speedMod);
		super.common();
	}

	@Override
	public void render() {
		if (Math.abs(this.velocity.x) > 1f) {
			this.flipped = this.velocity.x < 0;
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

		this.sword.render(this.x, this.y, this.w, this.h, this.flipped, false);
		Graphics.batch.setColor(1, 1, 1, 1);
		super.renderStats();
	}

	@Override
	protected ArrayList<Item> getDrops() {
		ArrayList<Item> items = super.getDrops();

		if (Random.chance(5)) {
			items.add(new Sword());
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
			case "wait": return new WaitState();
			case "idle": case "roam": return new RoamState();
			case "chase": return new ChaseState();
			case "dash": return new DashState();
			case "preattack": return new PreAttackState();
			case "attack": return new AttackingState();
			case "alerted": return new SawState();
			case "predash": return new PredashState();
		}

 		return super.getAi(state);
	}

	public class KnightState extends State<Knight> {

	}

	public class SawState extends KnightState {
		@Override
		public void update(float dt) {
			super.update(dt);

			if (this.t >= 1f) {
				self.become("chase");
			}
		}
	}

	@Override
	public void renderSigns() {
		super.renderSigns();

		if (this.sword instanceof Gun) {
			((Gun) this.sword).renderReload();
		}
	}

	@Override
	public void renderShadow() {
		Graphics.shadowSized(this.x, this.y, this.w, this.h, 6);
	}

	public void checkDir() {

	}

	@Override
	public boolean shouldCollide(Object entity, Contact contact, Fixture fixture) {
		if (!(entity instanceof Mob)) {
			checkDir();
		}

		return super.shouldCollide(entity, contact, fixture);
	}

	public class RoamState extends KnightState {
		/*private float delay;
		private float wait;
		private Vector2 direction = new Vector2();
		private Vector2 last = new Vector2();

		@Override
		public void onEnter() {
			selectPoint();
		}

		public void selectPoint() {
			this.delay = Random.newFloat(4, 8f);
			this.wait = Random.chance(50) ? Random.newFloat(1, 2) : 0;
		}

		private void selectDir() {
			if (Random.chance(50)) {
				this.direction.x = Random.chance(50) ? 1 : -1;
				this.direction.y = 0;
			} else {
				this.direction.x = 0;
				this.direction.y = Random.chance(50) ? 1 : -1;
			}

			int x = (int) ((self.x + self.w / 2) / 16);
			int y = (int) ((self.y + self.h / 2) / 16);

			if (Dungeon.level.checkFor(x, y, Terrain.PASSABLE) && !Dungeon.level.checkFor(x + (int) direction.x, y + (int) direction.y, Terrain.PASSABLE)) {
				selectDir();
			}
		}

		public void selectDirs() {
			do {
				selectDir();
			} while (direction.x == last.x && direction.y == last.y);
		}
*/
		@Override
		public void update(float dt) {
			super.update(dt);
			this.checkForPlayer();

			if (self.target != null && self.canSee(self.target)) {
				self.saw = true;
				self.noticeSignT = 2f;
				self.playSfx("enemy_alert");
				self.become("alerted");
			}

			/*if (self.target != null) {
				float dx = self.target.x + self.target.w / 2 - self.x - self.w / 2;
				float dy = self.target.y + self.target.h / 2 - self.y - self.h / 2;
				boolean alerted = false;

				float d = 16;

				if (this.direction.x != 0) {
					alerted = (direction.x == -1 ? dx < 0 : dx > 0) && Math.abs(dy) < d;
				} else {
					alerted = (direction.y == -1 ? dy < 0 : dy > 0) && Math.abs(dx) < d;
				}

				if (alerted) {
					if (!saw) {
						this.checkForPlayer();
						self.saw = true;
						self.noticeSignT = 2f;
						self.playSfx("enemy_alert");
					}

					self.become("saw");
				}
			}*/

/*			if (this.wait > 0) {
				this.wait -= dt;
				this.checkForPlayer();

				if (this.wait <= 0) {
					selectDirs();
				}

				if (self.canSee(self.target)) {
					self.become("chase");
				}

				return;
			}

			if (this.delay > 0) {
				this.delay -= dt;
			} else {
				this.selectPoint();
			}

			float f = 12;

			self.acceleration.x = direction.x * f;
			self.acceleration.y = direction.y * f;

			self.lastAcceleration.x = self.acceleration.x * f;
			self.lastAcceleration.y = self.acceleration.y * f;

			if (self.canSee(self.target)) {
				self.become("chase");
			}*/
		}
	}

	private Vector2 lastAcceleration = new Vector2();

	@Override
	public float getWeaponAngle() {
		return (float) (Math.atan2(lastAcceleration.y, lastAcceleration.x) + Math.PI / 2 * (this.flipped ? 1 : -1));
	}

	@Override
	protected void deathEffects() {
		super.deathEffects();
		this.playSfx("death_towelknight");
		deathEffect(killed);
	}

	public float minAttack = 130f;

	public class ChaseState extends KnightState {
		public static final float ATTACK_DISTANCE = 16f;
		public static final float DASH_DIST = 48f;
		public float delay;
		private float att;

		@Override
		public void onEnter() {
			super.onEnter();

			this.delay = Random.newFloat(4f, 5f);

			if (self.sword instanceof Sword) {
				this.att = ATTACK_DISTANCE;
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
				if (this.moveTo(self.lastSeen, 18f, this.att)) {
					if (self.target != null && self.getDistanceTo((int) (self.target.x + self.target.w / 2),
						(int) (self.target.y + self.target.h / 2)) <= this.att) {

						if (self.canSee(self.target)) {
							self.become("preattack");
						}
					} else {
						self.noticeSignT = 0f;
						self.hideSignT = 2f;
						self.become("idle");
					}
				}

				self.lastAcceleration.x = self.acceleration.x;
				self.lastAcceleration.y = self.acceleration.y;

				if (this.t >= this.delay && self.canSee(Player.instance, Terrain.HOLE)) {
					self.become("predash");
					return;
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

			if (this.t > 0.2f) {
				self.become("attack");
			}
		}
	}

	public class PredashState extends KnightState {
		@Override
		public void onEnter() {
			super.onEnter();

			self.playSfx("predash");

			self.velocity.x = 0;
			self.velocity.y = 0;

			Tween.to(new Tween.Task(0.7f, 0.5f) {
				@Override
				public float getValue() {
					return self.sy;
				}

				@Override
				public void setValue(float value) {
					self.sy = value;
				}

				@Override
				public void onEnd() {
					Tween.to(new Tween.Task(1f, 0.2f) {
						@Override
						public void onStart() {
							super.onStart();
							self.playSfx("dash");
							self.become("dash");
						}

						@Override
						public float getValue() {
							return self.sy;
						}

						@Override
						public void setValue(float value) {
							self.sy = value;
						}
					}).delay(0.1f);
				}
			});

			Tween.to(new Tween.Task(1.3f, 0.5f) {
				@Override
				public float getValue() {
					return self.sx;
				}

				@Override
				public void setValue(float value) {
					self.sx = value;
				}

				@Override
				public void onEnd() {
					Tween.to(new Tween.Task(1f, 0.2f) {
						@Override
						public float getValue() {
							return self.sx;
						}

						@Override
						public void setValue(float value) {
							self.sx = value;
						}
					}).delay(0.1f);
				}
			});
		}
	}

	public class DashState extends KnightState {
		private Vector2 vel;

		@Override
		public void onEnter() {
			super.onEnter();

			for (int i = 0; i < 5; i++) {
				PoofFx fx = new PoofFx();

				fx.x = self.x + self.w / 2;
				fx.y = self.y + self.h / 2;

				Dungeon.area.add(fx);
			}

			float dx = self.target.x + 8 - self.x - self.w / 2;
			float dy = self.target.y + 8 - self.y - self.h / 2;
			float d = (float) Math.sqrt(dx * dx + dy * dy);

			this.vel = new Vector2();
			this.vel.x = dx / (d + Random.newFloat(-d / 3, d / 3)) * 350;
			this.vel.y = dy / (d + Random.newFloat(-d / 3, d / 3)) * 350;
		}

		@Override
		public void update(float dt) {
			super.update(dt);

			this.vel.x -= this.vel.x * Math.min(1, dt * 3);
			this.vel.y -= this.vel.y * Math.min(1, dt * 3);

			self.velocity.x = this.vel.x;
			self.velocity.y = this.vel.y;

			if (this.t >= 1f) {
				self.become("wait");
			}
		}
	}

	public class WaitState extends KnightState {
		@Override
		public void update(float dt) {
			super.update(dt);

			if (this.t >= 2f) {
				self.become("chase");
				// self.become(self.getDistanceTo(self.target.x + 8, self.target.y + 8) < 64 ? "chase" : "idle");
			}
		}
	}
}