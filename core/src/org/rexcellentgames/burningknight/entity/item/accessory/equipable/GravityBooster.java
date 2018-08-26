package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.item.pet.impl.Orbital;
import org.rexcellentgames.burningknight.util.Tween;

public class GravityBooster extends Equipable {
	{
		name = Locale.get("gravity_booster");
		description = Locale.get("gravity_booster_desc");
		sprite = "item-blank_card";
	}

	@Override
	public void onEquip(boolean load) {
		super.onEquip(load);

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
	public void onUnequip(boolean load) {
		super.onUnequip(load);

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