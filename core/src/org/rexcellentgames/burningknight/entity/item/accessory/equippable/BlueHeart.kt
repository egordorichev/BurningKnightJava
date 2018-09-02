package org.rexcellentgames.burningknight.entity.item.accessory.equippable

import org.rexcellentgames.burningknight.assets.Locale

class BlueHeart : Equippable() {
	init {
		name = Locale.get("blue_heart")
		description = Locale.get("blue_heart_desc")
		sprite = "item-picking_up_hp_restores_mana_if_not_full"
	}

	override fun getMaxLevel(): Int {
		return 6
	}

	override fun canBeDegraded(): Boolean {
		return false
	}
}