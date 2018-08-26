package org.rexcellentgames.burningknight.entity.item.accessory.equipable

import org.rexcellentgames.burningknight.assets.Locale

class BlackHeart : Equipable() {
	init {
		name = Locale.get("black_heart")
		description = Locale.get("black_heart_desc")
		sprite = "item-getting_damaged_damages_everyone"
	}

	override fun getMaxLevel(): Int {
		return 6
	}

	fun getDamage(): Float {
		return this.level * 2 + 2f
	}

	override fun getDescription(): String {
		return super.getDescription().replace("{DAMAGE}", getDamage().toInt().toString())
	}
}