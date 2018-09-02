package org.rexcellentgames.burningknight.entity.item.accessory.equippable

import org.rexcellentgames.burningknight.assets.Locale

class Lootpick : Equippable() {
	init {
		sprite = "item-lootpick"
		name = Locale.get("lootpick")
		description = Locale.get("lootpick_desc")
	}

	override fun canBeUpgraded(): Boolean {
		return false
	}

	override fun canBeDegraded(): Boolean {
		return false
	}
}