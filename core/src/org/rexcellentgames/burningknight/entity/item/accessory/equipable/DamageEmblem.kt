package org.rexcellentgames.burningknight.entity.item.accessory.equipable

import org.rexcellentgames.burningknight.assets.Locale

class DamageEmblem : Equipable() {
	private val mod: Float
		get() = (20 + this.level * 20).toFloat()

	init {
		name = Locale.get("damage_emblem")
		description = Locale.get("damage_emblem_desc")
		sprite = "item-more_damage"
	}

	override fun onEquip(load: Boolean) {
		super.onEquip(load)
		this.owner.damageModifier += mod / 100f
	}

	override fun onUnequip(load: Boolean) {
		super.onUnequip(load)
		this.owner.damageModifier -= mod / 100f
	}

	override fun getMaxLevel(): Int {
		return 4
	}

	override fun getDescription(): String {
		return super.getDescription().replace("{PERCENT}", mod.toString())
	}
}