package org.rexcellentgames.burningknight.entity.item.accessory.equippable

import org.rexcellentgames.burningknight.assets.Locale

class VampireRing : Equippable() {
	init {
		description = Locale.get("vampire_ring_desc")
		name = Locale.get("vampire_ring")
		sprite = "item-ring_j"
	}

	override fun onEquip(load: Boolean) {
		super.onEquip(load)
		this.owner.vampire += getChance()
	}

	override fun getMaxLevel(): Int {
		return 10
	}

	override fun canBeDegraded(): Boolean {
		return false
	}

	override fun onUnequip(load: Boolean) {
		super.onUnequip(load)
		this.owner.vampire -= getChance()
	}

	private fun getChance(): Float {
		return this.level * 10f
	}

	override fun getDescription(): String {
		return super.getDescription().replace("{CHANCE}", getChance().toInt().toString())
	}
}