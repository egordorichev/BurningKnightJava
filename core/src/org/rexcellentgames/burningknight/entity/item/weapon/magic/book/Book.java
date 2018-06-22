package org.rexcellentgames.burningknight.entity.item.weapon.magic.book;

import org.rexcellentgames.burningknight.entity.item.weapon.gun.Gun;
import org.rexcellentgames.burningknight.entity.item.weapon.magic.Wand;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class Book extends Wand {
	@Override
	public void render(float x, float y, float w, float h, boolean flipped) {
		if (this.owner != null) {
			Point aim = this.owner.getAim();

			float an = (float) (this.owner.getAngleTo(aim.x, aim.y) - Math.PI / 2);
			an = Gun.angleLerp(this.lastAngle, an, 0.15f);
			this.lastAngle = an;
		}

		this.renderAt(x + w / 2, y + h / 4, (float) Math.toDegrees(this.lastAngle) + 45, 0, 0, false, false);
	}
}