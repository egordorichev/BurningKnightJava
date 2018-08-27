package org.rexcellentgames.burningknight.entity.item.weapon.magic.book;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.Gun;
import org.rexcellentgames.burningknight.entity.item.weapon.magic.Wand;
import org.rexcellentgames.burningknight.util.Tween;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class Book extends Wand {
	@Override
	public void render(float x, float y, float w, float h, boolean flipped) {
		float angle = 0;

		if (this.owner != null) {
			Point aim = this.owner.getAim();

			float an = (float) (this.owner.getAngleTo(aim.x, aim.y) - Math.PI);
			an = Gun.angleLerp(this.lastAngle, an, 0.15f);
			this.lastAngle = an;
			float a = (float) Math.toDegrees(this.lastAngle);

			angle += (flipped ? a : -a);
			angle = flipped ? angle : 180 - angle;
		}

		TextureRegion sprite = this.getSprite();

		float xx = x + w / 2 + (flipped ? 0 : w / 4);
		float yy = y + h / 4;

		this.renderAt(xx - (flipped ? sprite.getRegionWidth() / 2 : 0), yy,
			angle, 0, 0, false, false, flipped ? -sx : sx, sy);
	}

	@Override
	protected void sendProjectiles() {
		float angle = 0;

		if (this.owner != null) {
			Point aim = this.owner.getAim();

			float an = (float) (this.owner.getAngleTo(aim.x, aim.y) - Math.PI);
			an = Gun.angleLerp(this.lastAngle, an, 0.15f);
			this.lastAngle = an;
			float a = (float) Math.toDegrees(this.lastAngle);

			angle += (this.owner.isFlipped() ? a : -a);
			angle = this.owner.isFlipped() ? angle : 180 - angle;

			if (this.owner.isFlipped()) {
				angle -= 180;
			}
		}

		sx = 0.5f;
		sy = 1.4f;

		Tween.to(new Tween.Task(1f, 0.2f) {
			@Override
			public float getValue() {
				return sy;
			}

			@Override
			public void setValue(float value) {
				sy = value;
			}
		});

		Tween.to(new Tween.Task(1f, 0.2f) {
			@Override
			public float getValue() {
				return sx;
			}

			@Override
			public void setValue(float value) {
				sx = value;
			}
		});

		// todo: fix

		float xx = this.owner.x + this.owner.w / 2 +
			(this.owner.isFlipped() ? 0 : this.owner.w / 4);

		float yy = this.owner.y + this.owner.h / 2;

		double a = Math.toRadians(angle);

		this.owner.vel.x -= Math.cos(a) * 40f;
		this.owner.vel.y -= Math.sin(a) * 40f;

		this.spawnProjectile(xx, yy, angle);
	}
}