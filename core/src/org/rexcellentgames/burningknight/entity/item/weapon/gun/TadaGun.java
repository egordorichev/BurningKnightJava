package org.rexcellentgames.burningknight.entity.item.weapon.gun;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.fx.Confetti;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class TadaGun extends Gun {
	{
		origin.x = 4;
		origin.y = 1;
		hole.x = 4;
		hole.y = 8;
		setAccuracy(20);
	}

	@Override
	protected void sendBullets() {
		Point aim = this.owner.getAim();
		float a = (float) (this.owner.getAngleTo(aim.x, aim.y) + (this.owner.isFlipped() ? -Math.PI / 2 : Math.PI / 2));

		for (int i = 0; i < 10; i++) {
			float an = (float) (a + Math.toRadians(Random.newFloat(-this.getAccuracy(), this.getAccuracy())));

			Confetti fx = new Confetti();

			float x = this.owner.x + this.owner.w / 2;
			float y = this.owner.y + this.owner.h / 4 + region.getRegionHeight() / 2 - 2;

			fx.x = x + this.getAimX(0, 0);
			fx.y = y + this.getAimY(0, 0);

			float f = Random.newFloat(40, 80f);

			fx.vel.x = (float) Math.cos(an) * f;
			fx.vel.y = (float) Math.sin(an) * f;

			Dungeon.area.add(fx);
		}
	}
}