package org.rexcellentgames.burningknight.entity.item.autouse;

import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.player.Player;

public class ManaHeart extends Autouse {
	{
	}

	@Override
	public void use() {
		super.use();

		setCount(count - 1);
		this.owner.setHpMax(this.owner.getHpMax() + 2);

		if (this.owner instanceof Player) {
			((Player) this.owner).modifyManaMax(2);
		}
	}
}