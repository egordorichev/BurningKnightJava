package org.rexcellentgames.burningknight.entity.item.weapon.dagger;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.Gun;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.Sword;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class Dagger extends Sword {
	{
		knockback = 30f;
		useTime = 0.3f;
		auto = true;
	}


	@Override
	public void render(float x, float y, float w, float h, boolean flipped) {
		if (this.animation == null) {
			this.animation = animations.get("idle");
			this.animation.setPaused(true);
		}

		float angle = 0;

		if (this.owner != null) {
			Point aim = this.owner.getAim();

			float an = (float) (this.owner.getAngleTo(aim.x, aim.y) - Math.PI / 2);
			an = Gun.angleLerp(this.lastAngle, an, 0.15f);
			this.lastAngle = an;
			float a = (float) Math.toDegrees(this.lastAngle);

			angle += a;
			// angle = flipped ? angle : 180 - angle;
		}

		TextureRegion sprite = this.getSprite();

		float a = (float) Math.toRadians(angle);
		float an = this.added / 16f;

		float xx = (float) (x + w / 2 + (flipped ? -w / 4 : w / 4) + Math.cos(a + Math.PI / 2) * an);
		float yy = (float) (y + (this.ox == 0 ? h / 4 : h / 2) + Math.sin(a + Math.PI / 2) * an);

		if (!this.animation.isPaused() && !this.owner.isDead()) {
			this.animation.render(x + w / 2, y - this.owner.hh / 2, false, false, 0, 11, angle + 90, false);
		}

		this.renderAt(xx - (flipped ? sprite.getRegionWidth() : 0), yy,
			angle, sprite.getRegionWidth() / 2 + (flipped ? this.ox : -this.ox), this.oy, flipped, false);

		if (this.blockbox != null) {
			this.blockbox.setTransform(xx + (float) Math.cos(a) * (flipped ? 0 : ox * 2), yy + (float) Math.sin(a) * (flipped ? 0 : ox * 2), a);
		} else if (this.body != null) {
			this.body.setTransform(xx, yy, a);
		}
	}
}