package org.rexcellentgames.burningknight.entity.item.accessory.equipable

import org.rexcellentgames.burningknight.assets.Locale
import org.rexcellentgames.burningknight.entity.item.pet.impl.Orbital
import org.rexcellentgames.burningknight.util.Tween

class GravityBooster : Equipable() {
	init {
		name = Locale.get("gravity_booster")
		description = Locale.get("gravity_booster_desc")
		sprite = "item-blank_card"
	}

	override fun onEquip(load: Boolean) {
		super.onEquip(load)

		Tween.to(object : Tween.Task(Orbital.speed + this.level, 0.3f) {
			override fun getValue(): Float {
				return Orbital.speed
			}

			override fun setValue(value: Float) {
				Orbital.speed = value
			}
		})
	}

	override fun upgrade() {
		this.level++;
		this.onEquip(false);
	}

	override fun onUnequip(load: Boolean) {
		super.onUnequip(load)

		Tween.to(object : Tween.Task(Orbital.speed - this.level, 0.3f) {
			override fun getValue(): Float {
				return Orbital.speed
			}

			override fun setValue(value: Float) {
				Orbital.speed = value
			}
		})
	}
}