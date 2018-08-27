package org.rexcellentgames.burningknight.entity.item.accessory.equipable

import org.rexcellentgames.burningknight.assets.Locale

class LuckyCube : Equipable() {
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

	override fun getDescription(): String {
		return super.getDescription().replace("{CHANCE}", chance.toInt().toString())
	}
}