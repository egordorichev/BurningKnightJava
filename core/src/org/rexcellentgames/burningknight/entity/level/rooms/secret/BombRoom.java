package org.rexcellentgames.burningknight.entity.level.rooms.secret;

import org.rexcellentgames.burningknight.entity.item.Bomb;
import org.rexcellentgames.burningknight.entity.item.ItemHolder;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.save.LevelSave;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.item.Bomb;
import org.rexcellentgames.burningknight.entity.item.ItemHolder;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.save.LevelSave;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class BombRoom extends SecretRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		for (int i = 0; i < Random.newInt(3, 5); i++) {
			Point point = this.getRandomCell();
			ItemHolder holder = new ItemHolder();
			holder.setItem(new Bomb());
			holder.x = point.x * 16 + 3;
			holder.y = point.y * 16;

			Dungeon.area.add(holder);
			LevelSave.add(holder);
		}
	}
}