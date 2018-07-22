package org.rexcellentgames.burningknight.entity.item.reference;

import org.rexcellentgames.burningknight.entity.item.autouse.Autouse;
import org.rexcellentgames.burningknight.entity.item.consumable.Consumable;
import org.rexcellentgames.burningknight.assets.Audio;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.item.consumable.Consumable;

public class MeetBoy extends Autouse {
	{
		name = Locale.get("meetboy");
		description = Locale.get("meetboy_desc");
		sprite = "item-meetboy_hp";
	}

	@Override
	public void use() {
		super.use();

		this.owner.setHpMax(this.owner.getHpMax() + 2);
		this.owner.modifyHp(2, null);

		Audio.playSfx("health_up");
	}
}