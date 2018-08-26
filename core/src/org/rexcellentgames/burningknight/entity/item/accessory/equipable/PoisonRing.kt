package org.rexcellentgames.burningknight.entity.item.accessory.equipable

import org.rexcellentgames.burningknight.assets.Locale

class PoisonRing : Equipable() {
	init {
		super.init()

		name = Locale.get("poison_ring")
		description = Locale.get("poison_ring_desc")
		sprite = "item-ring_h"
	}

	override fun onEquip() {
		super.onEquip()

		owner.poisonChance += getChance()
	}

	override fun onUnequip() {
		super.onUnequip()
		owner.poisonChance -= getChance()
	}

	private fun getChance(): Float {
		return 20f + this.level * 10
	}

	override fun getMaxLevel(): Int {
		return 8
	}

	override fun getDescription(): String {
		return super.getDescription().replace("{CHANCE}", getChance().toInt().toString())
	}
}