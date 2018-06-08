package org.rexellentgames.dungeon.entity.item.weapon.sword;

import org.rexellentgames.dungeon.assets.Locale;
import org.rexellentgames.dungeon.entity.creature.Creature;
import org.rexellentgames.dungeon.entity.creature.buff.FreezeBuff;

public class SwordA extends Sword {
	{
		name = Locale.get("sword_a");
		description = Locale.get("sword_a_desc");
		sprite = "item (sword A)";
		damage = 4;
		useTime = 0.4f;
	}

	@Override
	public String getSfx() {
		return "sword_2";
	}

	@Override
	public void onHit(Creature creature) {
		super.onHit(creature);

		this.owner.addBuff(new FreezeBuff());
		creature.addBuff(new FreezeBuff());
	}
}