package org.rexcellentgames.burningknight.entity.item.accessory.equippable

import org.rexcellentgames.burningknight.assets.Locale

class ProtectiveBand : Equippable() {
	init {
		name = Locale.get("protective_band")
		description = Locale.get("protective_band_desc")
		sprite = "item-black_belt"
	}

	override fun onEquip(load: Boolean) {
		super.onEquip(load)
		this.owner.explosionBlock = true
	}

	override fun onUnequip(load: Boolean) {
		super.onUnequip(load)
		this.owner.explosionBlock = false
	}

	override fun canBeUpgraded(): Boolean {
		return false
	}

	override fun canBeDegraded(): Boolean {
		return false
	}
}