package org.rexcellentgames.burningknight.entity.item.weapon.throwing;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.item.consumable.Consumable;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class ConfettiGrenade extends Consumable {
	@Override
	public void use() {
		super.use();
		setCount(count - 1);

		CGFx fx = new CGFx();

		fx.x = this.owner.x + this.owner.w / 2;
		fx.y = this.owner.y + this.owner.h / 2;

		Point aim = this.owner.getAim();

		float a = this.owner.getAngleTo(aim.x, aim.y);
		float f = 60f;

		fx.vel.x = (float) Math.cos(a) * f;
		fx.vel.y = (float) Math.sin(a) * f;

		Dungeon.area.add(fx);
	}
}