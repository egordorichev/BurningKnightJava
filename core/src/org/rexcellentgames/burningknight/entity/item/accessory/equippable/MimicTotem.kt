package org.rexcellentgames.burningknight.entity.item.accessory.equippable

import org.rexcellentgames.burningknight.assets.Locale
import org.rexcellentgames.burningknight.entity.level.entities.chest.Mimic

class MimicTotem : Equippable() {
	init {
		sprite = "item-mimic_totem"
	}

	override fun onEquip(load: Boolean) {
		super.onEquip(load)
		Mimic.chance -= getChance() * 0.2f

		for (mimic in Mimic.all) {
			mimic.toChest()
		}
	}

	override fun upgrade() {
		Mimic.chance += getChance() * 0.2f
		level ++
		Mimic.chance -= getChance() * 0.2f
	}

	override fun onUnequip(load: Boolean) {
		super.onUnequip(load)
		Mimic.chance += getChance() * 0.2f
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
		return d
	}

	companion object {
		val almost = " (" + Locale.get("almost") + ")"
	}
}