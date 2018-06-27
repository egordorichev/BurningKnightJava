package org.rexcellentgames.burningknight.entity.item.reference;

import org.rexcellentgames.burningknight.entity.item.autouse.Autouse;
import org.rexcellentgames.burningknight.assets.Audio;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.item.autouse.Autouse;

public class MagicMushroom extends Autouse {
	{
		
		
	}

	@Override
	public void use() {
		super.use();
		this.setCount(this.getCount() - 1);

		this.owner.setHpMax(this.owner.getHpMax() + 4);
		this.owner.modifyHp(4, null);
		Audio.playSfx("health_up");
	}
}