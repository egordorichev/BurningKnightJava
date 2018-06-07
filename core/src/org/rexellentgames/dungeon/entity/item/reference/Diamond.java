package org.rexellentgames.dungeon.entity.item.reference;

import org.rexellentgames.dungeon.assets.Locale;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.item.Gold;
import org.rexellentgames.dungeon.entity.item.ItemHolder;
import org.rexellentgames.dungeon.entity.item.autouse.Autouse;

public class Diamond extends Autouse {
	{
		name = Locale.get("diamond");
		stackable = true;
		sprite = "item-diamond";
		autoPickup = true;
		useable = false;
		description = Locale.get("diamond_desc");
	}

	@Override
	public void use() {
		super.use();
		setCount(count - 1);

		ItemHolder item = new ItemHolder();
		item.setItem(new Gold());
		item.getItem().setCount(99);

		Player.instance.tryToPickup(item);
	}
}