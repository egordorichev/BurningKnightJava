package org.rexcellentgames.burningknight.entity.item.consumable;

import org.rexcellentgames.burningknight.entity.item.Item;

public class Consumable extends Item {
	{
		stackable = true;
	}


	@Override
	public void use() {
		super.use();

		if (this.canBeConsumed()) {
			this.setCount(this.count - 1);
		}
	}

	protected boolean canBeConsumed() {
		return true;
	}
}