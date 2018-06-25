package org.rexcellentgames.burningknight.entity.item.consumable.food;

import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.item.consumable.Consumable;

public class Apple extends Consumable {
	{
		name = Locale.get("apple");
		description = Locale.get("apple_desc");
		sprite = "item-apple";
	}

	@Override
	public void use() {
		super.use();
		setCount(count - 1);
		this.owner.modifyHp(this.owner.getHp(), null);
	}
}