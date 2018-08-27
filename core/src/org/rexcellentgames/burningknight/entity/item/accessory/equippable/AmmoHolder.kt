package org.rexcellentgames.burningknight.entity.item.accessory.equippable

class AmmoHolder : Equippable() {
	override fun onEquip(load: Boolean) {
		super.onEquip(load)
		this.owner.modifyStat("ammo_capacity", getChance() / 100f)
	}

	override fun onUnequip(load: Boolean) {
		super.onUnequip(load)
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