package org.rexcellentgames.burningknight.entity.item.permanent;

public class MoreGold extends PermanentUpgrade {
	public static String ID = "SHOP_MORE_GOLD";

	{
		sprite = "upgrade-upg_gold";
	}

	@Override
	public String getUID() {
		return ID;
	}
}