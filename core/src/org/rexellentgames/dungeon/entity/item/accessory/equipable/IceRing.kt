package org.rexellentgames.dungeon.entity.item.accessory.equipable

import org.rexellentgames.dungeon.assets.Locale
import org.rexellentgames.dungeon.entity.creature.player.Player

class IceRing : Equipable() {
	init {
		super.init()

		name = Locale.get("ice_ring")
		description = Locale.get("ice_ring_desc")
	}

	override fun onEquip() {
		super.onEquip()

		if (this.owner is Player) {
			(this.owner as Player).freezeChance += 5f
		}
	}

	override fun onUnequip() {
		super.onUnequip()

		if (this.owner is Player) {
			(this.owner as Player).freezeChance -= 5f
		}
	}
}