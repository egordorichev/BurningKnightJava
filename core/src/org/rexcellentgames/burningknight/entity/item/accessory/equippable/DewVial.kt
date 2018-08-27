package org.rexcellentgames.burningknight.entity.item.accessory.equippable

import org.rexcellentgames.burningknight.assets.Locale

class DewVial : Equippable() {
	init {
		name = Locale.get("dew_vial")
		description = Locale.get("dew_vial_desc")
		sprite = "item-dew_vial"
	}

	override fun onEquip(load: Boolean) {
		super.onEquip(load)
		this.owner.healOnEnter = true
	}

	override fun onUnequip(load: Boolean) {
		super.onUnequip(load)
		this.owner.healOnEnter = false
	}

	override fun getMaxLevel(): Int {
		return 6
	}
}