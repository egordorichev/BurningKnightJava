package org.rexellentgames.dungeon.entity.item.reference;

import org.rexellentgames.dungeon.assets.Locale;
import org.rexellentgames.dungeon.entity.item.accessory.equipable.Equipable;

public class Switch extends Equipable {
	{
		description = Locale.get("switch_desc");
		name = Locale.get("switch");
		sprite = "item-switch";
	}
}