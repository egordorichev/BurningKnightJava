package org.rexcellentgames.burningknight.entity.item.accessory.equippable

import org.rexcellentgames.burningknight.assets.Locale

class BlueCoin : Equippable() {
	init {
		name = Locale.get("blue_coin")
		description = Locale.get("blue_coin_desc")
		sprite = "item-picking_up_coins_gives_mana"
	}

	override fun canBeUpgraded(): Boolean {
		return false
	}

	override fun onEquip(load: Boolean) {
		super.onEquip(load)
		this.owner.manaCoins = true
	}

	override fun onUnequip(load: Boolean) {
		super.onUnequip(load)
		this.owner.manaCoins = false
	}

	override fun canBeDegraded(): Boolean {
		return false
	}
}