package org.rexcellentgames.burningknight.entity.item.weapon.sword;

import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.assets.Locale;

public class Bone extends Claymore {
	@Override
	protected void setStats() {
		super.setStats();
		sprite = "item-bone";
		name = Locale.get("bone");
		description = Locale.get("bone_desc");
		region = Graphics.getTexture(sprite);
	}
}