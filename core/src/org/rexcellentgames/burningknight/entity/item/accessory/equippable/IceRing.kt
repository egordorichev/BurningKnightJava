package org.rexcellentgames.burningknight.entity.item.accessory.equippable

import org.rexcellentgames.burningknight.assets.Locale

class IceRing : Equippable() {
	init {
		super.init()

		name = Locale.get("ice_ring")
		description = Locale.get("ice_ring_desc")
		sprite = "item-ring_e"
	}

	override fun onEquip(load: Boolean) {
		super.onEquip(load)
		owner.freezeChance += getChance()
	}

	override fun onUnequip(load: Boolean) {
		super.onUnequip(load)
		owner.freezeChance -= getChance()
	}

	private fun getChance(): Float {
		return 10f + this.level * 10
	}

	override fun getMaxLevel(): Int {
		return 9
	}

	override fun getDescription(): String {
		return super.getDescription().replace("{CHANCE}", getChance().toInt().toString())
	}
}