package org.rexcellentgames.burningknight.entity.item.consumable.scroll;

import org.rexcellentgames.burningknight.game.Achievements;

public class ScrollOfUpgrade extends Scroll {
	{
		sprite = "item-upgrade_scroll";
	}

	@Override
	public void use() {
		super.use();

		Achievements.unlock(Achievements.UPGRADE);
	}
}