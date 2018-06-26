package org.rexcellentgames.burningknight.entity.item.autouse;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.fx.BallProjectile;

public class Ball extends Autouse {
	{
		name = Locale.get("ball");
		description = Locale.get("ball_desc");
		sprite = "item-bullet_e";
	}

	@Override
	public void use() {
		super.use();
		setCount(count - 1);

		BallProjectile projectile = new BallProjectile();

		projectile.x = this.owner.x + (this.owner.w - projectile.w) / 2;
		projectile.y = this.owner.y + (this.owner.h - projectile.h) / 2;

		Dungeon.area.add(projectile);
	}
}