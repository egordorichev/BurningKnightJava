package org.rexellentgames.dungeon.entity.item.accessory.equipable

import org.rexellentgames.dungeon.assets.Locale
import org.rexellentgames.dungeon.entity.creature.player.Player

class FireFlower : Equipable() {
	init {
		super.init()

		name = Locale.get("fire_flower")
		description = Locale.get("fire_flower_desc")
	}

	override fun onEquip() {
		super.onEquip()

		if (this.owner is Player) {
			(this.owner as Player).burnChance += 100f
		}
	}

	override fun onUnequip() {
		super.onUnequip()

		if (this.owner is Player) {
			(this.owner as Player).burnChance -= 100f
		}
	}
}