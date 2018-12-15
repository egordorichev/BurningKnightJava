package org.rexcellentgames.burningknight.entity.item.weapon.sword;

import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.util.Tween;

public class Lightsaber extends Sword {
	{
		maxAngle = 360;
		auto = true;
		penetrates = true;
		tr = 0f;
		tg = 0.7f;
		tail = true;
		timeA = 0.2f;
		useTime = timeA;
	}

	@Override
	public void generateModifier() {

	}

	protected void setStats() {
		String letter = this.level <= 1 ? "a" : (this.level <= 2 ? "b" : (this.level <= 3 ? "c" : "d"));

		name = Locale.get("lightsaber_" + letter);
		description = Locale.get("lightsaber_desc");
		sprite = "item-lightsaber " + letter.toUpperCase();
		damage = 6;
		useTime = timeA;
		region = Graphics.getTexture(sprite);
	}

	@Override
	public int getMaxLevel() {
		return 4;
	}

	@Override
	public void use() {
		if (this.delay > 0) {
			return;
		}

		this.owner.playSfx(this.getSfx());

		this.createHitbox();
		this.delay = this.useTime;

		float a = this.owner.getAngleTo(Input.instance.worldMouse.x, Input.instance.worldMouse.y);

		this.owner.velocity.x += -Math.cos(a) * 30f;
		this.owner.velocity.y += -Math.sin(a) * 30f;

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
				added = 0;
				endUse();
			}
		});
	}
}