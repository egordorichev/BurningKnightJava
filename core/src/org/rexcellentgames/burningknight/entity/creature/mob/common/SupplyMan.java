package org.rexcellentgames.burningknight.entity.creature.mob.common;

import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.key.KeyC;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;

import java.util.ArrayList;

public class SupplyMan extends Mob {
	public static Animation animations = Animation.make("actor-supply", "-key");
	private AnimationData run;
	private AnimationData idle;
	private AnimationData hurt;
	private AnimationData killed;
	private AnimationData animation;

	public Animation getAnimation() {
		return animations;
	}

	{
		hpMax = 2;
		w = 10;

		run = getAnimation().get("run");
		idle = getAnimation().get("idle");
		killed = getAnimation().get("dead");
		hurt = getAnimation().get("hurt");
		animation = run;
	}


	@Override
	public void init() {
		super.init();

		this.body = this.createSimpleBody(0, 0, 10, 14, BodyDef.BodyType.DynamicBody, false);
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
		Graphics.batch.setColor(1, 1, 1, 1);
		super.renderStats();
	}

	@Override
	public void renderShadow() {
		Graphics.shadow(x, y + 4, w, h, 0);
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (!freezed) {
			animation.update(dt);
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
	protected ArrayList<Item> getDrops() {
		ArrayList<Item> items = new ArrayList<>();

		items.add(new KeyC());

		return items;
	}

	@Override
	protected State getAi(String state) {
		switch (state) {
			case "idle": case "roam": case "alerted": return new IdleState();
			case "run": return new RunState();
		}

		return super.getAi(state);
	}

	public class SupplyState extends Mob.State<SupplyMan> {

	}

	public class IdleState extends SupplyState {
		@Override
		public void update(float dt) {
			super.update(dt);

			if (self.target != null && self.onScreen && self.canSee(self.target)) {
				self.become("run");
				self.noticeSignT = 2f;
				self.playSfx("enemy_alert");
			}
		}
	}

	public class RunState extends SupplyState {
		@Override
		public void update(float dt) {
			super.update(dt);

			moveFrom(Player.instance, 20f, 512f);

			if (!self.onScreen) {
				self.poof();
				Player.instance.playSfx("head_explode");
				self.done = true;
			}
		}
	}
}