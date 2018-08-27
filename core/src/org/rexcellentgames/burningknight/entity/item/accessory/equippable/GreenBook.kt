package org.rexcellentgames.burningknight.entity.item.accessory.equippable

import org.rexcellentgames.burningknight.assets.Locale

class GreenBook : Equippable() {
	init {
		name = Locale.get("green_book")
		description = Locale.get("green_book_desc")
		sprite = "item-less_mana_more_damage"
	}

	override fun onEquip(load: Boolean) {
		super.onEquip(load)
		this.owner.modifyManaMax(-4)
		this.owner.damageModifier += 1f
	}

	override fun onUnequip(load: Boolean) {
		super.onUnequip(load)
		this.owner.modifyManaMax(+4)
		this.owner.damageModifier -= 1f
	}

	override fun canBeUpgraded(): Boolean {
		return false
	}
}