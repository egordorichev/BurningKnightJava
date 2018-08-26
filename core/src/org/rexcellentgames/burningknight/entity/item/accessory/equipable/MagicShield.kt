package org.rexcellentgames.burningknight.entity.item.accessory.equipable

import org.rexcellentgames.burningknight.assets.Locale

class MagicShield : Equipable() {
	init {
		name = Locale.get("magic_shield")
		description = Locale.get("magic_shield_desc")
		sprite = "item-shield"
	}

	override fun onEquip() {
		super.onEquip()
		this.owner.modifyStat("block_chance", getChance() / 10)
	}

	override fun onUnequip() {
		super.onUnequip()
		this.owner.modifyStat("block_chance", -getChance() / 10)
	}

	private fun getChance(): Float {
		return this.level * 10f
	}

	override fun getDescription(): String {
		return super.getDescription().replace("{CHANCE}", getChance().toInt().toString())
	}
}