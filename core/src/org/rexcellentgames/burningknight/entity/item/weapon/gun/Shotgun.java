package org.rexcellentgames.burningknight.entity.item.weapon.gun;

import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class Shotgun extends Gun {
	{
		accuracy = 20f;
		useTime = 3f;
		damage = 3;
		sprite = "item-gun_a";
		vel = 3f;
	}

	@Override
	public void sendBullets() {
		Point aim = this.owner.getAim();
		float an = this.owner.getAngleTo(aim.x, aim.y);
		float a = (float) (an - Math.PI * 2);

		for (int i = 0; i < 4; i++) {
			this.sendBullet((float) (a + Math.toRadians(Random.newFloat(-this.accuracy, this.accuracy))));
		}
	}

	@Override
	protected String getSfx() {
		return "gun_3";
	}
}