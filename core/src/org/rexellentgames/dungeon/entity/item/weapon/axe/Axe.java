package org.rexellentgames.dungeon.entity.item.weapon.axe;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.item.weapon.Weapon;

public class Axe extends Weapon {
	{
		stackable = true;
		penetrates = true;
	}

	@Override
	public void use() {
		super.use();

		count -= 1;

		AxeFx fx = new AxeFx();

		fx.region = this.getSprite();
		fx.type = this.getClass();
		fx.x = this.owner.x + (this.owner.w - 16) / 2;
		fx.y = this.owner.y + (this.owner.h - 16) / 2;

		fx.owner = this.owner;
		fx.damage = this.damage;
		fx.penetrates = this.penetrates;

		Dungeon.area.add(fx);
	}
}