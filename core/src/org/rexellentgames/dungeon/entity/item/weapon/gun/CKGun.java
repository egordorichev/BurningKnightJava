package org.rexellentgames.dungeon.entity.item.weapon.gun;
import org.rexellentgames.dungeon.entity.item.weapon.gun.bullet.BigBulletEntity;
import org.rexellentgames.dungeon.util.geometry.Point;

public class CKGun extends GunA {
	{
		useTime = 0.2f;
	}

	@Override
	protected void sendBullets() {
		bigShot();
	}

	public void defaultShot() {
		this.vel = 1f;
		super.sendBullets();
	}

	public void bigShot() {
		Point aim = this.owner.getAim();
		float a = (float) (this.owner.getAngleTo(aim.x, aim.y) - Math.PI * 2);

		this.vel = 1f;
		sendBullet(a, 0, 0, new BigBulletEntity());
	}

	public void trippleShot() {
		Point aim = this.owner.getAim();
		float a = (float) (this.owner.getAngleTo(aim.x, aim.y) - Math.PI * 2);

		float d = 13f;
		this.vel = 2f;

		sendBullet(a);
		sendBullet(a, (float) Math.cos(a + Math.PI / 2) * d, (float) Math.sin(a + Math.PI / 2) * d);
		sendBullet(a, (float) Math.cos(a - Math.PI / 2) * d, (float) Math.sin(a - Math.PI / 2) * d);
	}
}