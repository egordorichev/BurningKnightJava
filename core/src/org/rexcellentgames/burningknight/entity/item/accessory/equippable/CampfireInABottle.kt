package org.rexcellentgames.burningknight.entity.item.accessory.equippable

import org.rexcellentgames.burningknight.assets.Locale

class CampfireInABottle : Equippable() {
	companion object {
		val lvl2 = Locale.get("burns_floor")
	}

	init {
		name = Locale.get("campfire_in_a_bottle")
		description = Locale.get("campfire_in_a_bottle_desc")
		sprite = "item-campfire_in_a_bottle"
	}

	override fun getMaxLevel(): Int {
		return 2
	}

	override fun onEquip(load: Boolean) {
		super.onEquip(load)
		this.owner.stunResist = ((this.owner.stunResist + 1) % 128).toByte()

		if (this.level >= 2) {
			this.owner.burnLevel = ((this.owner.burnLevel + 1) % 128).toByte()
		}
	}

	override fun onUnequip(load: Boolean) {
		super.onUnequip(load)
		this.owner.stunResist = ((this.owner.stunResist - 1) % 128).toByte()

		if (this.level >= 2) {
			this.owner.burnLevel = ((this.owner.burnLevel - 1) % 128).toByte()
		}
	}

	override fun getDescription(): String {
		var d = super.getDescription()

		if (this.level >= 2) {
			d += "\n" + lvl2
		}

		return d
	}
}