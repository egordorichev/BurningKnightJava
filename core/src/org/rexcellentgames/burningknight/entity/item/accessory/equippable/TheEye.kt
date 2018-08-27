package org.rexcellentgames.burningknight.entity.item.accessory.equippable

import org.rexcellentgames.burningknight.assets.Locale
import org.rexcellentgames.burningknight.entity.creature.mob.Mob

class TheEye : Equippable() {

	val speed: Float
		get() = this.level * 0.1f + 0.3f

	init {
		name = Locale.get("the_eye")
		description = Locale.get("the_eye_desc")
		sprite = "item-the_eye"
	}

	override fun onEquip(load: Boolean) {
		super.onEquip(load)
		Mob.shotSpeedMod -= speed
	}

	override fun onUnequip(load: Boolean) {
		super.onUnequip(load)
		Mob.shotSpeedMod += speed
	}

	override fun getMaxLevel(): Int {
		return 4
	}
}