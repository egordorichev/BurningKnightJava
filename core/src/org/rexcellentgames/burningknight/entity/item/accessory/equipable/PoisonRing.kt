package org.rexcellentgames.burningknight.entity.item.accessory.equipable

import org.rexcellentgames.burningknight.assets.Locale
import org.rexcellentgames.burningknight.entity.creature.player.Player

class PoisonRing : Equipable() {
	init {
		super.init()

	}

	override fun onEquip() {
		super.onEquip()

		if (this.owner is Player) {
			(this.owner as Player).poisonChance += 10f
		}
	}

	override fun onUnequip() {
		super.onUnequip()

		if (this.owner is Player) {
			(this.owner as Player).poisonChance -= 10f
		}
	}
}