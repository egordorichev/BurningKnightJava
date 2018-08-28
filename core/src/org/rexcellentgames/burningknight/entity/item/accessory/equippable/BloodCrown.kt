package org.rexcellentgames.burningknight.entity.item.accessory.equippable

import org.rexcellentgames.burningknight.assets.Locale
import org.rexcellentgames.burningknight.entity.creature.player.Player

class BloodCrown : Equippable() {
	init {
		name = Locale.get("blood_crown")
		description = Locale.get("blood_crown_desc")
		sprite = "item-blood_crown"
	}

	override fun onEquip(load: Boolean) {
		super.onEquip(load)
		Player.mobSpawnModifier = getModifier()
	}

	override fun onUnequip(load: Boolean) {
		super.onUnequip(load)

		if (!load) {
			Player.mobSpawnModifier = 1f
		}
	}

	override fun getMaxLevel(): Int {
		return 7
	}

	fun getModifier(): Float {
		return (this.level - 1f) * 0.5f + 2f
	}

	override fun getDescription(): String {
		return super.getDescription().replace("{MODIFIER}", (if (getModifier() % 1 == 0.5f) getModifier().toInt() else getModifier().toInt()).toString())
	}
}