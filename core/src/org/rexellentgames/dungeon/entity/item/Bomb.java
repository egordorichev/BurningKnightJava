package org.rexellentgames.dungeon.entity.item;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.item.entity.BombEntity;

public class Bomb extends Item {
	{
		name = "Bomb";
		description = "Don't press that button @_@";
		sprite = 6;
		useTime = 1f;
		stackable = true;
	}

	@Override
	public void render(float x, float y, boolean flipped) {

	}

	@Override
	public void use() {
		super.use();
		this.count -= 1;

		Dungeon.area.add(new BombEntity(this.owner.x + this.owner.w / 2, this.owner.y + this.owner.h / 2));
	}
}