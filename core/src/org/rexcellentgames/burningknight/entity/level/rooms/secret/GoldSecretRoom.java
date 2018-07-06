package org.rexcellentgames.burningknight.entity.level.rooms.secret;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.item.Gold;
import org.rexcellentgames.burningknight.entity.item.ItemHolder;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.entity.level.save.LevelSave;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class GoldSecretRoom extends SecretRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		if (Random.chance(70)) {
			if (Random.chance(50)) {
				Painter.fill(level, this, 2, Terrain.randomFloor());
			} else {
				Painter.fillEllipse(level, this, 2, Terrain.randomFloor());
			}
		}

		if (Random.chance(70)) {
			boolean wall = Random.chance(50);

			if (Random.chance(50)) {
				Painter.fillEllipse(level, this, 3, wall ? Terrain.WALL : Terrain.CHASM);
			} else {
				Painter.fill(level, this, 3, wall ? Terrain.WALL : Terrain.CHASM);
			}

			if (Random.chance(70)) {
				if (Random.chance(50)) {
					Painter.fillEllipse(level, this, 4, !wall ? Terrain.WALL : Terrain.CHASM);
				} else {
					Painter.fill(level, this, 4, !wall ? Terrain.WALL : Terrain.CHASM);
				}
			}
		}

		for (int i = 0; i < Random.newInt(3, 10); i++) {
			Point point = this.getRandomFreeCell();
			ItemHolder holder = new ItemHolder();
			holder.setItem(new Gold());
			holder.x = point.x * 16 + 3;
			holder.y = point.y * 16;
			holder.getItem().generate();

			Dungeon.area.add(holder);
			LevelSave.add(holder);
		}
	}
}