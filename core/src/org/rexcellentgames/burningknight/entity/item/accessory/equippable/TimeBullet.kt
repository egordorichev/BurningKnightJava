package org.rexcellentgames.burningknight.entity.item.accessory.equippable

class TimeBullet : Equippable() {
	override fun onEquip(load: Boolean) {
		super.onEquip(load)
		this.owner.modifyStat("slow_down_on_hit", 0.1f)
	}

	override fun onUnequip(load: Boolean) {
		super.onUnequip(load)
		this.owner.modifyStat("slow_down_on_hit", -0.1f)
	}

	override fun canBeUpgraded(): Boolean {
		return false
	}
}