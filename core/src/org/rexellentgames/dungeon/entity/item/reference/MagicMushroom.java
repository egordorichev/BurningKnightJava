package org.rexellentgames.dungeon.entity.item.reference;

import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.assets.Locale;
import org.rexellentgames.dungeon.entity.item.consumable.Consumable;

public class MagicMushroom extends Consumable {
	{
		name = Locale.get("magic_mushroom");
		description = Locale.get("magic_mushroom_desc");
	}

	@Override
	public void use() {
		super.use();
		this.setCount(this.getCount() - 1);

		this.owner.setHpMax(this.owner.getHpMax() + 4);
		this.owner.modifyHp(4, null);
		Graphics.playSfx("health_up");
	}
}