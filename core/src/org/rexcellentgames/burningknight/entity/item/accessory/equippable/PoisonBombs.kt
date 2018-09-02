package org.rexcellentgames.burningknight.entity.item.accessory.equippable

import org.rexcellentgames.burningknight.assets.Locale

class PoisonBombs : Equippable() {
	init {
		name = Locale.get("poison_bombs")
		description = Locale.get("poison_bombs_desc")
		sprite = "item-poison_bomb"
	}

	override fun onEquip(load: Boolean) {
		super.onEquip(load)
		this.owner.poisonBombs = true
	}

	override fun onUnequip(load: Boolean) {
		super.onUnequip(load)
		this.owner.poisonBombs = false
	}

	override fun canBeUpgraded(): Boolean {
		return false
	}

	override fun canBeDegraded(): Boolean {
		return false
	}
}