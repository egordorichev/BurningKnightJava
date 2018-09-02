package org.rexcellentgames.burningknight.entity.item.accessory.equippable

class ElementalRing : Equippable() {
	override fun onEquip(load: Boolean) {
		super.onEquip(load)
		this.owner.lavaResist += 1
		this.owner.poisonResist += 1
		this.owner.stunResist += 1
		this.owner.fireResist += 1
	}

	override fun onUnequip(load: Boolean) {
		super.onUnequip(load)
		this.owner.lavaResist -= 1
		this.owner.poisonResist -= 1
		this.owner.stunResist -= 1
		this.owner.fireResist -= 1
	}

	override fun canBeUpgraded(): Boolean {
		return false
	}

	override fun canBeDegraded(): Boolean {
		return false
	}
}