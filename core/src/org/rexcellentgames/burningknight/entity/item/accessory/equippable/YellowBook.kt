package org.rexcellentgames.burningknight.entity.item.accessory.equippable

import org.rexcellentgames.burningknight.assets.Locale

class YellowBook : Equippable() {
	init {
		name = Locale.get("yellow_book")
		description = Locale.get("yellow_book_desc")
		sprite = "item-more_mana_less_damage"
	}

	override fun onEquip(load: Boolean) {
		super.onEquip(load)

		this.owner.modifyManaMax(6)
		this.owner.damageModifier -= 0.5f
	}

	override fun onUnequip(load: Boolean) {
		super.onUnequip(load)

		this.owner.modifyManaMax(-6)
		this.owner.damageModifier += 0.5f
	}

	override fun canBeUpgraded(): Boolean {
		return false
	}
}