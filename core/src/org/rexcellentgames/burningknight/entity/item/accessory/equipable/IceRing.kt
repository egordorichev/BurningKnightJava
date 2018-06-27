package org.rexcellentgames.burningknight.entity.item.accessory.equipable

import org.rexcellentgames.burningknight.assets.Locale
import org.rexcellentgames.burningknight.entity.creature.player.Player

class IceRing : Equipable() {
	init {
		super.init()

		name = Locale.get("ice_ring")
		description = Locale.get("ice_ring_desc")
		sprite = "item-ring_e"
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