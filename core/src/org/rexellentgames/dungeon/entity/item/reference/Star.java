package org.rexellentgames.dungeon.entity.item.reference;

import org.rexellentgames.dungeon.assets.Locale;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.item.Item;

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

	@Override
	public void render(float x, float y, float w, float h, boolean flipped) {

	}
}