package org.rexcellentgames.burningknight.entity.item.accessory.equippable

import org.rexcellentgames.burningknight.assets.Locale

class PenetrationRune : Equippable() {
	init {
		name = Locale.get("penetration_rune")
		description = Locale.get("penetration_rune_desc")
		sprite = "item-penetration_stone"
	}

	override fun onEquip(load: Boolean) {
		super.onEquip(load)
		this.owner.penetrates = true
	}

	override fun onUnequip(load: Boolean) {
		super.onUnequip(load)
		this.owner.penetrates = false
	}

	override fun canBeUpgraded(): Boolean {
		return false
	}

	override fun canBeDegraded(): Boolean {
		return false
	}
}