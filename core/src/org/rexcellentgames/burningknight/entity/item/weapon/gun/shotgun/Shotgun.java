package org.rexcellentgames.burningknight.entity.item.weapon.gun.shotgun;

import org.rexcellentgames.burningknight.entity.item.weapon.gun.Gun;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class Shotgun extends Gun {
	{
		accuracy = 20;
		useTime = 3f;
		damage = 3;
	}

	@Override
	protected void sendBullets() {
		Point aim = this.owner.getAim();
		float an = this.owner.getAngleTo(aim.x, aim.y);

		for (int i = 0; i < 3; i++) {
			float a = (float) (an - Math.PI * 2);
			this.sendBullet((float) (a + Math.toRadians(Random.newFloat(-this.accuracy, this.accuracy))));
		}
	}
}