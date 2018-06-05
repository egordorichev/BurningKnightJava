package org.rexellentgames.dungeon.entity.level.rooms.secret;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.item.Gold;
import org.rexellentgames.dungeon.entity.item.ItemHolder;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.save.LevelSave;
import org.rexellentgames.dungeon.util.Random;
import org.rexellentgames.dungeon.util.geometry.Point;

public class GoldSecretRoom extends SecretRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		for (int i = 0; i < Random.newInt(3, 5); i++) {
			Point point = this.getRandomCell();
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