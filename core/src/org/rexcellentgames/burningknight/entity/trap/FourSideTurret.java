package org.rexcellentgames.burningknight.entity.trap;

import org.rexcellentgames.burningknight.entity.item.weapon.projectile.BulletProjectile;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.util.AnimationData;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class FourSideTurret extends Turret {
	private AnimationData four = animations.get("four");
	boolean str = true;

	@Override
	public void update(float dt) {
		super.update(dt);

		this.four.setFrame(str ? 0 : 1);
	}

	@Override
	public void render() {
		four.render(this.x, this.y, false);
	}

	@Override
	protected void send() {
		for (int i = 0; i < 4; i++) {
			BulletProjectile bullet = new BulletProjectile();
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