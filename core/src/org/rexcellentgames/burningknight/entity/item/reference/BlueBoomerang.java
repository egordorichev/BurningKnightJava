package org.rexcellentgames.burningknight.entity.item.reference;

import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.buff.FreezeBuff;
import org.rexcellentgames.burningknight.entity.item.weapon.axe.Axe;
import org.rexcellentgames.burningknight.util.Log;

public class BlueBoomerang extends Axe {
	{
		name = Locale.get("blue_boomerang");
		description = Locale.get("blue_boomerang_desc");
		damage = 10;
		speed = 1000;
		penetrates = true;
		sprite = "item-blue_boomerang";
	}

	@Override
	public void onHit(Creature creature) {
		super.onHit(creature);
		creature.addBuff(new FreezeBuff().setDuration(2));
	}
}