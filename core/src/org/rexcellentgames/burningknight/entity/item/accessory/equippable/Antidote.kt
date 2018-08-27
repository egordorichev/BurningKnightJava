package org.rexcellentgames.burningknight.entity.item.accessory.equippable

import org.rexcellentgames.burningknight.assets.Locale

class Antidote : Equippable() {
	companion object {
		val lvl2 = Locale.get("leaves_venom")
	}

	init {
		name = Locale.get("antidote")
		description = Locale.get("antidote_desc")
		sprite = "item-antidote"
	}

	override fun onEquip(load: Boolean) {
		super.onEquip(load)
		this.owner.poisonResist = ((this.owner.poisonResist + 1) % 128).toByte()

		if (this.level >= 2) {
			this.owner.leaveVenom = ((this.owner.leaveVenom + 1) % 128).toByte()
		}
	}

	override fun onUnequip(load: Boolean) {
		super.onUnequip(load)
		this.owner.poisonResist = ((this.owner.poisonResist - 1) % 128).toByte()

		if (this.level >= 2) {
			this.owner.leaveVenom = ((this.owner.leaveVenom - 1) % 128).toByte()
		}
	}

	override fun getMaxLevel(): Int {
		return 2
	}

	override fun getDescription(): String {
		var d = super.getDescription()

		if (this.level >= 2) {
			d += "\n" + lvl2
		}

		return d
	}
}