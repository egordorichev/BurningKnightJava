package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

import org.rexcellentgames.burningknight.entity.item.ItemHolder;

public class ShopSale extends Equipable {
	{
		sprite = "item-shop_sale";
	}

	@Override
	public void onEquip() {
		super.onEquip();
		this.owner.modifyStat("sale", 1);

		for (ItemHolder item : ItemHolder.all) {
			if (item.getItem().shop) {
				item.sale();
			}
		}
	}

	@Override
	public void onUnequip() {
		super.onUnequip();
		this.owner.modifyStat("sale", 1);

		for (ItemHolder item : ItemHolder.all) {
			if (item.getItem().shop) {
				item.unsale();
			}
		}
	}
}