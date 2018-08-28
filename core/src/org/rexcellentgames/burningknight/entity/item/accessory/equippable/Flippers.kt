package org.rexcellentgames.burningknight.entity.item.accessory.equippable

import org.rexcellentgames.burningknight.assets.Locale
import org.rexcellentgames.burningknight.entity.creature.player.Player

class Flippers : Equippable() {
	companion object {
		val lvl2 = Locale.get("lava_immunity")
	}

	override fun onEquip(load: Boolean) {
		super.onEquip(load)
		this.owner.slowLiquidResist += 1

		if (this.level >= 2) {
			(this.owner as Player).lavaResist += 1
		}
	}

	override fun onUnequip(load: Boolean) {
		super.onUnequip(load)
		this.owner.slowLiquidResist -= 1

		if (this.level >= 2) {
			(this.owner as Player).lavaResist -= 1
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