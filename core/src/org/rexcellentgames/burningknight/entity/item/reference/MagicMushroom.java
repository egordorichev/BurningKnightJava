package org.rexcellentgames.burningknight.entity.item.reference;

import org.rexcellentgames.burningknight.entity.item.autouse.Autouse;
import org.rexcellentgames.burningknight.assets.Audio;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.item.autouse.Autouse;

public class MagicMushroom extends Autouse {
	{
		name = Locale.get("magic_mushroom");
		description = Locale.get("magic_mushroom_desc");
	}

	@Override
	public void use() {
		super.use();

		this.owner.setHpMax(this.owner.getHpMax() + 4);
		this.owner.modifyHp(4, null);
		Audio.playSfx("health_up");
	}
}