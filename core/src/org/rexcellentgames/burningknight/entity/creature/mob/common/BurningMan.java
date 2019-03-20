package org.rexcellentgames.burningknight.entity.creature.mob.common;

import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.buff.Buffs;
import org.rexcellentgames.burningknight.entity.creature.buff.BurningBuff;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;
import org.rexcellentgames.burningknight.util.Random;

public class BurningMan extends Mob {
	public static Animation animations = Animation.make("actor-burningman", "-gray");
	private AnimationData run;
	private AnimationData hurt;
	private AnimationData killed;
	private AnimationData animation;

	public Animation getAnimation() {
		return animations;
	}

	{
		hpMax = 6;

		run = getAnimation().get("run");
		hurt = getAnimation().get("hurt");
		killed = getAnimation().get("death");
		animation = run;
	}

	@Override
	public void init() {
		super.init();

		this.body = this.createSimpleBody(4, 3, 8, 9, BodyDef.BodyType.DynamicBody, false);
		World.checkLocked(this.body).setTransform(this.x, this.y, 0);
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
			this.animation = run;
		}

		this.renderWithOutline(this.animation);
		Graphics.batch.setColor(1, 1, 1, 1);
		super.renderStats();
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (!freezed) {
			animation.update(dt);
		}

		if (this.target != null && this.target.room == this.room && !this.hasBuff(Buffs.BURNING)) {
			BurningBuff burningBuff = new BurningBuff();
			burningBuff.infinite = true;

			addBuff(burningBuff);
		}

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
		return new RunningState();
	}

	public class RunningState extends Mob.State<BurningMan> {
		@Override
		public void onEnter() {
			super.onEnter();
			t = Random.newFloat(10);
		}

		@Override
		public void update(float dt) {
			super.update(dt);

			if (self.target == null) {
				return;
			}

			moveTo(self.target, 20f, 8f);
		}
	}
}