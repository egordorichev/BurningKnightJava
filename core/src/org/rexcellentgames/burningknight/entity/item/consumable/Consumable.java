package org.rexcellentgames.burningknight.entity.item.consumable;

import org.rexcellentgames.burningknight.entity.item.Item;

public class Consumable extends Item {
	{
		stackable = true;
	}

	@Override
	public StringBuilder buildInfo() {
		StringBuilder builder = super.buildInfo();
		builder.append("\n[green]Consumable");

		return builder;
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