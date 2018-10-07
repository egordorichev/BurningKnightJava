package org.rexcellentgames.burningknight.entity.item.weapon.sword;

import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Tween;

public class Claymore extends Sword {
	protected void setStats() {
		String letter = this.level <= 2 ? "a" : (this.level <= 4 ? "b" : "c");

		name = Locale.get("claymore_" + letter);
		description = Locale.get("claymore_desc");
		sprite = "item-claymore_" + letter;
		damage = 4;
		useTime = 0.4f;
		region = Graphics.getTexture(sprite);
	}

	@Override
	public void use() {
		if (this.delay > 0) {
			return;
		}

		this.delay = this.useTime;
		this.owner.playSfx(this.getSfx());

		if (this.body != null) {
			this.body = World.removeBody(this.body);
		}

		this.createHitbox();

		float a = this.owner.getAngleTo(Input.instance.worldMouse.x, Input.instance.worldMouse.y);

		this.owner.knockback.x += -Math.cos(a) * 30f;
		this.owner.knockback.y += -Math.sin(a) * 30f;

		if (this.added != 0) {
			Tween.to(new Tween.Task(0, this.timeA) {
				@Override
				public float getValue() {
					return added;
				}

				@Override
				public void setValue(float value) {
					added = value;
				}

				@Override
				public void onEnd() {
					endUse();
				}
			});
		} else {
			Tween.to(new Tween.Task(this.maxAngle, this.timeA) {
				@Override
				public float getValue() {
					return added;
				}

				@Override
				public void setValue(float value) {
					added = value;
				}

				@Override
				public void onEnd() {
					endUse();
				}
			});
		}
	}
}