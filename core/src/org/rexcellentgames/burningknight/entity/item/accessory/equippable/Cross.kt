package org.rexcellentgames.burningknight.entity.item.accessory.equippable

import org.rexcellentgames.burningknight.assets.Locale

class Cross : Equippable() {
	init {
		name = Locale.get("cross")
		description = Locale.get("cross_desc")
		sprite = "item-cross"
	}

	override fun onEquip(load: Boolean) {
		super.onEquip(load)
		this.owner.modifyStat("inv_time", getChance())
	}

	override fun onUnequip(load: Boolean) {
		super.onUnequip(load)
		this.owner.modifyStat("inv_time", -getChance())
	}

	private fun getChance(): Float {
		return 0.2f + 0.2f * level
	}

	override fun getMaxLevel(): Int {
		return 9
	}

	override fun getDescription(): String { // fixme
		return super.getDescription().replace("{TIME}", getChance().toString().replace(".0", "").replace("000001", ""))
	}
}