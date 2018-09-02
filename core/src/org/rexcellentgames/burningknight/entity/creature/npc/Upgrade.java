package org.rexcellentgames.burningknight.entity.creature.npc;

import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.level.SaveableEntity;

public class Upgrade extends SaveableEntity {
	{
		depth = -1;
	}

	@Override
	public void render() {
		Graphics.render(Item.missing, this.x, this.y);
	}
}