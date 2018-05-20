package org.rexellentgames.dungeon.entity.item.accessory.equipable

import org.rexellentgames.dungeon.entity.creature.player.Player

class MetalRing : Equipable() {
	override fun onEquip() {
		super.onEquip()

		if (this.owner is Player) {
			(this.owner as Player).reflectDamageChance += 10f
		}
	}

	override fun onUnequip() {
		super.onUnequip()

		if (this.owner is Player) {
			(this.owner as Player).reflectDamageChance -= 10f
		}
	}
}