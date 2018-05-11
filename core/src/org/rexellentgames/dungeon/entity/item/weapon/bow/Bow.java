package org.rexellentgames.dungeon.entity.item.weapon.bow;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.item.Item;
import org.rexellentgames.dungeon.entity.item.weapon.bow.arrows.Arrow;
import org.rexellentgames.dungeon.entity.item.weapon.bow.arrows.ArrowA;
import org.rexellentgames.dungeon.entity.item.weapon.bow.arrows.ArrowEntity;
import org.rexellentgames.dungeon.game.input.Input;
import org.rexellentgames.dungeon.util.Tween;

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
		Arrow ar = new ArrowA();
		super.use();

		float a = (float) (this.owner.getAngleTo(Input.instance.worldMouse.x, Input.instance.worldMouse.y) - Math.PI);
		float s = 60f;

		this.owner.vel.x += Math.cos(a) * s;
		this.owner.vel.y += Math.sin(a) * s;

		ArrowEntity arrow = new ArrowEntity();

		arrow.owner = this.owner;

		float dx = Input.instance.worldMouse.x - this.owner.x - this.owner.w / 2;
		float dy = Input.instance.worldMouse.y - this.owner.y - this.owner.h / 2;

		arrow.type = ar.getClass();
		arrow.sprite = ar.getSprite();
		arrow.a = (float) Math.atan2(dy, dx);
		arrow.x = (float) (this.owner.x + this.owner.w / 2 + Math.cos(arrow.a) * 16);
		arrow.y = (float) (this.owner.y + this.owner.h / 2 + Math.sin(arrow.a) * 16);
		arrow.damage = this.damage + ar.damage;

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
		TextureRegion s = this.getSprite();
		float dx = Input.instance.worldMouse.x - this.owner.x - this.owner.w / 2;
		float dy = Input.instance.worldMouse.y - this.owner.y - this.owner.h / 2;
		float a = (float) Math.toDegrees(Math.atan2(dy, dx));

		Graphics.startShadows();
		Graphics.render(s, x + w / 2, y - h / 2, -a, -4, s.getRegionHeight() / 2, false, false, sx, -sy);
		Graphics.endShadows();
		Graphics.render(s, x + w / 2, y + h / 2, a, -4, s.getRegionHeight() / 2, false, false, sx, sy);
	}
}