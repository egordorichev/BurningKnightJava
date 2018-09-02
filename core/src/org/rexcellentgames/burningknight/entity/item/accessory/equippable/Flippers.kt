package org.rexcellentgames.burningknight.entity.item.accessory.equippable

import org.rexcellentgames.burningknight.assets.Locale

class Flippers : Equippable() {
	companion object {
		val lvl2 = Locale.get("lava_immunity")
	}

	override fun onEquip(load: Boolean) {
		super.onEquip(load)
		this.owner.slowLiquidResist += 1

		if (this.level >= 2) {
			this.owner.lavaResist += 1
		}
	}

	override fun onUnequip(load: Boolean) {
		super.onUnequip(load)
		this.owner.slowLiquidResist -= 1

		if (this.level >= 2) {
			this.owner.lavaResist -= 1
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

	override fun canBeDegraded(): Boolean {
		return false
	}
}