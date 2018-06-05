package org.rexellentgames.dungeon.entity.level.rooms.regular;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.entities.Chair;
import org.rexellentgames.dungeon.entity.level.entities.Table;
import org.rexellentgames.dungeon.entity.level.save.LevelSave;
import org.rexellentgames.dungeon.util.geometry.Point;

public class CenterTableRoom extends RegularRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		Point center = this.getCenter();
		Table table = new Table();

		table.w = 16 * 4;
		table.h = 16 * 4;
		table.x = center.x * 16 - 32;
		table.y = center.y * 16 - 32;

		this.row((int) (center.x - 3), (int) center.y - 3, true);
		this.row((int) (center.x + 2), (int) center.y - 3, false);

		Dungeon.area.add(table);
		LevelSave.add(table);
	}

	private void row(int x, int yy, boolean f) {
		for (int y = 0; y < 4; y++) {
			Chair chair = new Chair();

			chair.x = x * 16 + (f ? 0 : 4);
			chair.y = (y + yy) * 16;
			chair.flipped = f;

			Dungeon.area.add(chair);
			LevelSave.add(chair);
		}
	}

	@Override
	public int getMinHeight() {
		return 12;
	}

	@Override
	public int getMinWidth() {
		return 12;
	}

	@Override
	public int getMaxWidth() {
		return 20;
	}

	@Override
	public int getMaxHeight() {
		return 20;
	}
}