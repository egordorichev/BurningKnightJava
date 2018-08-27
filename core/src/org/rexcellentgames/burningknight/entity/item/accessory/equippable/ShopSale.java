package org.rexcellentgames.burningknight.entity.item.accessory.equippable;

import org.rexcellentgames.burningknight.entity.item.ItemHolder;

public class ShopSale extends Equippable {
	{
		sprite = "item-shop_sale";
	}

	@Override
	public void onEquip(boolean load) {
		super.onEquip(load);
		this.owner.modifyStat("sale", 1);

		for (ItemHolder item : ItemHolder.all) {
			if (item.getItem().shop) {
				item.sale();
			}
		}
	}

	@Override
	public void onUnequip(boolean load) {
		super.onUnequip(load);
		this.owner.modifyStat("sale", 1);

		for (ItemHolder item : ItemHolder.all) {
			if (item.getItem().shop) {
				item.unsale();
			}
		}
	}
}