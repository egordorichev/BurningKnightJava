package org.rexcellentgames.burningknight.entity.item.accessory.equipable

import org.rexcellentgames.burningknight.assets.Locale

class ArcaneBattery : Equipable() {
	init {
		name = Locale.get("arcane_battery")
		description = Locale.get("arcane_battery_desc")
		sprite = "item-lower_health_more_mana_regen"
	}

	override fun canBeUpgraded(): Boolean {
		return false
	}

	override fun onEquip() {
		super.onEquip()
		this.owner.moreManaRegenWhenLow = true
	}

	override fun onUnequip() {
		super.onUnequip()
		this.owner.moreManaRegenWhenLow = false
	}
}