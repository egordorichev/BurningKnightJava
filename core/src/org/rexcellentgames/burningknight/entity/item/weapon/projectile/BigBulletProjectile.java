package org.rexcellentgames.burningknight.entity.item.weapon.projectile;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.bullet.Part;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class BigBulletProjectile extends BulletProjectile {
	@Override
	public void init() {
		parts = true;
		this.sprite = Graphics.getTexture("bullet-big");
		super.init();
	}

	@Override
	protected void onDeath() {
		super.onDeath();

		for (int i = 0; i < 16; i++) {
			float a = (float) (i * Math.PI / 8);

			BulletProjectile bullet = new BulletProjectile();
			bullet.damage = 2;
			bullet.letter = "bad";
			bullet.owner = this.owner;
			bullet.bad = true;
			bullet.a = a;
			bullet.parts = true;
			bullet.sprite = Graphics.getTexture("bullet-bad");
			bullet.x = this.x + (this.w - bullet.sprite.getRegionWidth()) / 2;
			bullet.y = this.y + (this.h - bullet.sprite.getRegionHeight()) / 2;

			float s = 0.8f * 60f;

			for (int j = 0; j < 20; j++) {
				Part part = new Part();

				part.x = this.x + Random.newFloat(this.w / 2) + this.w / 2;
				part.y = this.y + Random.newFloat(this.h / 2) + this.h / 2;

				Dungeon.area.add(part);
			}

			bullet.vel = new Point((float) Math.cos(a) * s, (float) Math.sin(a) * s);

			Dungeon.area.add(bullet);
		}
	}
}