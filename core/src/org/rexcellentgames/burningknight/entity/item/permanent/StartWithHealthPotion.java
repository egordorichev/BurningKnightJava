package org.rexcellentgames.burningknight.entity.item.permanent;

public class StartWithHealthPotion extends PermanentUpgrade {
	public static String ID = "SHOP_START_WITH_HEALTH_POTION";

	{
		sprite = "upgrade-upg_potion";
	}

	@Override
	public String getUID() {
		return ID;
	}
}