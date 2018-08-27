package org.rexcellentgames.burningknight.entity.item.accessory.equippable

import org.rexcellentgames.burningknight.assets.Locale

class IceBombs : Equippable() {
	init {
		name = Locale.get("ice_bombs")
		description = Locale.get("ice_bombs_desc")
		sprite = "item-ice_bomb"
	}

	override fun onEquip(load: Boolean) {
		super.onEquip(load)
		this.owner.iceBombs = true
	}

	override fun onUnequip(load: Boolean) {
		super.onUnequip(load)
		this.owner.iceBombs = false
	}

	override fun canBeUpgraded(): Boolean {
		return false
	}
}