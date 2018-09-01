package org.rexcellentgames.burningknight.entity.level.rooms.secret;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.creature.fx.HeartFx;
import org.rexcellentgames.burningknight.entity.item.Bomb;
import org.rexcellentgames.burningknight.entity.item.Gold;
import org.rexcellentgames.burningknight.entity.item.key.KeyC;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.entity.level.save.LevelSave;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class MixedSecretRoom extends SecretRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		Painter.fill(level, this, 1, Terrain.FLOOR_D);
		Painter.fillEllipse(level, this, 2, Terrain.randomFloor());

		for (int i = 0; i < Random.newInt(1, 4); i++) {
			Point point = this.getRandomFreeCell();
			ItemHolder holder = new ItemHolder(new Bomb());

			holder.x = point.x * 16 + 3;
			holder.y = point.y * 16;

			Dungeon.area.add(holder);
			LevelSave.add(holder);
		}

		for (int i = 0; i < Random.newInt(1, 3); i++) {
			Point point = this.getRandomFreeCell();
			ItemHolder holder = new ItemHolder(new KeyC());
			
			holder.x = point.x * 16 + 3;
			holder.y = point.y * 16;

			Dungeon.area.add(holder);
			LevelSave.add(holder);
		}

		for (int i = 0; i < Random.newInt(1, 3); i++) {
			Point point = this.getRandomFreeCell();
			ItemHolder holder = new ItemHolder(new Gold());
			
			holder.getItem().generate();
			holder.x = point.x * 16 + 3;
			holder.y = point.y * 16;

			Dungeon.area.add(holder);
			LevelSave.add(holder);
		}

		for (int i = 0; i < Random.newInt(1, 3); i++) {
			Point point = this.getRandomFreeCell();
			HeartFx holde = new HeartFx();
			holde.x = point.x * 16 + 3;
			holde.y = point.y * 16;

			Dungeon.area.add(holde);
			LevelSave.add(holde);
		}
	}
}