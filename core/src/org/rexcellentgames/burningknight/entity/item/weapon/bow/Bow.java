package org.rexcellentgames.burningknight.entity.item.weapon.bow;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.item.weapon.bow.arrows.Arrow;
import org.rexcellentgames.burningknight.entity.item.weapon.bow.arrows.ArrowEntity;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.item.weapon.WeaponBase;
import org.rexcellentgames.burningknight.entity.item.weapon.bow.arrows.Arrow;
import org.rexcellentgames.burningknight.entity.item.weapon.bow.arrows.ArrowEntity;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.Gun;
import org.rexcellentgames.burningknight.util.Tween;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class Bow extends WeaponBase {
	private float sx = 1f;
	private float sy = 1f;

	{
		auto = true;
		identified = true;
	}

	@Override
	public void use() {
		Arrow ar = (Arrow) this.owner.getAmmo("arrow");
		Point aim = this.owner.getAim();

		super.use();

		float a = (float) (this.owner.getAngleTo(aim.x, aim.y) - Math.PI);
		float s = 60f;

		this.owner.vel.x += Math.cos(a) * s * owner.knockbackMod;
		this.owner.vel.y += Math.sin(a) * s * owner.knockbackMod;

		ArrowEntity arrow = new ArrowEntity();

		arrow.owner = this.owner;

		float dx = aim.x - this.owner.x - this.owner.w / 2;
		float dy = aim.y - this.owner.y - this.owner.h / 2;

		arrow.type = ar.getClass();
		arrow.sprite = ar.getSprite();
		arrow.a = (float) Math.atan2(dy, dx);
		arrow.x = (float) (this.owner.x + this.owner.w / 2 + Math.cos(arrow.a) * 16);
		arrow.y = (float) (this.owner.y + this.owner.h / 2 + Math.sin(arrow.a) * 16);
		arrow.damage = rollDamage() + ar.damage;
		arrow.crit = lastCrit;
		arrow.bad = this.owner instanceof Mob;

		Dungeon.area.add(arrow);

		Tween.to(new Tween.Task(1.5f, 0.1f) {
			@Override
			public float getValue() {
				return sx;
			}

			@Override
			public void setValue(float value) {
				sx = value;
			}

			@Override
			public void onEnd() {
				Tween.to(new Tween.Task(1f, 0.2f, Tween.Type.BACK_OUT) {
					@Override
					public float getValue() {
						return sx;
					}

					@Override
					public void setValue(float value) {
						sx = value;
					}
				});
			}
		});

		Tween.to(new Tween.Task(0.4f, 0.1f) {
			@Override
			public float getValue() {
				return sy;
			}

			@Override
			public void setValue(float value) {
				sy = value;
			}

			@Override
			public void onEnd() {
				Tween.to(new Tween.Task(1f, 0.2f, Tween.Type.BACK_OUT) {
					@Override
					public float getValue() {
						return sy;
					}

					@Override
					public void setValue(float value) {
						sy = value;
					}
				});
			}
		});
	}

	private float lastAngle;

	@Override
	public void render(float x, float y, float w, float h, boolean flipped) {
		Point aim = this.owner.getAim();

		float an = this.owner.getAngleTo(aim.x, aim.y);
		an = Gun.angleLerp(this.lastAngle, an, 0.15f);
		this.lastAngle = an;
		float a = (float) Math.toDegrees(this.lastAngle);

		TextureRegion s = this.getSprite();
		this.renderAt(x + w / 2, y + h / 2, a, -4, s.getRegionHeight() / 2, false, false, sx, sy);
	}
}