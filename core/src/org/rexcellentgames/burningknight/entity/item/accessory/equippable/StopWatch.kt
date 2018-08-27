package org.rexcellentgames.burningknight.entity.item.accessory.equippable

import org.rexcellentgames.burningknight.assets.Locale
import org.rexcellentgames.burningknight.entity.creature.mob.Mob

class StopWatch : Equippable() {

	val speed: Float
		get() = this.level * 0.2f + 0.3f

	init {
		name = Locale.get("stopwatch")
		description = Locale.get("stopwatch_desc")
		sprite = "item-stop_watch"
	}

	override fun onEquip(load: Boolean) {
		super.onEquip(load)
		Mob.speedMod -= speed
	}

	override fun onUnequip(load: Boolean) {
		super.onUnequip(load)
		Mob.speedMod += speed
	}

	override fun getMaxLevel(): Int {
		return 3
	}
}