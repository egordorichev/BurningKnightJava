package org.rexcellentgames.burningknight.entity.creature.buff;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.buff.fx.FlameFx;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.game.Achievements;

public class BurningBuff extends Buff {
	public static TextureRegion frame = Graphics.getTexture("ui-debuff_frame");
	public static TextureRegion flame = Graphics.getTexture("ui-debuff_fire");

	{
		name = "Burning";
		description = "You are on fire!";
		sprite = "ui-burning";
		bad = true;
	}

	private float lastFlame = 0;
	private float progress;
	private float rate;

	@Override
	public Buff setDuration(float duration) {
		rate = 1 / duration;
		return super.setDuration(duration);
	}

	@Override
	public void onStart() {
		super.onStart();
		setDuration(6.0f);
		this.owner.removeBuff(FreezeBuff.class);
	}

	@Override
	protected void onUpdate(float dt) {
		boolean mob = this.owner instanceof Mob;
		progress += mob ? dt * 2 : dt * rate;

		Dungeon.level.addLightInRadius(this.owner.x + this.owner.w / 2, this.owner.y + this.owner.h / 2, 1f, 0.9f, 0f, 0.9f, 3f, false);
		this.lastFlame += dt;

		if (this.lastFlame >= 0.1f) {
			Dungeon.area.add(new FlameFx(this.owner));
			this.lastFlame = 0;
		}

		if (progress >= 1f) {
			this.owner.modifyHp(this.owner instanceof Player ? -1 : -4, null, true);

			if (!mob) {
				setDuration(0);

				if (this.owner instanceof Player && this.owner.isDead()) {
					Achievements.unlock(Achievements.BURN_TO_DEATH);
				}
			}
		}
	}

	@Override
	public void render(Creature creature) {
		float x = creature.x + creature.w / 2 - frame.getRegionWidth() / 2;
		float y = creature.y + creature.h + 4;

		if (!(this.owner instanceof Mob)) {
			Graphics.render(frame, x, y);
			Graphics.render(flame, x + 1, y + 1, 0, 0, 0, false, false, 1, 5 * progress);
		}
	}
}