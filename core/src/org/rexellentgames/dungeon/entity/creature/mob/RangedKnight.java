package org.rexellentgames.dungeon.entity.creature.mob;

import org.rexellentgames.dungeon.entity.item.weapon.gun.BadGun;

public class RangedKnight extends Knight {


	{
		hpMax = 5;
	}

	@Override
	public void init() {
		super.init();

		this.sword = new BadGun();
		this.sword.setOwner(this);
	}

	@Override
	public void destroy() {
		super.destroy();

		this.sword.destroy();
	}
}