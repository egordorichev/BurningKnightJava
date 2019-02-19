package org.rexcellentgames.burningknight.entity.item.weapon.projectile;

import org.rexcellentgames.burningknight.assets.Graphics;

public class KotlinBullet extends BulletProjectile {
	@Override
	protected void setup() {
		super.setup();

		this.second = false;
		lightUp = false;

		sprite = Graphics.getTexture("bullet-kotlin");
	}
}
