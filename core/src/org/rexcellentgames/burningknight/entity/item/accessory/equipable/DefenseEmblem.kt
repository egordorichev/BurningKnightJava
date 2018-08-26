package org.rexcellentgames.burningknight.entity.item.accessory.equipable

import org.rexcellentgames.burningknight.assets.Locale

class DefenseEmblem : Equipable() {

	// todo: inventory defense indicator
	private val mod: Float
		get() = 20 + this.level * 20f

	init {
		name = Locale.get("defense_emblem")
		description = Locale.get("defense_emblem_desc")
		sprite = "item-more_defense"
	}

	override fun onEquip(load: Boolean) {
		super.onEquip(load)

		this.owner.defenseModifier += mod / 100f
	}

	override fun onUnequip(load: Boolean) {
		super.onUnequip(load)
		this.owner.defenseModifier -= mod / 100f
	}

	override fun getMaxLevel(): Int {
		return 4
	}

	override fun getDescription(): String {
		return super.getDescription().replace("{PERCENT}", mod.toString())
	}
}