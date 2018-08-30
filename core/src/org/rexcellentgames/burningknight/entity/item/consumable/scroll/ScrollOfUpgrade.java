package org.rexcellentgames.burningknight.entity.item.consumable.scroll;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.fx.CurseFx;
import org.rexcellentgames.burningknight.entity.fx.UpgradeFx;
import org.rexcellentgames.burningknight.game.Achievements;
import org.rexcellentgames.burningknight.util.Random;

public class ScrollOfUpgrade extends Scroll {
	{
		sprite = "item-upgrade_scroll";
		useTime = 0f;
	}

	public boolean wasCursed;

	@Override
	public void use() {
		super.use();

		Achievements.unlock(Achievements.UPGRADE);

		for (int i = 0; i < 10; i++) {
			UpgradeFx fx = new UpgradeFx();

			fx.x = Random.newFloat(this.owner.w) + this.owner.x;
			fx.y = Random.newFloat(this.owner.h) + this.owner.y + this.owner.h / 2;

			Dungeon.area.add(fx);
		}

		if (this.wasCursed) {
			this.wasCursed = false;
			for (int i = 0; i < 10; i++) {
				CurseFx fx = new CurseFx();

				fx.x = Random.newFloat(this.owner.w) + this.owner.x;
				fx.y = Random.newFloat(this.owner.h) + this.owner.y;

				Dungeon.area.add(fx);
			}
		}
	}

	@Override
	public boolean canBeUsed() {
		return false;
	}
}