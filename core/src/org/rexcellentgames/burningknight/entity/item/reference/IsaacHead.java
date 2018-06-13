package org.rexcellentgames.burningknight.entity.item.reference;

import org.rexcellentgames.burningknight.entity.item.weapon.gun.Gun;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.Gun;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.bullet.Tear;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class IsaacHead extends Gun {
	{
		sprite = "item (isaac_head)";
		ammo = Tear.class;
		textureA = 90;
		ox = 9;
		vel = 2f;
	}

	@Override
	public void init() {
		super.init();

		this.tw = this.getSprite().getRegionHeight() / 2;
		this.th = this.getSprite().getRegionWidth();
	}

	@Override
	protected void sendBullets() {
		Point aim = this.owner.getAim();
		float a = (float) (this.owner.getAngleTo(aim.x, aim.y) - Math.PI * 2);

		sendBullet(a, (float) Math.cos(a + Math.PI / 2) * 2, (float) Math.sin(a + Math.PI / 2) * 2);
		sendBullet(a, (float) Math.cos(a - Math.PI / 2) * 7, (float) Math.sin(a - Math.PI / 2) * 7);
	}
}