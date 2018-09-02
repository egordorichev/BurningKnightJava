package org.rexcellentgames.burningknight.entity.item.accessory.equippable

import org.rexcellentgames.burningknight.entity.creature.player.Player

class TechEye : Equippable() {
	override fun onEquip(load: Boolean) {
		super.onEquip(load)
		Player.showStats = true
	}

	override fun onUnequip(load: Boolean) {
		super.onUnequip(load)
		Player.showStats = false
	}

	override fun canBeUpgraded(): Boolean {
		return false
	}

	override fun canBeDegraded(): Boolean {
		return false
	}
}