package org.rexcellentgames.burningknight.entity.creature.mob.desert;

import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.Bone;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

import java.util.ArrayList;

public class Skeleton extends Mob {
	public static Animation animations = Animation.make("actor-skeleton", "-white");
	private AnimationData idle;
	private AnimationData run;
	private AnimationData hurt;
	private AnimationData killed;
	private AnimationData revive;
	private AnimationData animation;
	public float distance = 48;

	public Animation getAnimation() {
		return animations;
	}

	{
		hpMax = 3;
		w = 13;

		idle = getAnimation().get("idle").randomize();
		run = getAnimation().get("run").randomize();
		hurt = getAnimation().get("hurt").randomize();
		killed = getAnimation().get("death");
		killed.setAutoPause(true);

		revive = getAnimation().get("revive");
		revive.setListener(new AnimationData.Listener() {
			@Override
			public void onEnd() {
				become("idle");
			}
		});

		animation = this.idle;
	}

	@Override
	public void init() {
		super.init();

		this.body = this.createSimpleBody(2, 1, 12, 12, BodyDef.BodyType.DynamicBody, false);
		World.checkLocked(this.body).setTransform(this.x, this.y, 0);

		this.weapon = new Bone();
		this.weapon.setOwner(this);
	}

	@Override
	public void render() {
		if (Math.abs(this.velocity.x) > 1f) {
			this.flipped = this.velocity.x < 0;
		}

		float v = Math.abs(this.acceleration.x) + Math.abs(this.acceleration.y);

		if (this.state.equals("revive")) {
			this.animation = revive;
		} else if (this.state.equals("dead") || this.state.equals("kindadead")) {
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
		this.weapon.render(this.x, this.y, this.w, this.h, this.flipped, false);

		super.renderStats();
	}

	public class SkeletonState extends Mob.State<Skeleton> {

	}

	@Override
	protected ArrayList<Item> getDrops() {
		ArrayList<Item> items = super.getDrops();

		if (Random.chance(5)) {
			items.add(new Bone());
		}

		return items;
	}

	@Override
	protected State getAi(String state) {
		switch (state) {
			case "idle": case "roam": return new IdleState();
			case "alerted": return new AlertedState();
			case "chase": return new ChaseState();
			case "attack": return new AttackState();
		}

		return super.getAi(state);
	}

	private boolean justAttacked;
	private Item weapon;

	public class AttackState extends SkeletonState {
		@Override
		public void onEnter() {
			super.onEnter();
			self.weapon.use();
		}

		@Override
		public void update(float dt) {

			super.update(dt);
			if (t >= 1f) {
				justAttacked = true;
				self.become("chase");
			}
		}
	}

	public class IdleState extends SkeletonState {
		@Override
		public void update(float dt) {
			super.update(dt);
			checkForPlayer();
		}
	}

	public class AlertedState extends SkeletonState {
		@Override
		public void update(float dt) {
			super.update(dt);

			if (this.t >= 0.75f) {
				self.become("chase");
			}
		}
	}


	@Override
	protected void deathEffects() {
		super.deathEffects();
		this.playSfx("death_clown");

		this.done = true;
		deathEffect(killed);
	}

	public class ChaseState extends SkeletonState {
		private Point to;
		private boolean canSee;

		@Override
		public void update(float dt) {
			super.update(dt);

			if (this.t < 0.6f) {
				if (to == null) {
					canSee = self.canSee(Player.instance, Terrain.HOLE);
					float d = self.getDistanceTo(Player.instance.x, Player.instance.y);

					if (d < 24f && !justAttacked) {
						self.become("attack");
						return;
					}

					justAttacked = false;

					if (canSee) {
						float f = 120f;
						float a;

						if (d < 64) {
							if (Random.chance(50)) {
								f = 50f;
								a = self.getAngleTo(Player.instance.x, Player.instance.y);
							} else {
								a = Random.newFloat((float) (Math.PI * 2));
							}
						} else {
							a = self.getAngleTo(Player.instance.x, Player.instance.y) + Random.newFloat(-1f, 1f);
						}

						to = new Point((float) Math.cos(a) * f, (float) Math.sin(a) * f);
					} else {
						to = new Point(Player.instance.x, Player.instance.y);
					}
				}


				if (canSee) {
					if (t < 0.4f) {
						self.acceleration.x = to.x;
						self.acceleration.y = to.y;
					}
				} else {
					if (moveTo(to, 80f, 32f) || self.canSee(Player.instance)) {
						to = null;
					}
				}
			} else {
				self.velocity.mul(0);
				to = null;

				if (this.t >= 0.61f) {
					this.t = 0;
				}
			}
		}
	}

	public void mod(Point vel, Point ivel, float a, float d, float time) {
		float v = (float) Math.cos(time * 2f);

		vel.x = ivel.x * v;
		vel.y = ivel.y * v;
	}

	public int side;
	public boolean eight;
	public float boneSpeed = 120f;

	@Override
	public void update(float dt) {
		if (this.animation != null) {
			this.animation.update(dt * speedMod);
		}

		this.weapon.update(dt * speedMod);

		super.update(dt);

		if (this.freezed) {
			return;
		}

		if (this.dead) {
			super.common();
			return;
		}

		super.common();
	}

	@Override
	public void destroy() {
		super.destroy();
		weapon.destroy();
	}

	public static Skeleton random() {
		return new Skeleton();
	}
}