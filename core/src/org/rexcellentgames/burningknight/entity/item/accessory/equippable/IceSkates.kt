package org.rexcellentgames.burningknight.entity.item.accessory.equippable

class IceSkates : Equippable() {
	override fun onEquip(load: Boolean) {
		super.onEquip(load)
		this.owner.iceResitant += 1
	}

	override fun onUnequip(load: Boolean) {
		super.onUnequip(load)
		this.owner.iceResitant -= 1
	}

	override fun canBeUpgraded(): Boolean {
		return false
	}

	override fun canBeDegraded(): Boolean {
		return false
	}
}