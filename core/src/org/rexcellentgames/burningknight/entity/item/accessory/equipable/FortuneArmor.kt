package org.rexcellentgames.burningknight.entity.item.accessory.equipable

import org.rexcellentgames.burningknight.assets.Locale

class FortuneArmor : Equipable() {
	val chance: Float
		get() = this.level * 5 + 45f

	init {
		name = Locale.get("fortune_armor")
		description = Locale.get("fortune_armor_desc")
		sprite = "item-defense_roll"
	}

	override fun onEquip(load: Boolean) {
		super.onEquip(load)
		this.owner.luckDefense = true
	}

	override fun onUnequip(load: Boolean) {
		super.onUnequip(load)
		this.owner.luckDefense = false
	}

	override fun getMaxLevel(): Int {
		return 15
	}

	override fun getDescription(): String {
		return super.getDescription().replace("{CHANCE}", chance.toInt().toString())
	}
}