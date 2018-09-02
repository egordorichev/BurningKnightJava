package org.rexcellentgames.burningknight.entity.item.accessory.equippable

class Scissors : Equippable() {
	override fun onEquip(load: Boolean) {
		super.onEquip(load)
		this.owner.cutCobweb = true
	}

	override fun onUnequip(load: Boolean) {
		super.onUnequip(load)
		this.owner.cutCobweb = false
	}

	override fun canBeUpgraded(): Boolean {
		return false
	}

	override fun canBeDegraded(): Boolean {
		return false
	}
}