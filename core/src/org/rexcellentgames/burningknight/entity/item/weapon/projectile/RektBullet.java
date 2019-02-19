package org.rexcellentgames.burningknight.entity.item.weapon.projectile;

import org.rexcellentgames.burningknight.assets.Graphics;

public class RektBullet extends BulletProjectile {
	@Override
	protected void setup() {
		super.setup();

		noRotation = false;
		sprite = Graphics.getTexture("bullet-rekt");
	}
}