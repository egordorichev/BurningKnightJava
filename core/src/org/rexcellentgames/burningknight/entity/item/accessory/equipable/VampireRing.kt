package org.rexcellentgames.burningknight.entity.item.accessory.equipable

import org.rexcellentgames.burningknight.assets.Locale

class VampireRing : Equipable() {
	init {
		description = Locale.get("vampire_ring_desc")
		name = Locale.get("vampire_ring")
		sprite = "item-ring_j"
	}

	override fun onEquip() {
		super.onEquip()
		this.owner.vampire += getChance()
	}

	override fun getMaxLevel(): Int {
		return 10
	}

	override fun onUnequip() {
		super.onUnequip()
		this.owner.vampire -= getChance()
	}

	private fun getChance(): Float {
		return this.level * 10f
	}

	override fun getDescription(): String {
		return super.getDescription().replace("{CHANCE}", getChance().toInt().toString())
	}
}