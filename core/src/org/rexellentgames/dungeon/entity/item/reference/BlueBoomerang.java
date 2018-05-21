package org.rexellentgames.dungeon.entity.item.reference;

import org.rexellentgames.dungeon.assets.Locale;
import org.rexellentgames.dungeon.entity.creature.Creature;
import org.rexellentgames.dungeon.entity.creature.buff.FreezeBuff;
import org.rexellentgames.dungeon.entity.item.weapon.axe.Axe;
import org.rexellentgames.dungeon.util.Log;

public class BlueBoomerang extends Axe {
	{
		name = Locale.get("blue_boomerang");
		description = Locale.get("blue_boomerang_desc");
		damage = 10;
		speed = 1000;
		penetrates = true;
	}

	@Override
	public void onHit(Creature creature) {
		super.onHit(creature);

		Log.info("Hit " + creature);

		creature.addBuff(new FreezeBuff().setDuration(2));
	}
}