package org.rexcellentgames.burningknight.entity.item.accessory.equipable

import org.rexcellentgames.burningknight.assets.Locale

class DemageEmblem : Equipable() {
	init {
		name = Locale.get("demage_emblem")
		description = Locale.get("demage_emblem_desc")
		sprite = "item-more_defense_and_damage"
	}

	override fun onEquip(load: Boolean) {
		super.onEquip(load)
		this.owner.defenseModifier += getMod() / 100f
		this.owner.damageModifier += getMod() / 100f
	}

	override fun onUnequip(load: Boolean) {
		super.onUnequip(load)
		this.owner.defenseModifier -= getMod() / 100f
		this.owner.damageModifier -= getMod() / 100f
	}

	private fun getMod(): Float {
		return this.level * 20f
	}

	override fun getDescription(): String {
		return super.getDescription().replace("{PERCENT}", getMod().toInt().toString())
	}
}