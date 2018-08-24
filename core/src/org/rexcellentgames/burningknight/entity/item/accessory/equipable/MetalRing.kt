package org.rexcellentgames.burningknight.entity.item.accessory.equipable

import org.rexcellentgames.burningknight.assets.Locale
import org.rexcellentgames.burningknight.entity.creature.player.Player

class MetalRing : Equipable() {
	init {
		super.init()

		name = Locale.get("metal_ring")
		description = Locale.get("metal_ring_desc")
		sprite = "item-ring_d"
	}

	override fun onEquip() {
		super.onEquip()

		if (this.owner is Player) {
			(this.owner as Player).reflectDamageChance += 20f
		}
	}

	override fun onUnequip() {
		super.onUnequip()

		if (this.owner is Player) {
			(this.owner as Player).reflectDamageChance -= 20f
		}
	}
}