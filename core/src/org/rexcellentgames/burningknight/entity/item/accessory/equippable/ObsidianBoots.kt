package org.rexcellentgames.burningknight.entity.item.accessory.equippable

import org.rexcellentgames.burningknight.assets.Locale

class ObsidianBoots : Equippable() {
	init {
		name = Locale.get("obsidian_boots")
		description = Locale.get("obsidian_boots_desc")
		sprite = "item-obsidian_boots"
	}

	override fun onEquip(load: Boolean) {
		super.onEquip(load)
		this.owner.lavaResist += 1
	}

	override fun onUnequip(load: Boolean) {
		super.onUnequip(load)
		this.owner.lavaResist -= 1
	}

	override fun canBeUpgraded(): Boolean {
		return false
	}

	override fun canBeDegraded(): Boolean {
		return false
	}
}