package org.rexcellentgames.burningknight.entity.item.weapon.gun;

import org.rexcellentgames.burningknight.game.input.Input;

public class PuncherGun extends Gun {
	{
		accuracy = 3f;
		auto = true;
		ammoMax = 18;
		reloadRate = 1.2f;
		useTime = 0.14f;
		damage = 1;
	}

	@Override
	protected void sendBullets() {
		float a = (float) (this.owner.getAngleTo(Input.instance.worldMouse.x, Input.instance.worldMouse.y) - Math.PI * 2);

		this.sendBullet(a, 0, -10);
		this.sendBullet(a, 0, 10);
		this.sendBullet(a);
	}
}