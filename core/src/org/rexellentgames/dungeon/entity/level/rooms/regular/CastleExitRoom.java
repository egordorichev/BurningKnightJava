package org.rexellentgames.dungeon.entity.level.rooms.regular;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.entities.Exit;
import org.rexellentgames.dungeon.entity.level.painters.Painter;
import org.rexellentgames.dungeon.util.Log;

public class CastleExitRoom extends ExitRoom {
	public CastleExitRoom() {
		super(Type.CASTLE_EXIT);
	}

	@Override
	public void paint(Level level) {
		Painter.fill(level, this, 0, Terrain.WALL);
		Painter.fill(level, this, 1, Terrain.FLOOR);

		Exit exit = new Exit();

		exit.x = (this.left + this.getWidth() / 2) * 16;
		exit.y = (this.top + this.getHeight() / 4) * 16;

		level.addSaveable(exit);
		Dungeon.area.add(exit);
	}

	@Override
	public int getMaxConnections(Connection side) {
		if (side == Connection.ALL) {
			return 1;
		} else if (side == Connection.BOTTOM || side == Connection.TOP || side == Connection.RIGHT) {
			return 0;
		}

		return 1;
	}

	@Override
	public int getMinConnections(Connection side) {
		if (side == Connection.ALL) {
			return 1;
		} else if (side == Connection.BOTTOM || side == Connection.TOP || side == Connection.RIGHT) {
			return 0;
		}

		return 1;
	}

	@Override
	public int getMinWidth() {
		return 15;
	}

	@Override
	public int getMinHeight() {
		return 18;
	}

	@Override
	public int getMaxHeight() {
		return 19;
	}

	@Override
	public int getMaxWidth() {
		return 16;
	}
}