package org.rexcellentgames.burningknight.entity.item.accessory.equippable

import org.rexcellentgames.burningknight.assets.Locale

class CobaltShield : Equippable() {
	init {
		name = Locale.get("cobalt_shield")
		description = Locale.get("cobalt_shield_desc")
		sprite = "item-obsidian_shield"
	}

	override fun onEquip(load: Boolean) {
		super.onEquip(load)
		this.owner.modifyStat("knockback", -1f)
	}

	override fun onUnequip(load: Boolean) {
		super.onUnequip(load)
		this.owner.modifyStat("knockback", 1f)
	}

	override fun canBeUpgraded(): Boolean {
		return false
	}
}