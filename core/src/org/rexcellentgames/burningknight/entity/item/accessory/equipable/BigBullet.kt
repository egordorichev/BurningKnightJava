package org.rexcellentgames.burningknight.entity.item.accessory.equipable

class BigBullet : Equipable() {
	init {
		sprite = "item-large_bullet"
	}

	override fun onEquip(load: Boolean) {
		super.onEquip(load)
		this.owner.modifyStat("gun_use_time", -0.5f)
		this.owner.modifyStat("damage", getChance() / 100)
	}

	override fun onUnequip(load: Boolean) {
		super.onUnequip(load)
		this.owner.modifyStat("gun_use_time", 0.5f)
		this.owner.modifyStat("damage", -getChance() / 100)
	}

	private fun getChance(): Float {
		return 10f + this.level * 20
	}

	override fun getMaxLevel(): Int {
		return 7
	}

	override fun getDescription(): String {
		return super.getDescription().replace("{PERCENT}", getChance().toInt().toString())
	}
}