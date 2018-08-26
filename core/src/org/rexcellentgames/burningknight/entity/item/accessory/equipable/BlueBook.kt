package org.rexcellentgames.burningknight.entity.item.accessory.equipable

import org.rexcellentgames.burningknight.assets.Locale

class BlueBook : Equipable() {
	init {
		name = Locale.get("blue_book")
		description = Locale.get("blue_book_desc")
		sprite = "item-less_mana_used_but_minus_hp_percent"
	}

	override fun onEquip(load: Boolean) {
		super.onEquip(load)
		this.owner.hpMax = this.owner.hpMax - 4
		this.owner.manaModifier -= 0.5f
	}

	override fun onUnequip(load: Boolean) {
		super.onUnequip(load)
		this.owner.hpMax = this.owner.hpMax + 4
		this.owner.manaModifier += 0.5f
	}

	override fun canBeUpgraded(): Boolean {
		return false
	}
}