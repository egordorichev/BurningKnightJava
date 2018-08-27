package org.rexcellentgames.burningknight.entity.item.accessory.equippable

import org.rexcellentgames.burningknight.assets.Locale

class StoneHeartRune : Equippable() {
	init {
		name = Locale.get("stone_heart_rune")
		description = Locale.get("stone_heart_rune_desc")
		sprite = "item-scroll_h"
	}

	override fun onEquip(load: Boolean) {
		super.onEquip(load)
		this.owner.lowHealthDefense = true
	}

	override fun onUnequip(load: Boolean) {
		super.onUnequip(load)
		this.owner.lowHealthDefense = false
	}

	override fun canBeUpgraded(): Boolean {
		return false
	}
}