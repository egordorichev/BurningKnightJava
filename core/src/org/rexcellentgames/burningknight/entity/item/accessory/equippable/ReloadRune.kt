package org.rexcellentgames.burningknight.entity.item.accessory.equippable

class ReloadRune : Equippable() {

	val time: Float
		get() = this.level.toFloat()

	init {
		sprite = "item-scroll_a"
	}

	override fun onEquip(load: Boolean) {
		super.onEquip(load)
		this.owner.modifyStat("reload_time", time)
	}

	override fun onUnequip(load: Boolean) {
		super.onUnequip(load)
		this.owner.modifyStat("reload_time", -time)
	}

	override fun getMaxLevel(): Int {
		return 2
	}
}