package org.rexellentgames.dungeon.entity.level.rooms.regular;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.entities.Table;
import org.rexellentgames.dungeon.util.Random;

public class TableRoom extends RegularRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		Table table = new Table();

		int w = Random.newInt(1, Math.min(4, this.getWidth() - 5));
		int h = Random.newInt(1, Math.min(4, this.getHeight() - 5));

		table.w = w * 16;
		table.h = h * 16;
		table.x = (this.left + Random.newInt(this.getHeight() - w - 4) + 2) * 16;
		table.y = (this.top + Random.newInt(this.getHeight() - w - 4) + 2) * 16;

		Dungeon.area.add(table);
		Dungeon.level.addSaveable(table);
	}
}