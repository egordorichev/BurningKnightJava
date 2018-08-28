package org.rexcellentgames.burningknight.entity.item.accessory.equippable

import org.rexcellentgames.burningknight.assets.Locale

class IceBoots : Equippable() {
	init {
		sprite = "item-frost_boots"
	}

	override fun getDescription(): String {
		var d = super.getDescription()

		if (this.level >= 2) {
			d += "\n" + lvl2
		}

		if (this.level >= 3) {
			d += "\n" + lvl3
		}

		if (this.level >= 4) {
			d += "\n" + lvl4
		}

		return d
	}

	override fun getMaxLevel(): Int {
		return 4
	}

	override fun onEquip(load: Boolean) {
		super.onEquip(load)
		this.owner.frostLevel += this.level.toInt()

		if (this.level >= 2) {
			this.owner.iceResitant += 1
		}

		if (this.level >= 3) {
			this.owner.lavaResist += 1
		}
	}

	override fun onUnequip(load: Boolean) {
		super.onUnequip(load)
		this.owner.frostLevel -= this.level.toInt()

		if (this.level >= 2) {
			this.owner.iceResitant -= 1
		}

		if (this.level >= 3) {
			this.owner.lavaResist -= 1
		}
	}

	companion object {

		private val lvl2 = Locale.get("ice_immunity")
		private val lvl3 = Locale.get("lava_immunity")
		private val lvl4 = Locale.get("lava_to_ice")
	}
}