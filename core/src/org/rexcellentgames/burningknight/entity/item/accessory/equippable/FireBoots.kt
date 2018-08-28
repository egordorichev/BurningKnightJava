package org.rexcellentgames.burningknight.entity.item.accessory.equippable

import org.rexcellentgames.burningknight.assets.Locale

class FireBoots : Equippable() {

	override fun getDescription(): String {
		var d = super.getDescription()

		if (this.level >= 2) {
			d += "\n" + lvl2
		}

		if (this.level >= 3) {
			d += "\n" + lvl3
		}

		return d
	}

	override fun onEquip(load: Boolean) {
		super.onEquip(load)
		this.owner.burnLevel += this.level.toInt()

		if (this.level >= 2) {
			this.owner.fireResist += 1
		}

		if (this.level >= 3) {
			this.owner.lavaResist += 1
		}
	}

	override fun getMaxLevel(): Int {
		return 3
	}

	override fun onUnequip(load: Boolean) {
		super.onUnequip(load)
		this.owner.burnLevel -= this.level.toInt()

		if (this.level >= 2) {
			this.owner.fireResist -= 1
		}

		if (this.level >= 3) {
			this.owner.lavaResist -= 1
		}
	}

	companion object {
		private val lvl2 = Locale.get("fire_immunity")
		private val lvl3 = Locale.get("lava_immunity")
	}
}