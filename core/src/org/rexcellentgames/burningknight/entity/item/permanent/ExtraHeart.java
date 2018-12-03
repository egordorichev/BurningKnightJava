package org.rexcellentgames.burningknight.entity.item.permanent;

public class ExtraHeart extends PermanentUpgrade {
	public static String ID = "SHOP_EXTRA_HEART";

	{
		sprite = "upgrade-upg_health";
	}

	@Override
	public String getUID() {
		return ID;
	}
}