package org.rexcellentgames.burningknight.entity.item.accessory.equippable

import org.rexcellentgames.burningknight.entity.item.ItemHolder

class ShopSale : Equippable() {
	init {
		sprite = "item-shop_sale"
	}

	override fun onEquip(load: Boolean) {
		super.onEquip(load)
		this.owner.modifyStat("sale", 1f)

		for (item in ItemHolder.all) {
			if (item.item.shop) {
				item.sale()
			}
		}
	}

	override fun onUnequip(load: Boolean) {
		super.onUnequip(load)
		this.owner.modifyStat("sale", 1f)

		for (item in ItemHolder.all) {
			if (item.item.shop) {
				item.unSale()
			}
		}
	}

	override fun canBeUpgraded(): Boolean {
		return false
	}
}