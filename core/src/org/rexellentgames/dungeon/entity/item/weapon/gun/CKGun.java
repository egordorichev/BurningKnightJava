package org.rexellentgames.dungeon.entity.item.weapon.gun;

import org.rexellentgames.dungeon.entity.item.weapon.gun.bullet.BigBulletEntity;
import org.rexellentgames.dungeon.util.geometry.Point;

public class CKGun extends GunA {
	{
		useTime = 0.2f;
		vel = 1f;
	}

	@Override
	protected void sendBullets() {
		bigShot();
	}

	protected void defaultShot() {
		super.sendBullets();
	}

	protected void bigShot() {
		Point aim = this.owner.getAim();
		float a = (float) (this.owner.getAngleTo(aim.x, aim.y) - Math.PI * 2);

		sendBullet(a, 0, 0, new BigBulletEntity());
	}

	protected void trippleShot() {
		Point aim = this.owner.getAim();
		float a = (float) (this.owner.getAngleTo(aim.x, aim.y) - Math.PI * 2);

		float d = 10f;

		sendBullet(a);
		sendBullet(a, (float) Math.cos(a + Math.PI / 2) * d, (float) Math.sin(a + Math.PI / 2) * d);
		sendBullet(a, (float) Math.cos(a - Math.PI / 2) * d, (float) Math.sin(a - Math.PI / 2) * d);
	}
}