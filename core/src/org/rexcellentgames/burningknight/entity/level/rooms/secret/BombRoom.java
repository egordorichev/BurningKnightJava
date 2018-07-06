package org.rexcellentgames.burningknight.entity.level.rooms.secret;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.item.Bomb;
import org.rexcellentgames.burningknight.entity.item.ItemHolder;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.entity.level.save.LevelSave;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class BombRoom extends SecretRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		if (Random.chance(50)) {
			Painter.fill(level, this, 1, Random.chance(50) ? Terrain.FLOOR_D : Terrain.randomFloor());
			Painter.fill(level, this, 2, Terrain.CHASM);
		} else {
			Painter.fill(level, this, 1, Terrain.CHASM);
		}

		if (Random.chance(50)) {
			Painter.fill(level, this, 3, Terrain.randomFloor());
			Painter.fill(level, this, 4, Terrain.randomFloor());
		} else {
			Painter.fillEllipse(level, this, 3, Terrain.randomFloor());
			Painter.fillEllipse(level, this, 4, Terrain.randomFloor());
		}

		paintTunnel(level, Terrain.FLOOR_D);

		for (int i = 0; i < Random.newInt(3, 5); i++) {
			Point point = this.getCenter();
			ItemHolder holder = new ItemHolder();
			holder.setItem(new Bomb());
			holder.x = point.x * 16 + 3;
			holder.y = point.y * 16;

			Dungeon.area.add(holder);
			LevelSave.add(holder);
		}
	}

	@Override
	protected Point getDoorCenter() {
		return getCenter();
	}
}