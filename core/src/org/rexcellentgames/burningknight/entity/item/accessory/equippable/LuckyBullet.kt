package org.rexcellentgames.burningknight.entity.item.accessory.equippable

class LuckyBullet : Equippable() {

	val chance: Float
		get() = this.level * 10f

	override fun onEquip(load: Boolean) {
		super.onEquip(load)
		this.owner.modifyStat("ammo_save_chance", chance / 100f)
	}

	override fun onUnequip(load: Boolean) {
		super.onUnequip(load)
		this.owner.modifyStat("ammo_save_chance", -chance / 100f)
	}

	override fun getDescription(): String {
		return super.getDescription().replace("{PERCENT}", chance)
	}
}