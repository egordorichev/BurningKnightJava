package org.rexcellentgames.burningknight.entity.item.weapon.projectile;

import org.rexcellentgames.burningknight.assets.Graphics;

public class NanoBullet extends BulletProjectile {
	@Override
	protected void setup() {
		super.setup();

		this.noRotation = true;
		this.second = false;

		sprite = Graphics.getTexture("bullet-nano");
	}
}