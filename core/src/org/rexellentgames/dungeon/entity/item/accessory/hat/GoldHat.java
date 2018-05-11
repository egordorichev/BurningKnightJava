package org.rexellentgames.dungeon.entity.item.accessory.hat;

import org.rexellentgames.dungeon.assets.Locale;

public class GoldHat extends Hat {
	{
		skin = "-gold";
		sprite = "item (hat_gold)";
		name = Locale.get("gold_hat");
		description = Locale.get("gold_hat_desc");
		defense = 2;
	}
}