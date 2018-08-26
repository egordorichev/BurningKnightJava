package org.rexcellentgames.burningknight.entity.item.accessory.equipable

import org.rexcellentgames.burningknight.assets.Locale

class IceRing : Equipable() {
	init {
		super.init()

		name = Locale.get("ice_ring")
		description = Locale.get("ice_ring_desc")
		sprite = "item-ring_e"
	}

	override fun onEquip() {
		super.onEquip()
		owner.freezeChance += getChance()
	}

	override fun onUnequip() {
		super.onUnequip()
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