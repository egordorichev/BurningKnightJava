package org.rexellentgames.dungeon.entity.level.rooms.special;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.entities.chest.Chest;
import org.rexellentgames.dungeon.entity.level.painters.Painter;
import org.rexellentgames.dungeon.util.geometry.Point;

public class TreasureRoom extends LockedRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		Painter.fill(level, this, 1, Terrain.FLOOR_D);

		Point center = this.getCenter();
		Chest chest = Chest.random();

		chest.x = center.x * 16;
		chest.y = center.y * 16;
		chest.setItem(chest.generate());

		Dungeon.area.add(chest);
		Dungeon.level.addSaveable(chest);
	}
}