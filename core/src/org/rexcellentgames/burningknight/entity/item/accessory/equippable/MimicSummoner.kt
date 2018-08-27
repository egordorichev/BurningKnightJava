package org.rexcellentgames.burningknight.entity.item.accessory.equippable

import org.rexcellentgames.burningknight.assets.Locale
import org.rexcellentgames.burningknight.entity.level.entities.chest.Chest
import org.rexcellentgames.burningknight.entity.level.entities.chest.Mimic
import org.rexcellentgames.burningknight.util.Random

class MimicSummoner : Equippable() {
	init {
		sprite = "item-mimic_summoner"
	}

	override fun onEquip(load: Boolean) {
		super.onEquip(load)
		Mimic.chance += getChance()

		for (chest in Chest.all) {
			if (Random.chance(Mimic.chance)) {
				chest.toMimic()
			}
		}
	}

	override fun onUnequip(load: Boolean) {
		super.onUnequip(load)
		Mimic.chance -= getChance()
	}

	override fun upgrade() {
		Mimic.chance -= getChance()
		level ++
		Mimic.chance += getChance()
	}

	fun getChance(): Float {
		return this.level * 20 + 40f
	}

	override fun getMaxLevel(): Int {
		return 3
	}

	override fun getDescription(): String {
		var d = super.getDescription().replace("{CHANCE}", getChance().toInt().toString())

		d = d.replace("{ALMOST}", if (getChance() < 100f) almost else "")
		return d;
	}

	companion object {
		val almost = " " + Locale.get("almost")
	}
}