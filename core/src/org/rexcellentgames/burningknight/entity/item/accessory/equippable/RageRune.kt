package org.rexcellentgames.burningknight.entity.item.accessory.equippable

import org.rexcellentgames.burningknight.assets.Locale

class RageRune : Equippable() {
	init {
		name = Locale.get("rage_rune")
		description = Locale.get("rage_rune_desc")
		sprite = "item-scroll_g"
	}

	override fun onEquip(load: Boolean) {
		super.onEquip(load)
		this.owner.lowHealthDamage = true
	}

	override fun onUnequip(load: Boolean) {
		super.onUnequip(load)
		this.owner.lowHealthDamage = false
	}

	override fun canBeUpgraded(): Boolean {
		return false
	}
}