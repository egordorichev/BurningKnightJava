package org.rexellentgames.dungeon.entity.item.weapon.gun;

import org.rexellentgames.dungeon.assets.Locale;
import org.rexellentgames.dungeon.game.input.Input;
import org.rexellentgames.dungeon.util.Random;

public class GunB extends Gun {
	{
		name = Locale.get("gun_b");
		description = Locale.get("gun_b_desc");
		sprite = "item (gun B)";
	}

	@Override
	protected void sendBullets() {
		float a = (float) (this.owner.getAngleTo(Input.instance.worldMouse.x, Input.instance.worldMouse.y) - Math.PI * 2);

		this.sendBullet((float) (a + Math.toRadians(Random.newFloat(-this.accuracy, this.accuracy)) + Math.toRadians(10f)));
		this.sendBullet((float) (a + Math.toRadians(Random.newFloat(-this.accuracy, this.accuracy)) - Math.toRadians(10f)));
		this.sendBullet((float) (a + Math.toRadians(Random.newFloat(-this.accuracy, this.accuracy))));
	}
}