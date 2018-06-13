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
}