package org.rexcellentgames.burningknight.entity.item.accessory.equipable

import org.rexcellentgames.burningknight.assets.Locale

class MagicShield : Equipable() {
	init {
		name = Locale.get("magic_shield")
		description = Locale.get("magic_shield_desc")
		sprite = "item-shield"
	}

	override fun onEquip(load: Boolean) {
		super.onEquip(load)
		this.owner.modifyStat("block_chance", getChance() / 100)
	}

	override fun onUnequip(load: Boolean) {
		super.onUnequip(load)
		this.owner.modifyStat("block_chance", -getChance() / 100)
	}

	private fun getChance(): Float {
		return this.level * 10f
	}

	override fun getDescription(): String {
		return super.getDescription().replace("{CHANCE}", getChance().toInt().toString())
	}

	override fun getMaxLevel(): Int {
		return 8
	}
}