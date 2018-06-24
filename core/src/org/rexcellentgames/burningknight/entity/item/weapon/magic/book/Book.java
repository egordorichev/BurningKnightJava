package org.rexcellentgames.burningknight.entity.item.weapon.magic.book;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.Gun;
import org.rexcellentgames.burningknight.entity.item.weapon.magic.Wand;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class Book extends Wand {
	@Override
	public void render(float x, float y, float w, float h, boolean flipped) {
		float angle = 0;

		if (this.owner != null) {
			Point aim = this.owner.getAim();

			float an = (float) (this.owner.getAngleTo(aim.x, aim.y) - Math.PI);
			an = Gun.angleLerp(this.lastAngle, an, 0.15f);
			this.lastAngle = an;
			float a = (float) Math.toDegrees(this.lastAngle);

			angle += (flipped ? a : -a);
			angle = flipped ? angle : 180 - angle;
		}

		TextureRegion sprite = this.getSprite();

		float xx = x + w / 2 + (flipped ? 0 : w / 4);
		float yy = y + h / 4;

		this.renderAt(xx - (flipped ? sprite.getRegionWidth() / 2 : 0), yy,
			angle, 0, 0, false, false, flipped ? -1 : 1, 1);
	}

	@Override
	protected void sendProjectiles() {
		float angle = 0;

		if (this.owner != null) {
			Point aim = this.owner.getAim();

			float an = (float) (this.owner.getAngleTo(aim.x, aim.y) - Math.PI);
			an = Gun.angleLerp(this.lastAngle, an, 0.15f);
			this.lastAngle = an;
			float a = (float) Math.toDegrees(this.lastAngle);

			angle += (this.owner.isFlipped() ? a : -a);
			angle = this.owner.isFlipped() ? angle : 180 - angle;

			if (this.owner.isFlipped()) {
				angle -= 180;
			}
		}

		// todo: fix

		float xx = this.owner.x + this.owner.w / 2 +
			(this.owner.isFlipped() ? 0 : this.owner.w / 4);

		float yy = this.owner.y + this.owner.h / 2;

		this.spawnProjectile(xx, yy, angle);
	}
}