package org.rexellentgames.dungeon.entity.level.rooms.regular;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.entities.Door;
import org.rexellentgames.dungeon.entity.level.entities.Exit;
import org.rexellentgames.dungeon.entity.level.painters.Painter;
import org.rexellentgames.dungeon.util.geometry.Rect;

public class SkyEntrance extends EntranceRoom {
	public SkyEntrance() {
		super(Type.SKY_ENTRANCE);
	}

	@Override
	public void paint(Level level) {
		Painter.fill(level, this, Terrain.WALL);
		Painter.fill(level, this, 1, Terrain.FLOOR);

		Rect topTower = new Rect(this.left, this.bottom - 7, this.left + 7, this.bottom);
		Rect bottomTower = new Rect(this.left, this.top, this.left + 7, this.top + 7);

		this.addExit(topTower.left + topTower.getWidth() / 2, topTower.top + topTower.getHeight() / 2);
		this.addExit(bottomTower.left + bottomTower.getWidth() / 2, bottomTower.top + bottomTower.getHeight() / 2);
	}

	private void addExit(int x, int y) {
		Exit exit = new Exit();

		exit.x = x * 16;
		exit.y = y * 16;

		Dungeon.level.addSaveable(exit);
		Dungeon.area.add(exit);
	}

	@Override
	public int getMaxConnections(Connection side) {
		if (side == Connection.ALL) {
			return 4;
		} else if (side == Connection.BOTTOM || side == Connection.RIGHT) {
			return 0;
		}

		return 2;
	}

	@Override
	public int getMinConnections(Connection side) {
		if (side == Connection.ALL) {
			return 2;
		} else if (side == Connection.BOTTOM || side == Connection.RIGHT) {
			return 0;
		}

		return 2;
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
		return 21;
	}

	@Override
	public int getMaxHeight() {
		return 22;
	}
}