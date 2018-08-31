package org.rexcellentgames.burningknight.entity.item.weapon.rocketlauncher;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.Gun;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.BulletProjectile;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.RocketProjectile;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class RocketLauncher extends Gun {
	{
		hole.x = 15;
		ammoMax = 1;
	}

	@Override
	protected void sendBullet(float an, float xx, float yy, BulletProjectile bullet) {
		float a = (float) Math.toDegrees(an);

		RocketProjectile b = new RocketProjectile();
		b.sprite = Graphics.getTexture("item-rocket_a");

		float x = this.owner.x + this.owner.w / 2 + (flipped ? -7 : 7);
		float y = this.owner.y + this.owner.h / 4 + this.owner.z;

		b.x = x + this.getAimX(0, -b.sprite.getRegionHeight() / 2);
		b.y = y + this.getAimY(0, -b.sprite.getRegionHeight() / 2);
		b.damage = b.damage + rollDamage();
		b.crit = true;
		// b.letter = b.bulletName;
		b.owner = this.owner;
		b.bad = this.owner instanceof Mob;
		b.penetrates = this.penetrates;
		//b.gun = this;

		this.modifyBullet(bullet);

		float s = this.vel * 0.2f;

		b.vel = new Point(
			(float) Math.cos(an) * s, (float) Math.sin(an) * s
		);

		b.a = a;

		Dungeon.area.add(b);
	}
}