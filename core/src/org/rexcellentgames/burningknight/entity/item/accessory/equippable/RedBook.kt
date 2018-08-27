package org.rexcellentgames.burningknight.entity.item.accessory.equippable

import org.rexcellentgames.burningknight.assets.Locale

class RedBook : Equippable() {

	private val regen: Float
		get() = (this.level * 4).toFloat()

	init {
		name = Locale.get("red_book")
		description = Locale.get("red_book_desc")
		sprite = "item-life_regen_becomes_mana_regen"
	}

	override fun onEquip(load: Boolean) {
		super.onEquip(load)
		this.owner.lifeRegenRegensMana = true
		this.owner.regen += this.regen
	}

	override fun onUnequip(load: Boolean) {
		super.onUnequip(load)
		this.owner.lifeRegenRegensMana = false
		this.owner.regen -= this.regen
	}

	override fun getMaxLevel(): Int {
		return 4
	}
}