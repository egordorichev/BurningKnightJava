package org.rexcellentgames.burningknight.entity.item.accessory.equippable

import org.rexcellentgames.burningknight.assets.Locale

class ManaBoots : Equippable() {
	init {
		name = Locale.get("mana_boots")
		description = Locale.get("mana_boots_desc")
		sprite = "item-running_boosts_mana_regen"
	}

	override fun onEquip(load: Boolean) {
		super.onEquip(load)
		this.owner.flipRegenFormula = true
	}

	override fun onUnequip(load: Boolean) {
		super.onUnequip(load)
		this.owner.flipRegenFormula = false
	}

	override fun canBeUpgraded(): Boolean {
		return false
	}
}