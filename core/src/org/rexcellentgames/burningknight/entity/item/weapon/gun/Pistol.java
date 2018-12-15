package org.rexcellentgames.burningknight.entity.item.weapon.gun;

public class Pistol extends Gun {
	{
		sprite = "item-gun_g";
		origin.x = 4;
		origin.y = 5;
		hole.x = 10;
		hole.y = 6;
		damage = 1;
		useTime = 0.2f;
	}

	@Override
	protected String getSfx() {
		return "gun_5";
	}
}