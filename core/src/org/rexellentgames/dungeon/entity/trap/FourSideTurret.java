package org.rexellentgames.dungeon.entity.trap;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.item.weapon.gun.bullet.BulletEntity;
import org.rexellentgames.dungeon.util.geometry.Point;

public class FourSideTurret extends Turret {
	@Override
	protected void send() {
		for (int i = 0; i < 4; i++) {
			BulletEntity bullet = new BulletEntity();
			bullet.sprite = Graphics.getTexture("bullet (bullet bad)");

			float x = this.x + region.getRegionWidth() / 2;
			float y = this.y + region.getRegionHeight() / 2;

			bullet.x = x;
			bullet.y = y;
			bullet.damage = 2;
			bullet.letter = "bullet bad";

			this.modify(bullet);

			float s = 1.5f * 60f;

			float a = (float) (this.a + i * Math.PI / 2);

			bullet.vel = new Point(
				(float) Math.cos(a) * s, (float) Math.sin(a) * s
			);

			bullet.a = a;

			Dungeon.area.add(bullet);
		}
	}
}