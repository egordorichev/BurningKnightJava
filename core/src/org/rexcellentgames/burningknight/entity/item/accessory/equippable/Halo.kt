package org.rexcellentgames.burningknight.entity.item.accessory.equippable

import org.rexcellentgames.burningknight.assets.Locale

class Halo : Equippable() {
	init {
		name = Locale.get("halo")
		description = Locale.get("halo_desc")
		sprite = "item-halo"
	}

	override fun onEquip(load: Boolean) {
		super.onEquip(load)

		val full = this.owner.hp == this.owner.hpMax
		this.owner.hpMax = (this.owner.hpMax + getHearts() * 2).toInt()

		if (full) {
			this.owner.modifyHp(4, null)
		}
	}

	override fun onUnequip(load: Boolean) {
		super.onUnequip(load)

		this.owner.modifyMana(3)
		this.owner.damageModifier -= 1f
		this.owner.hpMax = (this.owner.hpMax - getHearts() * 2).toInt()
	}

	private fun getHearts(): Float {
		return this.level * 1f + 1f
	}

	override fun getMaxLevel(): Int {
		return 3
	}

	override fun getDescription(): String {
		return super.getDescription().replace("{AMOUNT}", getHearts().toInt().toString())
	}
}