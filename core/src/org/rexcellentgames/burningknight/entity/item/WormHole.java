package org.rexcellentgames.burningknight.entity.item;

import org.rexcellentgames.burningknight.entity.creature.fx.WormholeFx;
import org.rexcellentgames.burningknight.entity.item.consumable.spell.Spell;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.fx.WormholeFx;
import org.rexcellentgames.burningknight.entity.item.consumable.spell.Spell;

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