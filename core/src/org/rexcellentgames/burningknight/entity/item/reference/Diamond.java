package org.rexcellentgames.burningknight.entity.item.reference;

import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.Gold;
import org.rexcellentgames.burningknight.entity.item.ItemHolder;
import org.rexcellentgames.burningknight.entity.item.autouse.Autouse;

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