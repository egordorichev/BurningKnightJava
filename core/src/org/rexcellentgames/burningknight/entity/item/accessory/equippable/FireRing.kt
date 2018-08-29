package org.rexcellentgames.burningknight.entity.item.accessory.equippable

import org.rexcellentgames.burningknight.assets.Locale
import org.rexcellentgames.burningknight.entity.creature.player.Player

class FireRing : Equippable() {
	init {
		super.init()

		name = Locale.get("fire_ring")
		description = Locale.get("fire_ring_desc")
		sprite = "item-ring_f"
	}

	override fun getMaxLevel(): Int {
		return 9
	}

	override fun onEquip(load: Boolean) {
		super.onEquip(load)

		if (this.owner is Player) {
			(this.owner as Player).burnChance += getChance()
		}
	}

	private fun getChance(): Float {
		return  10f + this.level * 10
	}

	override fun getDescription(): String {
		return super.getDescription().replace("{CHANCE}", getChance().toInt().toString())
	}

	override fun onUnequip(load: Boolean) {
		super.onUnequip(load)

		if (this.owner is Player) {
			(this.owner as Player).burnChance -= getChance()
		}
	}
}