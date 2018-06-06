package org.rexellentgames.dungeon.entity.level.rooms.ladder;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.item.Gold;
import org.rexellentgames.dungeon.entity.item.ItemHolder;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.entities.Entrance;
import org.rexellentgames.dungeon.entity.level.entities.Exit;
import org.rexellentgames.dungeon.entity.level.painters.Painter;
import org.rexellentgames.dungeon.entity.level.save.LevelSave;
import org.rexellentgames.dungeon.util.geometry.Point;

public class EntranceRoom extends LadderRoom {
	public boolean exit;

	@Override
	public void paint(Level level) {
		super.paint(level);
		this.place(level, this.getCenter());
	}

	protected void place(Level level, Point point) {
		if (this.exit) {
			Painter.set(level, (int) point.x, (int) point.y, Terrain.FLOOR_B);

			Exit exit = new Exit();

			exit.x = point.x * 16;
			exit.y = point.y * 16 - 8;

			level.set((int) point.x, (int) point.y, Terrain.EXIT);
			LevelSave.add(exit);
			Dungeon.area.add(exit);
		} else {
			Entrance entrance = new Entrance();

			entrance.x = point.x * 16;
			entrance.y = point.y * 16;

		  LevelSave.add(entrance);
			Dungeon.area.add(entrance);
		}
	}
}