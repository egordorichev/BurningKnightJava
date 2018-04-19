package org.rexellentgames.dungeon.entity.level.rooms;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.entities.Entrance;
import org.rexellentgames.dungeon.entity.level.entities.Exit;
import org.rexellentgames.dungeon.entity.level.features.Door;
import org.rexellentgames.dungeon.entity.level.painters.Painter;
import org.rexellentgames.dungeon.entity.level.rooms.regular.ladder.ExitRoom;
import org.rexellentgames.dungeon.util.geometry.Point;

public class TutorialRoom extends ExitRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		Point point = this.getCenter();
		Painter.set(level, (int) point.x, (int) point.y, Terrain.WOOD);

		Exit exit = new Exit();

		exit.x = point.x * 16;
		exit.y = point.y * 16 - 8;
		exit.setType(Entrance.ENTRANCE_TUTORIAL);

		level.addSaveable(exit);
		Dungeon.area.add(exit);

		for (Door door : this.connected.values()) {
			door.setType(Door.Type.ENEMY);
		}
	}
}