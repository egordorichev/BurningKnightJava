package org.rexellentgames.dungeon.entity.item;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Locale;
import org.rexellentgames.dungeon.entity.creature.fx.WormholeFx;
import org.rexellentgames.dungeon.entity.item.consumable.spell.Spell;

public class WormHole extends Spell {
	{
		name = Locale.get("worm_hole");
		description = Locale.get("worm_hole_desc");
		stackable = true;
	}

	@Override
	public void use() {
		super.use();

		WormholeFx fx = new WormholeFx();

		fx.x = this.owner.x;
		fx.y = this.owner.y;

		Dungeon.area.add(fx);
	}
}