package org.rexcellentgames.burningknight.entity.item.accessory.equippable

import org.rexcellentgames.burningknight.assets.Locale

class StopAndPlay : Equippable() {
	init {
		name = Locale.get("stop_and_play")
		description = Locale.get("stop_and_play_desc")
		sprite = "item-pause_and_play"
	}

	override fun onEquip(load: Boolean) {
		super.onEquip(load)
		this.owner.pauseMore = true
	}

	override fun onUnequip(load: Boolean) {
		super.onUnequip(load)
		this.owner.pauseMore = false
	}

	override fun canBeUpgraded(): Boolean {
		return false
	}
}