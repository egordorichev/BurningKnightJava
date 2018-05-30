package org.rexellentgames.dungeon.entity.item.weapon.sword.claymore;

import org.rexellentgames.dungeon.assets.Locale;
import org.rexellentgames.dungeon.entity.item.weapon.sword.Sword;
import org.rexellentgames.dungeon.game.input.Input;
import org.rexellentgames.dungeon.physics.World;
import org.rexellentgames.dungeon.util.Tween;

public class ClaymoreA extends Sword {
	{
		sprite = "item (claymore)";
		damage = 2;
		description = Locale.get("claymore_a_desc");
		name = Locale.get("claymore_a");
	}

	@Override
	public void use() {
		if (this.blocking || this.delay > 0) {
			return;
		}

		this.owner.playSfx(this.getSfx());

		this.animation.setPaused(false);

		if (this.body != null) {
			this.body = World.removeBody(this.body);
		}

		this.createHitbox();

		float a = this.owner.getAngleTo(Input.instance.worldMouse.x, Input.instance.worldMouse.y);

		this.owner.vel.x += -Math.cos(a) * 30f;
		this.owner.vel.y += -Math.sin(a) * 30f;

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