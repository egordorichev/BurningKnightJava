package org.rexellentgames.dungeon.entity.item.reference;

import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.assets.Locale;
import org.rexellentgames.dungeon.entity.item.consumable.Consumable;

public class MeetBoy extends Consumable {
	{
		name = Locale.get("meetboy");
		description = Locale.get("meetboy_desc");
		useOnPickup = true;
		sprite = "item-meetboy_hp";
	}

	@Override
	public void use() {
		super.use();
		this.setCount(this.getCount() - 1);

		this.owner.setHpMax(this.owner.getHpMax() + 2);
		this.owner.modifyHp(2, null);

		Graphics.playSfx("health_up");
	}
}