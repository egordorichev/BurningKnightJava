package org.rexcellentgames.burningknight.entity.item.accessory.equipable

class AmmoHolder : Equipable() {
	override fun onEquip() {
		super.onEquip()
		this.owner.modifyStat("ammo_capacity", getChance() / 100f)
	}

	override fun onUnequip() {
		super.onUnequip()
		this.owner.modifyStat("ammo_capacity", -getChance() / 100f)
	}

	private fun getChance(): Float {
		return 30f + this.level * 20
	}

	override fun getMaxLevel(): Int {
		return 6
	}

	override fun getDescription(): String {
		return super.getDescription().replace("{PERCENT}", getChance().toInt().toString())
	}
}