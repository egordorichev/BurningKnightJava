package org.rexcellentgames.burningknight.entity.item.weapon.laser;

import org.rexcellentgames.burningknight.entity.fx.Laser;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.Revolver;
import org.rexcellentgames.burningknight.Dungeon;

public class LaserGun extends Revolver {
	{
		useTime = 1f;
		damage = 30;
	}

	@Override
	protected void sendBullets() {
		this.owner.playSfx("laser");

		Laser laser = new Laser();
		float x = this.owner.x + this.owner.w / 2 + (this.owner.isFlipped() ? -7 : 7) + 3 - 2;
		float y = this.owner.y + this.owner.h / 4 + region.getRegionHeight() / 2 - 2;

		float px = this.tw - 3;

		double an = this.owner.getAngleTo(this.owner.getAim().x, this.owner.getAim().y);

		laser.x = (float) (x + px * Math.cos(an) - this.origin.x);
		laser.y = (float) (y + px * Math.sin(an));

		laser.a = (float) Math.toDegrees(an - Math.PI / 2);
		laser.w = 96f;
		laser.damage = this.rollDamage();
		laser.crit = this.lastCrit;
		laser.owner = this.owner;

		Dungeon.area.add(laser);
	}
}