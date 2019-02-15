package org.rexcellentgames.burningknight.entity.creature.mob.ice;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;
import org.rexcellentgames.burningknight.util.Random;

public class Snowball extends Mob {
	public static Animation animations = Animation.make("actor-snowball", "-white");
	private AnimationData idle;
	private AnimationData killed;
	private AnimationData hurt;
	private AnimationData animation;

	public Animation getAnimation() {
		return animations;
	}

	{
		hpMax = 16;

		idle = getAnimation().get("idle");
		hurt = getAnimation().get("hurt");
		killed = getAnimation().get("dead");
		animation = idle;
		w = 14;
	}

	@Override
	public void init() {
		super.init();

		idle.randomize();
		this.body = this.createSimpleBody(2, 0, 10, 10, BodyDef.BodyType.DynamicBody, false);
		World.checkLocked(this.body).setTransform(this.x, this.y, 0);
	}

	@Override
	public void renderShadow() {
		Graphics.shadow(x, y + 3, w, h, 6);
	}

	@Override
	public void destroy() {
		super.destroy();
		this.body = World.removeBody(this.body);
	}

	@Override
	public void render() {
		if (Math.abs(this.velocity.x) > 1f) {
			this.flipped = this.velocity.x < 0;
		}

		if (this.dead) {
			this.animation = killed;
		} else if (this.invt > 0) {
			this.animation = hurt;
		} else {
			this.animation = idle;
		}

		this.renderWithOutline(this.animation);
		Graphics.batch.setColor(1, 1, 1, 1);
		super.renderStats();
	}

	@Override
	public float getWeight() {
		return 0.2f;
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
	protected Mob.State getAi(String state) {
		switch (state) {
			case "idle": case "roam": case "alerted": return new WaitState();
			case "attack": return new AttackState();
		}

		return super.getAi(state);
	}

	public class WaitState extends Mob.State<Snowball> {
		private float delay;

		@Override
		public void onEnter() {
			super.onEnter();

			delay = Random.newFloat(0.1f, 2f);
		}

		@Override
		public void update(float dt) {
			super.update(dt);

			if (t >= delay) {
				self.become("attack");
			}
		}
	}

	public class AttackState extends Mob.State<Snowball> {
		private Vector2 dir;
		private float delay;

		@Override
		public void onEnter() {
			super.onEnter();

			delay = Random.newFloat(0.4f, 1.5f);
			float angle;

			if (Random.chance(40) || self.target == null || self.target.room != self.room) {
				angle = Random.newFloat((float) (Math.PI * 2));
			} else {
				angle = self.getAngleTo(self.target.x + 8, self.target.y + 8);
			}

			float f = Random.newFloat(90f, 150f);

			dir = new Vector2((float) Math.cos(angle) * f, (float) Math.sin(angle) * f);
		}

		@Override
		public void update(float dt) {
			super.update(dt);

			self.velocity.x += dir.x * dt * 10;
			self.velocity.y += dir.y * dt * 10;

			if (t >= delay) {
				self.become("idle");
			}
		}
	}
}