package org.rexellentgames.dungeon.entity.item.weapon.bow;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.creature.mob.Mob;
import org.rexellentgames.dungeon.entity.item.Item;
import org.rexellentgames.dungeon.entity.item.weapon.bow.arrows.Arrow;
import org.rexellentgames.dungeon.entity.item.weapon.bow.arrows.ArrowEntity;
import org.rexellentgames.dungeon.util.Tween;
import org.rexellentgames.dungeon.util.geometry.Point;

public class Bow extends Item {
	protected int damage;
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

		this.owner.vel.x += Math.cos(a) * s;
		this.owner.vel.y += Math.sin(a) * s;

		ArrowEntity arrow = new ArrowEntity();

		arrow.owner = this.owner;

		float dx = aim.x - this.owner.x - this.owner.w / 2;
		float dy = aim.y - this.owner.y - this.owner.h / 2;

		arrow.type = ar.getClass();
		arrow.sprite = ar.getSprite();
		arrow.a = (float) Math.atan2(dy, dx);
		arrow.x = (float) (this.owner.x + this.owner.w / 2 + Math.cos(arrow.a) * 16);
		arrow.y = (float) (this.owner.y + this.owner.h / 2 + Math.sin(arrow.a) * 16);
		arrow.damage = this.damage + ar.damage;
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

	@Override
	public void render(float x, float y, float w, float h, boolean flipped) {
		Point aim = this.owner.getAim();

		TextureRegion s = this.getSprite();
		float dx = aim.x - this.owner.x - this.owner.w / 2;
		float dy = aim.y - this.owner.y - this.owner.h / 2;
		float a = (float) Math.toDegrees(Math.atan2(dy, dx));

		Graphics.startShadows();
		Graphics.render(s, x + w / 2, y - h / 2, -a, -4, s.getRegionHeight() / 2, false, false, sx, -sy);
		Graphics.endShadows();
		Graphics.render(s, x + w / 2, y + h / 2, a, -4, s.getRegionHeight() / 2, false, false, sx, sy);
	}
}