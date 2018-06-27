package org.rexcellentgames.burningknight.entity.item.reference;

import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.Item;

public class Star extends Item {
	{
		useTime = 30f;
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