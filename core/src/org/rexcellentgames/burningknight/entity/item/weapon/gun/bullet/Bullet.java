package org.rexcellentgames.burningknight.entity.item.weapon.gun.bullet;

import org.rexcellentgames.burningknight.entity.item.Item;

public class Bullet extends Item {
	public int damage;
	public String bulletName;

	{
		stackable = true;
		useable = false;
	}

	@Override
	public void render(float x, float y, float w, float h, boolean flipped) {
	}
}