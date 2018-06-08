package org.rexellentgames.dungeon.entity.item.weapon.gun;

import org.rexellentgames.dungeon.assets.Locale;
import org.rexellentgames.dungeon.util.Random;
import org.rexellentgames.dungeon.util.geometry.Point;

public class BackGun extends GunA {
	{
		sprite = "item-back_gun";
		damage = 6;
		accuracy = 2;
		name = Locale.get("back_gun");
		description = Locale.get("back_gun_desc");
	}

	@Override
	protected void sendBullets() {
		Point aim = this.owner.getAim();
		float a = (float) (this.owner.getAngleTo(aim.x, aim.y) + Math.PI);

		this.sendBullet((float) (a +
			Math.toRadians(Random.newFloat(-this.accuracy, this.accuracy)))
		);
	}
}