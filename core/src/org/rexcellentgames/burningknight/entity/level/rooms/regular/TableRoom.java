package org.rexcellentgames.burningknight.entity.level.rooms.regular;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.entities.Table;
import org.rexcellentgames.burningknight.entity.level.save.LevelSave;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.entities.Table;
import org.rexcellentgames.burningknight.entity.level.save.LevelSave;
import org.rexcellentgames.burningknight.util.Random;

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
		LevelSave.add(table);
	}
}