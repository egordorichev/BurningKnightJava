package org.rexcellentgames.burningknight.entity.item.accessory.equipable

import org.rexcellentgames.burningknight.assets.Locale

class CursedAim : Equipable() {
	init {
		name = Locale.get("cursed_aim")
		description = Locale.get("cursed_aim_desc")
		sprite = "item-cursed_aim"
	}

	override fun onEquip(load: Boolean) {
		super.onEquip(load)
		this.owner.accuracy -= 5f * this.level
		this.owner.damageModifier += 0.5f * this.level
	}

	override fun onUnequip(load: Boolean) {
		super.onUnequip(load)
		this.owner.accuracy += 5f * this.level
		this.owner.damageModifier -= 0.5f * this.level
	}

	override fun getMaxLevel(): Int {
		return 4
	}
}