package org.rexcellentgames.burningknight.entity.item.accessory.equippable

import org.rexcellentgames.burningknight.entity.level.entities.chest.Chest
import org.rexcellentgames.burningknight.entity.level.entities.chest.Mimic

class MimicSummoner : Equippable() {
	init {
		sprite = "item-mimic_summoner"
	}

	override fun onEquip(load: Boolean) {
		super.onEquip(load)
		Mimic.chance += 100f

		for (chest in Chest.all) {
			chest.toMimic()
		}
	}

	override fun onUnequip(load: Boolean) {
		super.onUnequip(load)
		Mimic.chance -= 100f
	}
}