package org.rexcellentgames.burningknight.entity.item.accessory.equippable

import org.rexcellentgames.burningknight.assets.Locale
import org.rexcellentgames.burningknight.entity.creature.mob.Mob

class ChallengeRune : Equippable() {
	init {
		name = Locale.get("challenge_rune")
		description = Locale.get("challenge_rune_desc")
		sprite = "item-scroll_d"
	}

	override fun onEquip(load: Boolean) {
		super.onEquip(load)
		Mob.challenge = true

		for (mob in Mob.all) {
			mob.generatePrefix()
		}

		this.owner.modifyDefense(2)
	}

	override fun onUnequip(load: Boolean) {
		super.onUnequip(load)
		Mob.challenge = false
		this.owner.modifyDefense(-2)
	}

	override fun canBeUpgraded(): Boolean {
		return false
	}

	override fun canBeDegraded(): Boolean {
		return false
	}
}