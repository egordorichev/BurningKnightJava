package org.rexcellentgames.burningknight.entity.item.accessory.equippable

import org.rexcellentgames.burningknight.assets.Locale

class ClockHeart : Equippable() {
	init {
		name = Locale.get("clock_heart")
		description = Locale.get("clock_heart_desc")
		sprite = "item-getting_damaged_slows_down_time"
	}

	override fun getMaxLevel(): Int {
		return 4
	}

	override fun getMinLevel(): Int {
		return -1
	}
}