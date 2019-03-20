package org.rexcellentgames.burningknight.entity.creature.buff;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.buff.fx.FlameFx;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.mob.common.BurningMan;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.game.Achievements;

public class BurningBuff extends Buff {
	public static TextureRegion frame = Graphics.getTexture("ui-debuff_frame");
	public static TextureRegion flame = Graphics.getTexture("ui-debuff_fire");

	{
		id = Buffs.BURNING;
		name = "Burning";
	}

	private float lastFlame = 0;
	private float progress;
	private float rate;

	@Override
	public Buff setDuration(float duration) {
		rate = 1 / (duration - 1);
		return super.setDuration(duration);
	}

	@Override
	public void onStart() {
		super.onStart();
		setDuration(7.0f);
		this.owner.removeBuff(Buffs.FROZEN);
	}

	@Override
	protected void onUpdate(float dt) {
		boolean mob = this.owner instanceof Mob;

		if (!this.did) {
			progress += mob ? dt * 2 : dt * rate;
		}

		Dungeon.level.addLightInRadius(this.owner.x + this.owner.w / 2, this.owner.y + this.owner.h / 2, 0.9f, 3f, false);
		this.lastFlame += dt;

		if (this.lastFlame >= 0.1f) {
			Dungeon.area.add(new FlameFx(this.owner));
			this.lastFlame = 0;
		}

		if (progress >= 1f) {
			if (!(this.owner instanceof BurningMan)) {
				this.owner.modifyHp(this.owner instanceof Player ? -1 : -2, null, true);
			}

			if (!mob) {
				did = true;
				// setDuration(1f);

				if (this.owner instanceof Player && this.owner.isDead()) {
					Achievements.unlock(Achievements.BURN_TO_DEATH);
				}

				this.progress = 1f;
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
		float x = creature.x + creature.w / 2 - frame.getRegionWidth() / 2;
		float y = creature.y + creature.h + 4;

		if (!(this.owner instanceof Mob)) {
			Graphics.batch.setColor(1, 1, 1, this.al);
			Graphics.render(frame, x, y);

			if (this.progress < 1f) {
				Graphics.batch.setColor(0.2f, 0.2f, 0.2f, this.al);
				Graphics.render(flame, x + 1, y + 1, 0, 0, 0, false, false, 1, 5);
			}

			Graphics.batch.setColor(1, 1, 1, this.al);
			Graphics.render(flame, x + 1, y + 1, 0, 0, 0, false, false, 1, (float) Math.floor(5 * progress));
			Graphics.batch.setColor(1, 1, 1, 1);
		}
	}
}