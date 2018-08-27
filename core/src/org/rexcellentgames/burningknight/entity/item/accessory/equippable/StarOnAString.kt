package org.rexcellentgames.burningknight.entity.item.accessory.equippable

import org.rexcellentgames.burningknight.assets.Locale

class StarOnAString : Equippable() {
	val stars: Float
		get() = this.level.toFloat()

	init {
		name = Locale.get("star_on_a_string")
		description = Locale.get("star_on_a_string_desc")
		sprite = "item-mana_up"
	}

	override fun onEquip(load: Boolean) {
		super.onEquip(load)

		this.owner.modifyManaMax((stars * 2).toInt())
	}

	override fun onUnequip(load: Boolean) {
		super.onUnequip(load)

		this.owner.modifyManaMax((-stars * 2).toInt())
	}

	override fun getMaxLevel(): Int {
		return 4
	}

	override fun getDescription(): String {
		return super.getDescription().replace("{MANA}", stars.toInt().toString())
	}
}