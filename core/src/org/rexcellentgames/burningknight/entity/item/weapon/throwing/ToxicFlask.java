package org.rexcellentgames.burningknight.entity.item.weapon.throwing;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.item.weapon.WeaponBase;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class ToxicFlask extends WeaponBase {
	{
		sprite = "item-bottle_a";
	}

	@Override
	public void use() {
		super.use();

		TFFx fx = new TFFx();

		fx.x = this.owner.x + this.owner.w / 2;
		fx.y = this.owner.y;

		Point aim = this.owner.getAim();

		fx.to(this.owner.getAngleTo(aim.x, aim.y));

		Dungeon.area.add(fx);
	}
}