package org.rexellentgames.dungeon.entity.item.accessory.equipable

import org.rexellentgames.dungeon.assets.Locale
import org.rexellentgames.dungeon.entity.creature.player.Player

class FireRing : Equipable() {
	init {
		super.init()

		name = Locale.get("fire_ring")
		description = Locale.get("fire_ring_desc")
		sprite = "item (ring F)"
	}

	override fun onEquip() {
		super.onEquip()

		if (this.owner is Player) {
			(this.owner as Player).burnChance += 10f
		}
	}

	override fun onUnequip() {
		super.onUnequip()

		if (this.owner is Player) {
			(this.owner as Player).burnChance -= 10f
		}
	}
}