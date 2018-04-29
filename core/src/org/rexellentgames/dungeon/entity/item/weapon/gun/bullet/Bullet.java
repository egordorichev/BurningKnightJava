package org.rexellentgames.dungeon.entity.item.weapon.gun.bullet;

import org.rexellentgames.dungeon.entity.item.Item;
import org.rexellentgames.dungeon.util.geometry.Point;

public class Bullet extends Item {
	public int damage;
	public String bulletName;

	{
		identified = true;
		stackable = true;
		useable = false;
	}

	@Override
	public void render(float x, float y, float w, float h, boolean flipped) {
		
	}
}