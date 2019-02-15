package org.rexcellentgames.burningknight.entity.item.reference;

import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.Item;

public class Star extends Item {
	{
		useTime = 30f;
		sprite = "item-star";
		name = Locale.get("star");
		description = Locale.get("star_desc");
	}

	@Override
	public void use() {
		super.use();

		this.owner.setInvt(5f);
		if (this.owner instanceof Player) {
			((Player) this.owner).drawInvt = true;
		}
	}
}