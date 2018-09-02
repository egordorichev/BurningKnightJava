package org.rexcellentgames.burningknight.entity.item.accessory.equippable

import org.rexcellentgames.burningknight.assets.Locale

class Wings : Equippable() {
	init {
		name = Locale.get("wings")
		description = Locale.get("wings_desc")
		sprite = "item-wings"
	}

	override fun onEquip(load: Boolean) {
		super.onEquip(load)
		this.owner.flight += 1
	}

	override fun onUnequip(load: Boolean) {
		super.onUnequip(load)
		this.owner.flight -= 1
	}

	override fun canBeUpgraded(): Boolean {
		return false
	}

	override fun canBeDegraded(): Boolean {
		return false
	}
}