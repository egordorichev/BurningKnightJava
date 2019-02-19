package org.rexcellentgames.burningknight.entity.item.weapon.projectile;

import org.rexcellentgames.burningknight.assets.Graphics;

public class BookBullet extends BulletProjectile {
	@Override
	protected void setup() {
		super.setup();

		lightUp = false;
		second = false;
		rotates = true;
		rotationSpeed = 0.3f;

		sprite = Graphics.getTexture("bullet-book");
	}
}
