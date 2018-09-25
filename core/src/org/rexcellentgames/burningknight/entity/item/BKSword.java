package org.rexcellentgames.burningknight.entity.item;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.buff.BurningBuff;
import org.rexcellentgames.burningknight.entity.fx.BKSFx;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.Gun;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.SlashSword;
import org.rexcellentgames.burningknight.game.Achievements;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class BKSword extends SlashSword {
	{
		name = Locale.get("bk_sword");
		description = Locale.get("bk_sword_desc");
		sprite = "item-bk_sword";
		damage = 15;
		penetrates = true;
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (this.delay > 0) {
			last += Gdx.graphics.getDeltaTime();
		} else {
			last = 0;
		}

		if (last >= 0.01f) {
			last = 0;

			float angle = added;

			if (this.owner != null) {
				Point aim = this.owner.getAim();

				float an = (float) (this.owner.getAngleTo(aim.x, aim.y) - Math.PI);
				an = Gun.angleLerp(this.lastAngle, an, 0.15f, this.owner != null && this.owner.freezed);
				this.lastAngle = an;
				float a = (float) Math.toDegrees(this.lastAngle);

				angle += (this.owner.isFlipped() ? a : -a);

				angle = this.owner.isFlipped() ? angle : 180 - angle;
			}

			TextureRegion sprite = this.getSprite();

			float xx = x + w / 2 + (this.owner.isFlipped() ? 0 : w / 4) + move;
			float yy = y + h / 4 + moveY;

			BKSFx fx = new BKSFx();

			fx.depth = this.owner.depth - 1;
			fx.x = xx - (this.owner.isFlipped() ? sprite.getRegionWidth() / 2 : 0);
			fx.y = yy;
			fx.a = angle;
			fx.region = sprite;
			fx.ox = sprite.getRegionWidth() / 2 + this.ox;
			fx.oy = oy;
			fx.flipped = this.owner.isFlipped();
		}
	}

	private float last;

	@Override
	public void onHit(Creature creature) {
		super.onHit(creature);

		creature.addBuff(new BurningBuff());
	}

	@Override
	public void onPickup() {
		super.onPickup();
		Achievements.unlock(Achievements.UNLOCK_SWORD_ORBITAL);
	}
}