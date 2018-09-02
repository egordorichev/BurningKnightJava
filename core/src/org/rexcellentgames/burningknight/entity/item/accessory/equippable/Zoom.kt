package org.rexcellentgames.burningknight.entity.item.accessory.equippable

import org.rexcellentgames.burningknight.entity.creature.player.Player

class Zoom : Equippable() {
	override fun onEquip(load: Boolean) {
		super.onEquip(load)
		Player.seeMore = true
	}

	override fun onUnequip(load: Boolean) {
		super.onUnequip(load)
		Player.seeMore = false
	}

	override fun canBeUpgraded(): Boolean {
		return false
	}

	override fun canBeDegraded(): Boolean {
		return false
	}
}