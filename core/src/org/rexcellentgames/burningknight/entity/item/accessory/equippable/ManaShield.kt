package org.rexcellentgames.burningknight.entity.item.accessory.equippable

import org.rexcellentgames.burningknight.assets.Locale

class ManaShield : Equippable() {
	init {
		name = Locale.get("arcane_shield")
		description = Locale.get("arcane_shield_desc")
		sprite = "item-mana_shield"
	}

	fun getChance(): Float {
		return this.level * 10 + 40f
	}

	fun getCost(): Float {
		return if (this.level > 1) 1f else 2f
	}

	override fun getMaxLevel(): Int {
		return 4
	}

	override fun getDescription(): String {
		return super.getDescription().replace("{MANA}", getCost().toInt().toString()).replace("{CHANCE}", getChance().toInt().toString())
	}
}