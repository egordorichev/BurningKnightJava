package org.rexcellentgames.burningknight.entity.creature.npc;

import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.item.Item;

public class Trader extends Npc {
	@Override
	public void render() {
		Graphics.render(Item.missing, this.x, this.y);
	}
}