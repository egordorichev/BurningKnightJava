package org.rexcellentgames.burningknight.entity.item.accessory.equipable

import org.rexcellentgames.burningknight.assets.Locale

class BloodRing : Equipable() {
	init {
		description = Locale.get("blood_ring_desc")
		name = Locale.get("blood_ring")
		sprite = "item-ring_c"
	}

	override fun onEquip(load: Boolean) {
		super.onEquip(load)
		this.owner.regen += getChance()
	}

	override fun onUnequip(load: Boolean) {
		super.onUnequip(load)
		this.owner.regen -= getChance()
	}

	private fun getChance(): Float {
		return this.level * 2f - 1
	}
}