package org.rexcellentgames.burningknight.entity.item.weapon.gun;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.BigBulletProjectile;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class CKGun extends GunA {
	{
		useTime = 0.2f;
	}

	@Override
	protected void sendBullets() {

	}

	public void defaultShot() {
		super.use();
		this.vel = 1f;
		super.sendBullets();
	}

	public void bigShot() {
		super.use();
		Point aim = this.owner.getAim();
		float a = (float) (this.owner.getAngleTo(aim.x, aim.y) - Math.PI * 2);

		this.vel = 1f;
		sendBullet(a, 0, 0, new BigBulletProjectile());
	}

	public void trippleShot() {
		super.use();
		Point aim = this.owner.getAim();
		float a = (float) (this.owner.getAngleTo(aim.x, aim.y) - Math.PI * 2);

		float d = 13f;
		this.vel = 2f;

		sendBullet(a);
		sendBullet(a, (float) Math.cos(a + Math.PI / 2) * d, (float) Math.sin(a + Math.PI / 2) * d);
		sendBullet(a, (float) Math.cos(a - Math.PI / 2) * d, (float) Math.sin(a - Math.PI / 2) * d);
	}
}