package org.rexcellentgames.burningknight.entity.creature.mob.tech;

import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class Repair extends Bot {
	public static Animation animations = Animation.make("actor-repair", "-normal");
	private AnimationData idle;
	private AnimationData killed;
	private AnimationData hurt;
	private AnimationData move;
	private AnimationData animation;

	public Animation getAnimation() {
		return animations;
	}

	{
		hpMax = 16;

		idle = getAnimation().get("idle");
		hurt = getAnimation().get("hurt");
		killed = getAnimation().get("dead");
		move = getAnimation().get("move");
		animation = idle;
	}

	@Override
	public void init() {
		super.init();

		w = 9;
		this.body = this.createSimpleBody(1, 4, 6, 10, BodyDef.BodyType.DynamicBody, false);
		World.checkLocked(this.body).setTransform(this.x, this.y, 0);
	}

	@Override
	public void destroy() {
		super.destroy();
		this.body = World.removeBody(this.body);
	}

	@Override
	public void render() {
		if (this.dead) {
			this.animation = killed;
		} else if (this.invt > 0) {
			this.animation = hurt;
		} else if (this.state.equals("move")) {
			this.animation = move;
		} else {
			this.animation = idle;
		}

		this.renderWithOutline(this.animation);
		Graphics.batch.setColor(1, 1, 1, 1);
		// Graphics.print(this.state, Graphics.small, this.x, this.y);
		super.renderStats();

		if (targetDead != null) {
			Graphics.startShape();
			Graphics.shape.setColor(1, 0, 0, 1);
			Graphics.shape.circle((float) Math.floor(targetDead.x / 16) * 16, (float) Math.floor(targetDead.y) / 16 * 16, 4);
			Graphics.endShape();
			Graphics.print(targetDead.type.getSimpleName(), Graphics.small, targetDead.x, targetDead.y);
		}
	}

	@Override
	public void renderShadow() {
		Graphics.shadow(x, y, 8, h, 0);
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		w = 16;
		h = 16;
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
			case "idle": case "roam": case "alerted": return new IdleState();
			case "move": return new MoveState();
			case "fix": return new FixState();
		}

		return super.getAi(state);
	}

	public class RepairState extends Mob.State<Repair> {

	}

	public class IdleState extends RepairState {
		@Override
		public void update(float dt) {
			super.update(dt);

			if (Player.instance.room == self.room && Bot.data.size() > 0) {
				self.become("move");
			}
		}
	}

	@Override
	public float getOx() {
		return 4;
	}

	private DeathData targetDead;

	public class MoveState extends RepairState {
		@Override
		public void onEnter() {
			super.onEnter();
			self.targetDead = Bot.data.get(0);
		}

		@Override
		public void update(float dt) {
			super.update(dt);

			Point tar = new Point(self.targetDead.x, self.targetDead.y);

			if (self.canSeePoint(tar)) {
				nextPathPoint = tar;
			}

			if (moveTo(tar, 20f, 8f)) {
				self.become("fix");
			}
		}
	}

	public class FixState extends RepairState {
		@Override
		public void update(float dt) {
			super.update(dt);

			if (t >= 3f) {
				self.become("idle");
			}
		}

		@Override
		public void onExit() {
			super.onExit();

			try {
				Mob mob = self.targetDead.type.newInstance();
				mob.x = self.targetDead.x;
				mob.y = self.targetDead.y;
				mob.noLoot = true;
				Dungeon.area.add(mob.add());
				mob.poof();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}


			Bot.data.remove(self.targetDead);
			self.targetDead = null;
		}
	}
}