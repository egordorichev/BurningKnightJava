package org.rexcellentgames.burningknight.entity.item.accessory.equippable

import org.rexcellentgames.burningknight.assets.Locale

class ArcaneBattery : Equippable() {
	init {
		name = Locale.get("arcane_battery")
		description = Locale.get("arcane_battery_desc")
		sprite = "item-lower_health_more_mana_regen"
	}

	override fun canBeUpgraded(): Boolean {
		return false
	}

	override fun onEquip(load: Boolean) {
		super.onEquip(load)
		this.owner.moreManaRegenWhenLow = true
	}

	override fun onUnequip(load: Boolean) {
		super.onUnequip(load)
		this.owner.moreManaRegenWhenLow = false
	}
}