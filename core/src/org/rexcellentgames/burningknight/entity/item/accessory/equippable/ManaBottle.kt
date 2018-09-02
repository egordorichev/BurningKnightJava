package org.rexcellentgames.burningknight.entity.item.accessory.equippable

import org.rexcellentgames.burningknight.assets.Locale

class ManaBottle : Equippable() {
	init {
		name = Locale.get("mana_bottle")
		description = Locale.get("mana_bottle_desc")
		sprite = "item-mana_bottle"
	}

	override fun onEquip(load: Boolean) {
		super.onEquip(load)
		this.owner.manaRegenRoom = true
	}

	override fun onUnequip(load: Boolean) {
		super.onUnequip(load)
		this.owner.manaRegenRoom = false
	}

	override fun canBeUpgraded(): Boolean {
		return false
	}

	override fun canBeDegraded(): Boolean {
		return false
	}
}