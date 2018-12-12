package org.rexcellentgames.burningknight.entity.item.accessory.equippable

import org.rexcellentgames.burningknight.assets.Locale

class DemageEmblem : Equippable() {
	init {
		name = Locale.get("demage_emblem")
		description = Locale.get("demage_emblem_desc")
		sprite = "item-more_defense_and_damage"
	}

	override fun onEquip(load: Boolean) {
		super.onEquip(load)
		this.owner.modifyDefense(1)
		this.owner.defenseModifier += getMod() / 100f
		this.owner.damageModifier += getMod() / 100f
	}

	override fun onUnequip(load: Boolean) {
		super.onUnequip(load)
		this.owner.modifyDefense(-1)
		this.owner.defenseModifier -= getMod() / 100f
		this.owner.damageModifier -= getMod() / 100f
	}

	private fun getMod(): Float {
		return this.level * 20f
	}

	override fun getMaxLevel(): Int {
		return 10
	}

	override fun getDescription(): String {
		return super.getDescription().replace("{PERCENT}", getMod().toInt().toString())
	}
}