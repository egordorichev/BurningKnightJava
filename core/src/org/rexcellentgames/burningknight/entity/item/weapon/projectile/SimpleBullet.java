package org.rexcellentgames.burningknight.entity.item.weapon.projectile;

import org.rexcellentgames.burningknight.assets.Graphics;

public class SimpleBullet extends BulletProjectile {
	@Override
	protected void setup() {
		super.setup();

		sprite = Graphics.getTexture("bullet-a");
	}
}