package org.rexcellentgames.burningknight.entity.item.weapon.gun;

import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.game.input.Input;

public class GunC extends Gun {
	{
		name = Locale.get("gun_c");
		description = Locale.get("gun_c_desc");
		sprite = "item-gun_c";
		useTime = 0.15f;
	}

	@Override
	protected void sendBullets() {
		float a = (float) (this.owner.getAngleTo(Input.instance.worldMouse.x, Input.instance.worldMouse.y) - Math.PI * 2);

		this.sendBullet((float) (a + Math.toRadians(10f)));
		this.sendBullet((float) (a - Math.toRadians(10f)));
		this.sendBullet(a);
	}
}