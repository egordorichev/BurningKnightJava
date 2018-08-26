package org.rexcellentgames.burningknight.entity.item.accessory.equipable

import org.rexcellentgames.burningknight.assets.Locale

class MetalRing : Equipable() {
	init {
		super.init()

		name = Locale.get("metal_ring")
		description = Locale.get("metal_ring_desc")
		sprite = "item-ring_d"
	}

	override fun onEquip(load: Boolean) {
		super.onEquip(load)
		owner.reflectDamageChance += getChance()
	}

	override fun onUnequip(load: Boolean) {
		super.onUnequip(load)
		owner.reflectDamageChance -= getChance()
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