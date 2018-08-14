package org.rexcellentgames.burningknight.entity.item;

import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.buff.BurningBuff;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.SlashSword;

public class BKSword extends SlashSword {
	{
		name = Locale.get("bk_sword");
		description = Locale.get("bk_sword_desc");
		sprite = "item-bk_sword";
		damage = 15;
		penetrates = true;
	}

	@Override
	public void onHit(Creature creature) {
		super.onHit(creature);

		creature.addBuff(new BurningBuff());
	}
}