package org.rexcellentgames.burningknight.entity.item.weapon.gun;

import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class BackGun extends GunA {
	{
		sprite = "item-back_gun";
		damage = 6;
		setAccuracy(2);
		name = Locale.get("back_gun");
		description = Locale.get("back_gun_desc");
	}

	@Override
	protected void sendBullets() {
		Point aim = this.owner.getAim();
		float a = (float) (this.owner.getAngleTo(aim.x, aim.y) + Math.PI);

		this.sendBullet((float) (a +
			Math.toRadians(Random.newFloat(-this.getAccuracy(), this.getAccuracy())))
		);
	}
}