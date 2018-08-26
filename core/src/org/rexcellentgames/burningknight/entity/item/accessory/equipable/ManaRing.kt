package org.rexcellentgames.burningknight.entity.item.accessory.equipable

import org.rexcellentgames.burningknight.assets.Locale

class ManaRing : Equipable() {
	init {
		name = Locale.get("mana_ring")
		description = Locale.get("mana_ring_desc")
		sprite = "item-mana_ring"
	}

	override fun onEquip(load: Boolean) {
		super.onEquip(load)
		this.owner.manaRegenRate += getChance()
	}

	override fun onUnequip(load: Boolean) {
		super.onUnequip(load)
		this.owner.manaRegenRate -= getChance()
	}

	private fun getChance(): Float {
		return this.level * 0.5f
	}

	override fun getMaxLevel(): Int {
		return 8
	}
}