package org.rexcellentgames.burningknight.entity.item.accessory.equippable

import org.rexcellentgames.burningknight.assets.Locale
import org.rexcellentgames.burningknight.entity.item.pet.impl.Orbital

class GravityBooster : Equippable() {
	init {
		name = Locale.get("gravity_booster")
		description = Locale.get("gravity_booster_desc")
		sprite = "item-blank_card"
	}

	override fun onEquip(load: Boolean) {
		super.onEquip(load)
		Orbital.speed += this.level
	}

	override fun onUnequip(load: Boolean) {
		super.onUnequip(load)

		if (!equipped) {
			Orbital.speed = 1f
		}
	}

	override fun canBeDegraded(): Boolean {
		return false
	}
}