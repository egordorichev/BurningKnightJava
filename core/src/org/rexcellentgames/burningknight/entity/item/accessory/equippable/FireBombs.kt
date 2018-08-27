package org.rexcellentgames.burningknight.entity.item.accessory.equippable

import org.rexcellentgames.burningknight.assets.Locale

class FireBombs : Equippable() {
	init {
		name = Locale.get("fire_bombs")
		description = Locale.get("fire_bombs_desc")
		sprite = "item-fire_bomb"
	}

	override fun onEquip(load: Boolean) {
		super.onEquip(load)
		this.owner.fireBombs = true
	}

	override fun onUnequip(load: Boolean) {
		super.onUnequip(load)
		this.owner.fireBombs = false
	}

	override fun canBeUpgraded(): Boolean {
		return false
	}
}