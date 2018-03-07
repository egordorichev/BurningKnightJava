package org.rexellentgames.dungeon.entity.level.rooms.regular;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.entities.Door;
import org.rexellentgames.dungeon.entity.level.entities.Entrance;
import org.rexellentgames.dungeon.entity.level.painters.Painter;
import org.rexellentgames.dungeon.util.geometry.Point;
import org.rexellentgames.dungeon.util.geometry.Rect;

public class CastleEntranceRoom extends EntranceRoom {
	public CastleEntranceRoom() {
		super(Type.CASTLE_ENTRANCE);
	}

	@Override
	public void paint(Level level) {
		Painter.fill(level, this, 0, Terrain.WALL);
		Painter.fill(level, this, 1, Terrain.FLOOR);

		Rect topTower = new Rect(this.left - 3, this.bottom - 3, this.left + 4, this.bottom + 4);

		Painter.fill(level, topTower, Terrain.WALL);
		Painter.fill(level, topTower, 1, Terrain.FLOOR);

		Rect bottomTower = new Rect(this.left - 3, this.top - 3, this.left + 4,  this.top + 4);

		Painter.fill(level, bottomTower, Terrain.WALL);
		Painter.fill(level, bottomTower, 1, Terrain.FLOOR);

		Painter.drawLine(level, new Point(this.left, topTower.top - 2), new Point(this.left, bottomTower.bottom + 1),
			Terrain.FLOOR);

		int x = bottomTower.left + 6;
		int y = bottomTower.bottom - 2;

		Painter.set(level, x, y, Terrain.FLOOR);

		Door door = new Door(x, y, true);

		level.addSaveable(door);
		Dungeon.area.add(door);

		this.addEntrance(topTower.left + topTower.getWidth() / 2, topTower.top + topTower.getHeight() / 2);
		this.addEntrance(bottomTower.left + bottomTower.getWidth() / 2, bottomTower.top + bottomTower.getHeight() / 2);
	}

	private void addEntrance(int x, int y) {
		Entrance entrance = new Entrance();

		entrance.x = x * 16;
		entrance.y = y * 16;

		Dungeon.level.addSaveable(entrance);
		Dungeon.area.add(entrance);
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
		return 6;
	}

	@Override
	public int getMaxWidth() {
		return 7;
	}

	@Override
	public int getMinHeight() {
		return 15;
	}

	@Override
	public int getMaxHeight() {
		return 16;
	}
}