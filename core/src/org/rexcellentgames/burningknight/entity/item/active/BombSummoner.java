package org.rexcellentgames.burningknight.entity.item.active;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.entity.BombEntity;
import org.rexcellentgames.burningknight.entity.level.entities.fx.PoofFx;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class BombSummoner extends Item {
	{
		useTime = 60f;
	}

	@Override
	public void use() {
		super.use();

		for (int i = 0; i < Random.newInt(10, 24); i++) {
			Point point = Player.instance.room.getRandomFreeCell();

			if (point != null) {
				float x = point.x * 16 + Random.newFloat(0, 16);
				float y = point.y * 16 + Random.newFloat(0, 16);
				Dungeon.area.add(new BombEntity(x, y));

				for (int j = 0; j < 3; j++) {
					PoofFx fx = new PoofFx();
					fx.t = 0.5f;
					fx.x = x;
					fx.y = y;
					Dungeon.area.add(fx);
				}
			}
		}
	}
}