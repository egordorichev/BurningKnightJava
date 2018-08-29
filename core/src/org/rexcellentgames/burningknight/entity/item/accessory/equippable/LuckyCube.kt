package org.rexcellentgames.burningknight.entity.item.accessory.equippable

import org.rexcellentgames.burningknight.assets.Locale

class LuckyCube : Equippable() {
	val chance: Float
		get() = this.level * 5f + 45f

	init {
		name = Locale.get("lucky_cube")
		description = Locale.get("lucky_cube_desc")
		sprite = "item-damage_roll"
	}

	override fun onEquip(load: Boolean) {
		super.onEquip(load)
		this.owner.luckDamage = true
	}

	override fun onUnequip(load: Boolean) {
		super.onUnequip(load)
		this.owner.luckDamage = false
	}

	override fun getMaxLevel(): Int {
		return 15
	}

	override fun getDescription(): String {
		return super.getDescription().replace("{CHANCE}", chance.toInt().toString())
	}
}