package org.rexcellentgames.burningknight.entity.item.accessory.equippable

import org.rexcellentgames.burningknight.assets.Locale
import org.rexcellentgames.burningknight.entity.creature.player.Player
import org.rexcellentgames.burningknight.entity.item.Item
import org.rexcellentgames.burningknight.entity.item.weapon.WeaponBase

class LuckRune : Equippable() {
	init {
		name = Locale.get("luck_rune")
		description = Locale.get("luck_rune_desc")
		sprite = "item-scroll_f"
	}

	override fun onEquip(load: Boolean) {
		super.onEquip(load)
		WeaponBase.luck = true

		for (i in 0 until Player.instance.inventory.size) {
			val item = Player.instance.inventory.getSlot(i)

			if (item is WeaponBase) {
				item.generateModifier()
			}
		}
	}

	override fun onUnequip(load: Boolean) {
		super.onUnequip(load)
		WeaponBase.luck = false
	}

	override fun canBeUpgraded(): Boolean {
		return false
	}

	override fun canBeDegraded(): Boolean {
		return false
	}
}