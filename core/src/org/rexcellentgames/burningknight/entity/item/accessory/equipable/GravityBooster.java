package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.item.pet.impl.Orbital;
import org.rexcellentgames.burningknight.util.Tween;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.item.pet.impl.Orbital;
import org.rexcellentgames.burningknight.util.Tween;

public class GravityBooster extends Equipable {
	{
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