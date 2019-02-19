package org.rexcellentgames.burningknight.entity.item.consumable;

import org.rexcellentgames.burningknight.entity.item.Item;

public class Consumable extends Item {
	@Override
	public void use() {
		if (!canBeUsed() || count == 0) {
			return;
		}

		setCount(Math.max(0, count - 1));

		if (count == 0) {
			done = true;
		}

		super.use();
	}
}