package org.rexcellentgames.burningknight.entity.item.consumable.food;

import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.consumable.Consumable;

public class ManaInABottle extends Consumable {
	{
		name = Locale.get("mana_in_a_bottle");
		description = Locale.get("mana_in_a_bottle_desc");
		sprite = "item-mana_in_bottle";
	}

	@Override
	public void use() {
		super.use();
		if (this.owner instanceof Player) {
			((Player) this.owner).modifyMana(((Player) this.owner).getManaMax());
		}
	}
}