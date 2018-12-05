package org.rexcellentgames.burningknight.entity.item.weapon.sword;

import org.rexcellentgames.burningknight.assets.Graphics;

public class Bone extends Claymore {
	@Override
	protected void setStats() {
		super.setStats();
		sprite = "item-bone";
		region = Graphics.getTexture(sprite);
	}
}