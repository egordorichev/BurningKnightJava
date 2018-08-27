package org.rexcellentgames.burningknight.entity.creature.buff;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.player.Player;

public class PoisonBuff extends Buff {
	public static TextureRegion poison = Graphics.getTexture("ui-debuff_poison");

	{
		name = "Poisoned";
		description = "You are slowly losing your life";
		sprite = "ui-poisoned";
		bad = true;
	}

	private float rate;

	@Override
	public Buff setDuration(float duration) {
		rate = 1 / duration;
		return super.setDuration(duration);
	}

	private float progress;

	@Override
	public void onStart() {
		super.onStart();
		setDuration(10);

		this.owner.poisoned = true;
	}

	@Override
	public void onEnd() {
		super.onEnd();
		this.owner.poisoned = false;
	}

	@Override
	public void update(float dt) {
		super.update(dt);
		boolean mob = this.owner instanceof Mob;
		progress += mob ? dt * 2 : dt * rate;

		if (this.progress >= 1) {
			this.progress = 0;
			this.owner.modifyHp(this.owner instanceof Player ? -1 : -2, null, true);
		}
	}

	@Override
	public void render(Creature creature) {
		float x = creature.x + creature.w / 2 - BurningBuff.frame.getRegionWidth() / 2;
		float y = creature.y + creature.h + 4;

		Graphics.render(BurningBuff.frame, x, y);
		Graphics.render(poison, x + 1, y + 1, 0, 0, 0, false, false, 1, 5 * progress);
	}
}