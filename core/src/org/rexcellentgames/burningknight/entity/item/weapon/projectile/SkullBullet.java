package org.rexcellentgames.burningknight.entity.item.weapon.projectile;

import org.rexcellentgames.burningknight.assets.Graphics;

public class SkullBullet extends BulletProjectile {
	@Override
	protected void setup() {
		super.setup();

		this.rotates = false;
		this.second = false;
		this.noRotation = true;
		this.rectShape = false;
		this.circleShape = true;
		this.renderCircle = false;
		this.dissappearWithTime = true;
		this.lightUp = false;

		sprite = Graphics.getTexture("bullet-skull");
	}
}