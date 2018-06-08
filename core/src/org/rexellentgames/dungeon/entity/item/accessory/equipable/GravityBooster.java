package org.rexellentgames.dungeon.entity.item.accessory.equipable;

import org.rexellentgames.dungeon.assets.Locale;
import org.rexellentgames.dungeon.entity.item.pet.impl.Orbital;
import org.rexellentgames.dungeon.util.Tween;

public class GravityBooster extends Equipable {
	{
		name = Locale.get("gravity_booster");
		description = Locale.get("gravity_booster_desc");
		sprite = "item-blank_card";
	}

	@Override
	public void onEquip() {
		super.onEquip();

		Tween.to(new Tween.Task(Orbital.speed + 1, 0.3f) {
			@Override
			public float getValue() {
				return Orbital.speed;
			}

			@Override
			public void setValue(float value) {
				Orbital.speed = value;
			}
		});
	}

	@Override
	public void onUnequip() {
		super.onUnequip();

		Tween.to(new Tween.Task(Orbital.speed - 1, 0.3f) {
			@Override
			public float getValue() {
				return Orbital.speed;
			}

			@Override
			public void setValue(float value) {
				Orbital.speed = value;
			}
		});
	}
}