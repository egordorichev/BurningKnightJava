package org.rexcellentgames.burningknight.entity.item.accessory.equippable

class OldManual : Equippable() {
	override fun onEquip(load: Boolean) {
		super.onEquip(load)
		this.owner.modifyStat("fire_on_reload", 1f)
	}

	override fun onUnequip(load: Boolean) {
		super.onUnequip(load)
		this.owner.modifyStat("fire_on_reload", -1f)
	}

	override fun canBeUpgraded(): Boolean {
		return false
	}
}