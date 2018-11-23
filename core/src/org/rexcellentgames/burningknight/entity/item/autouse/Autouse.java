package org.rexcellentgames.burningknight.entity.item.autouse;

import org.rexcellentgames.burningknight.entity.item.consumable.Consumable;

public class Autouse extends Consumable {
	{
		useOnPickup = true;
	}

	/*@Override
	public void use() {
		super.use();

		UiBanner banner = new UiBanner();
		banner.text = this.name;
		banner.extra = this.description;
		Dungeon.ui.add(banner);
	}*/

	@Override
	public StringBuilder buildInfo() {
		StringBuilder builder = super.buildInfo();
		builder.append("\n[green]Consumable");

		return builder;
	}
}