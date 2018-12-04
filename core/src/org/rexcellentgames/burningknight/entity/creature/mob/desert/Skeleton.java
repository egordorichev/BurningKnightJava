package org.rexcellentgames.burningknight.entity.creature.mob.desert;

import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.level.entities.fx.PoofFx;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

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
	public void initStats() {
		super.initStats();
		setStat("knockback", 0);
	}

	@Override
	public void init() {
		super.init();

		this.body = this.createSimpleBody(2, 1, 12, 12, BodyDef.BodyType.DynamicBody, false);
		World.checkLocked(this.body).setTransform(this.x, this.y, 0);
	}

	@Override
	protected void onHurt(int a, Entity from) {
		super.onHurt(a, from);

		if (this.state.equals("dead")) {
			this.remove = true;
			this.dead = true;
			this.done = true;
			this.rem = true;

			for (int i = 0; i < 10; i++) {
				PoofFx fx = new PoofFx();

				fx.x = this.x + this.w / 2;
				fx.y = this.y + this.h / 2;

				Dungeon.area.add(fx);
			}
		}
	}

	private boolean rem;

	@Override
	protected void deathEffects() {
		if (this.rem) {
			return;
		}
		super.deathEffects();

		this.hp = this.hpMax;
		this.done = false;
		this.become("dead");

		if (this.prefix != null) {
			this.prefix.onDeath(this);
		}
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
		super.renderStats();
	}

	public class SkeletonState extends Mob.State<Skeleton> {

	}

	@Override
	protected State getAi(String state) {
		switch (state) {

		}

		return super.getAi(state);
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

	public static Skeleton random() {
		float r = Random.newFloat(1);

		if (r < 0.5f) {
			return new Skeleton();
		} else if (r < 0.8f) {
			return new BlackSkeleton();
		} else {
			return new BrownSkeleton();
		}
	}
}