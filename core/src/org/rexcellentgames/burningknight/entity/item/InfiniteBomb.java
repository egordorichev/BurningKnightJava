package org.rexcellentgames.burningknight.entity.item;

import org.rexcellentgames.burningknight.assets.Locale;

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
		setCount(count + 1);
	}
}