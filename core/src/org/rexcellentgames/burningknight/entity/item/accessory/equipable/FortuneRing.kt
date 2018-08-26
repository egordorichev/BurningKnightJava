package org.rexcellentgames.burningknight.entity.item.accessory.equipable

import org.rexcellentgames.burningknight.assets.Locale

class FortuneRing : Equipable() {
	init {
		description = Locale.get("fortune_ring_desc")
		name = Locale.get("fortune_ring")
		sprite = "item-ring_i"
	}

	override fun onEquip(load: Boolean) {
		super.onEquip(load)
		this.owner.modifyStat("crit_chance", getChance() / 100f)
	}

	override fun onUnequip(load: Boolean) {
		super.onUnequip(load)
		this.owner.modifyStat("crit_chance", -getChance() / 100f)
	}

	private fun getChance(): Float {
		return 30f + this.level * 10
	}

	override fun getDescription(): String {
		return super.getDescription().replace("{CHANCE}", getChance().toInt().toString())
	}
}