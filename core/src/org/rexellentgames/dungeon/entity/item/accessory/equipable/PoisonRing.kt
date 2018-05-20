package org.rexellentgames.dungeon.entity.item.accessory.equipable

import org.rexellentgames.dungeon.entity.creature.player.Player

class PoisonRing : Equipable() {
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