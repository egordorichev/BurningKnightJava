package org.rexcellentgames.burningknight.entity.item.accessory.equippable

import org.rexcellentgames.burningknight.assets.Locale

class BlueBomb : Equippable() {
	init {
		name = Locale.get("blue_bomb")
		description = Locale.get("blue_bomb_desc")
		sprite = "item-mana_bombs"
	}

	override fun onEquip(load: Boolean) {
		super.onEquip(load)

		this.owner.manaBombs = true
		this.owner.manaRegenRate += 0.5f
	}

	override fun onUnequip(load: Boolean) {
		super.onUnequip(load)

		this.owner.manaBombs = false
		this.owner.manaRegenRate -= 0.5f
	}

	override fun canBeUpgraded(): Boolean {
		return false
	}
}