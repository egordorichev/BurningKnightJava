package org.rexellentgames.dungeon.entity.item.weapon.laser;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.fx.Laser;
import org.rexellentgames.dungeon.entity.fx.Lighting;
import org.rexellentgames.dungeon.entity.item.weapon.gun.GunA;

public class LaserGun extends GunA {
	{
		useTime = 1f;
		damage = 10;
	}

	@Override
	protected void sendBullets() {
		Laser laser = new Lighting();
		float x = this.owner.x + this.owner.w / 2 + (this.owner.isFlipped() ? -7 : 7) + 3 - 2;
		float y = this.owner.y + this.owner.h / 4 + region.getRegionHeight() / 2 - 2;

		float px = this.tw - 3;

		double an = this.owner.getAngleTo(this.owner.getAim().x, this.owner.getAim().y);

		laser.x = (float) (x + px * Math.cos(an) - this.ox);
		laser.y = (float) (y + px * Math.sin(an));

		laser.a = (float) Math.toDegrees(an - Math.PI / 2);
		laser.w = 96f;
		laser.damage = this.rollDamage();
		laser.crit = this.lastCrit;
		laser.owner = this.owner;

		Dungeon.area.add(laser);
	}
}