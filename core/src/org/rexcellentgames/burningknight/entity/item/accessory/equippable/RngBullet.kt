package org.rexcellentgames.burningknight.entity.item.accessory.equippable

class RngBullet : Equippable() {
	val chance: Float
		get() = this.level * 10f

	override fun onEquip(load: Boolean) {
		super.onEquip(load)
		this.owner.modifyStat("restore_ammo_chance", chance / 100f)
	}

	override fun onUnequip(load: Boolean) {
		super.onUnequip(load)
		this.owner.modifyStat("restore_ammo_chance", -chance / 100f)
	}

	override fun getMaxLevel(): Int {
		return 4
	}

	override fun getDescription(): String {
		return super.getDescription().replace("{CHANCE}", chance.toInt().toString())
	}
}