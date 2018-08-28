package org.rexcellentgames.burningknight.entity.item.accessory.equippable

import org.rexcellentgames.burningknight.assets.Locale

class FireExtinguisher : Equippable() {
	init {
		name = Locale.get("fire_extinguisher")
		description = Locale.get("fire_extinguisher_desc")
		sprite = "item-fire_extinguisher"
	}

	override fun onEquip(load: Boolean) {
		super.onEquip(load)
		this.owner.fireResist += 1
	}

	override fun onUnequip(load: Boolean) {
		super.onUnequip(load)
		this.owner.fireResist -= 1
	}

	override fun canBeUpgraded(): Boolean {
		return false
	}
}