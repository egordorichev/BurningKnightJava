package org.rexcellentgames.burningknight.entity.item.accessory.equipable

import org.rexcellentgames.burningknight.assets.Locale

class Antidote : Equipable() {
	init {
		name = Locale.get("antidote")
		description = Locale.get("antidote_desc")
		sprite = "item-antidote"
	}

	override fun onEquip(load: Boolean) {
		super.onEquip(load)
		this.owner.poisonResist = ((this.owner.poisonResist + 1) % 128).toByte()
	}

	override fun onUnequip(load: Boolean) {
		super.onUnequip(load)
		this.owner.poisonResist = ((this.owner.poisonResist - 1) % 128).toByte()
	}

	override fun canBeUpgraded(): Boolean {
		return false
	}
}