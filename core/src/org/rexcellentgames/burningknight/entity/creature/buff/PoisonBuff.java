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
		rate = 1 / (duration - 1f);
		return super.setDuration(duration);
	}

	private float progress;

	@Override
	public void onStart() {
		super.onStart();
		setDuration(11);

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

		if (!did) {
			progress += mob ? dt * 2 : dt * rate;
		}

		if (this.progress >= 1) {
			this.owner.modifyHp(this.owner instanceof Player ? -1 : -2, null, true);

			if (this.owner instanceof Player) {
				this.progress = 1f;
				this.did = true;
			} else {
				this.progress = 0;
			}
		}

		this.al += ((did ? 0 : 1) - al) * dt * 5;

		if (this.did && this.al <= 0.05f) {
			this.setDuration(0);
		}
	}

	private boolean did;
	private float al;

	@Override
	public void render(Creature creature) {
		float x = creature.x + creature.w / 2 - BurningBuff.frame.getRegionWidth() / 2;
		float y = creature.y + creature.h + 4;

		if (!(this.owner instanceof Mob)) {
			Graphics.batch.setColor(1, 1, 1, this.al);
			Graphics.render(BurningBuff.frame, x, y);

			if (this.progress < 1f) {
				Graphics.batch.setColor(0.2f, 0.2f, 0.2f, this.al);
				Graphics.render(poison, x + 1, y + 1, 0, 0, 0, false, false, 1, 5);
			}

			Graphics.batch.setColor(1, 1, 1, this.al);
			Graphics.render(poison, x + 1, y + 1, 0, 0, 0, false, false, 1, 5 * progress);
			Graphics.batch.setColor(1, 1, 1, 1);
		}
	}
}