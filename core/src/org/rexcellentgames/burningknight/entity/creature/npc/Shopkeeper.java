package org.rexcellentgames.burningknight.entity.creature.npc;

import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.Settings;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.creature.fx.BloodFx;
import org.rexcellentgames.burningknight.entity.creature.fx.GoreFx;
import org.rexcellentgames.burningknight.entity.level.save.LevelSave;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;

public class Shopkeeper extends Npc {
	private static Animation animations = Animation.make("actor-trader", "-green");
	private AnimationData idle = animations.get("idle");
	private AnimationData run = animations.get("run");
	private AnimationData hurt = animations.get("hurt");
	private AnimationData death = animations.get("death");
	private AnimationData animation = idle;

	@Override
	public void init() {
		super.init();
		this.body = World.createSimpleBody(this, 4, 0, 8, 14, BodyDef.BodyType.DynamicBody, false);
		this.body.setTransform(this.x, this.y, 0);
	}

	@Override
	public void destroy() {
		super.destroy();
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (this.invt > 0) {
			this.animation = hurt;
		} else if (this.vel.len2() > 9f) {
			this.animation = run;
		} else {
			this.animation = idle;
		}

		animation.update(dt);
	}

	@Override
	protected void die(boolean force) {
		super.die(force);
		this.playSfx("death_towelknight");

		this.done = true;
		LevelSave.remove(this);

		if (Settings.gore) {
			for (Animation.Frame frame : death.getFrames()) {
				GoreFx fx = new GoreFx();

				fx.texture = frame.frame;
				fx.x = this.x + this.w / 2;
				fx.y = this.y + this.h / 2;

				Dungeon.area.add(fx);
			}
		}

		BloodFx.add(this, 20);
	}

	@Override
	public void render() {
		animation.render(this.x, this.y, this.flipped);
	}

	@Override
	public void renderShadow() {
		Graphics.shadow(this.x + 2, this.y, this.w - 4, this.h);
	}
}