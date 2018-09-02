package org.rexcellentgames.burningknight.entity.item.accessory.equippable

import org.rexcellentgames.burningknight.assets.Locale

class LaserAim : Equippable() {
	init {
		name = Locale.get("laser_aim")
		description = Locale.get("laser_aim")
		sprite = "item-laser_pointer"
	}

	override fun onEquip(load: Boolean) {
		super.onEquip(load)
		this.owner.hasRedLine = true
	}

	override fun onUnequip(load: Boolean) {
		super.onUnequip(load)
		this.owner.hasRedLine = false
	}

	override fun canBeUpgraded(): Boolean {
		return false
	}

	override fun canBeDegraded(): Boolean {
		return false
	}
}