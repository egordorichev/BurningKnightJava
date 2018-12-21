package org.rexcellentgames.burningknight.entity.item.active;

import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.item.Bomb;

public class InfiniteBomb extends Bomb {
	{
		sprite = "item-bomb_orbital";
		stackable = false;
		useTime = 0.4f;
		name = Locale.get("infinite_bomb");
		description = Locale.get("infinite_bomb_desc");
	}

	@Override
	public void use() {
		super.use();
		setCount(1);
	}
}