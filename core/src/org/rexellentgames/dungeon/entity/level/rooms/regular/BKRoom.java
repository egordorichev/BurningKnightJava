package org.rexellentgames.dungeon.entity.level.rooms.regular;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.creature.mob.BurningKnight;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.save.PlayerSave;
import org.rexellentgames.dungeon.util.geometry.Point;

public class BKRoom extends RegularRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		Point center = this.getCenter();
		BurningKnight knight = new BurningKnight();

		Dungeon.area.add(knight);
		PlayerSave.add(knight);

		knight.tp(center.x * 16, center.y * 16);

		knight.become("wait");
	}

	@Override
	public int getMinConnections(Connection side) {
		if (side == Connection.ALL) {
			return 2;
		}

		return 1;
	}

	@Override
	public int getMaxConnections(Connection side) {
		return 2;
	}

	@Override
	public int getMaxHeight() {
		return 21;
	}

	@Override
	public int getMaxWidth() {
		return 21;
	}

	@Override
	public int getMinHeight() {
		return 11;
	}

	@Override
	public int getMinWidth() {
		return 11;
	}
}