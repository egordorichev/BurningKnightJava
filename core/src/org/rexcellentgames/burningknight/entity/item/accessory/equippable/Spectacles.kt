package org.rexcellentgames.burningknight.entity.item.accessory.equippable

import org.rexcellentgames.burningknight.assets.Locale
import org.rexcellentgames.burningknight.entity.creature.player.Player

class Spectacles : Equippable() {
	init {
		name = Locale.get("spectacles")
		description = Locale.get("spectacles_desc")
		sprite = "item-spectacles"
	}

	override fun onEquip(load: Boolean) {
		super.onEquip(load)

		if (this.owner is Player) {
			(this.owner as Player).seeSecrets = true
		}
	}

	override fun onUnequip(load: Boolean) {
		super.onUnequip(load)

		if (this.owner is Player) {
			(this.owner as Player).seeSecrets = false
		}
	}

	override fun canBeUpgraded(): Boolean {
		return false
	}
}