package org.rexcellentgames.burningknight.entity.item;

import org.rexcellentgames.burningknight.entity.item.entity.BombEntity;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.item.entity.BombEntity;

public class Bomb extends Item {
	{
		name = Locale.get("bomb");
		description = Locale.get("bomb_desc");
		sprite = "item (bomb)";
		useTime = 1f;
		stackable = true;
		identified = true;
	}

	@Override
	public void render(float x, float y, float w, float h, boolean flipped) {

	}

	@Override
	public void use() {
		super.use();
		this.count -= 1;

		BombEntity e = new BombEntity(this.owner.x + (this.owner.w - 16) / 2, this.owner.y + (this.owner.h - 16) / 2).toMouseVel();
		e.owner = this.owner;

		Dungeon.area.add(e);
	}
}