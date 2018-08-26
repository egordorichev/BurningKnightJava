package org.rexcellentgames.burningknight.entity.item.accessory.equipable

import org.rexcellentgames.burningknight.assets.Locale

class GoldRing : Equipable() {
	init {
		description = Locale.get("gold_ring_desc")
		name = Locale.get("gold_ring")
		sprite = "item-ring_b"
	}

	override fun onEquip() {
		super.onEquip()
		this.owner.goldModifier += getChance() / 100f
	}

	override fun onUnequip() {
		super.onUnequip()
		this.owner.goldModifier -= getChance() / 100f
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