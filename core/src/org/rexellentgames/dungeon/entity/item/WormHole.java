package org.rexellentgames.dungeon.entity.item;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Locale;
import org.rexellentgames.dungeon.entity.creature.fx.WormholeFx;

public class WormHole extends Item {
	{
		name = Locale.get("worm_hole");
		description = Locale.get("worm_hole_desc");
		sprite = "item (scroll F)"; // Todo
		stackable = true;
	}

	@Override
	public void use() {
		super.use();

		WormholeFx fx = new WormholeFx();

		fx.x = (float) Math.floor(this.owner.x) + 8;
		fx.y = (float) Math.floor(this.owner.y) + 8;

		Dungeon.area.add(fx);
	}
}