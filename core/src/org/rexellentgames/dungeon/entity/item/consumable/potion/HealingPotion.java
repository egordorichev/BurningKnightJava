package org.rexellentgames.dungeon.entity.item.consumable.potion;

import org.rexellentgames.dungeon.assets.Locale;

public class HealingPotion extends Potion {
	{
		name = Locale.get("healing_potion");
		description = Locale.get("healing_potion_desc");
	}

	@Override
	public void use() {
		if (this.owner.getHpMax() == this.owner.getHp()) {
			return;
		}

		super.use();
		this.owner.modifyHp(this.owner.getHpMax() - this.owner.getHp(), null);
	}
}