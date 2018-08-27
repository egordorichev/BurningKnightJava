package org.rexcellentgames.burningknight.entity.item.accessory.equipable

import org.rexcellentgames.burningknight.assets.Locale

class FireFlower : Equipable() {
	init {
		super.init()

		sprite = "item-fire_flower"
		name = Locale.get("fire_flower")
		description = Locale.get("fire_flower_desc")
	}

	override fun onEquip(load: Boolean) {
		super.onEquip(load)
		this.owner.burnChance += 100f
	}

	override fun onUnequip(load: Boolean) {
		super.onUnequip(load)
		this.owner.burnChance -= 100f
	}

	override fun canBeUpgraded(): Boolean {
		return false
	}
}