package org.rexcellentgames.burningknight.entity.item.permanent;

public class BetterChestChance extends PermanentUpgrade {
	public static String ID = "SHOP_BETTER_CHEST_CHANCE";

	{
		sprite = "upgrade-upg_chests";
	}

	@Override
	public String getUID() {
		return ID;
	}
}