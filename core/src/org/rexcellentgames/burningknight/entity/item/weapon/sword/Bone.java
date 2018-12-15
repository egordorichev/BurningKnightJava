package org.rexcellentgames.burningknight.entity.item.weapon.sword;

import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.assets.Locale;

public class Bone extends Claymore {
	@Override
	protected void setStats() {
		super.setStats();
		timeA = 0.05f;
		useTime = timeA;
		sprite = "item-bone";
		name = Locale.get("bone");
		description = Locale.get("bone_desc");
		region = Graphics.getTexture(sprite);
	}
}