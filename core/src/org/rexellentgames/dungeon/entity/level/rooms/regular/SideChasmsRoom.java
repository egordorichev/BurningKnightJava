package org.rexellentgames.dungeon.entity.level.rooms.regular;

import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.features.Door;
import org.rexellentgames.dungeon.entity.level.painters.Painter;
import org.rexellentgames.dungeon.util.Random;

public class SideChasmsRoom extends RegularRoom {
	@Override
	public void paint(Level level) {
		for (Door door : this.connected.values()) {
			door.setType(Door.Type.REGULAR);
		}

		byte f = Terrain.randomFloor();

		Painter.fill(level, this, Terrain.WALL);
		Painter.fill(level, this, 1, Terrain.CHASM);
		Painter.fill(level, this, Random.newInt(2, 4), f);

		this.paintTunnel(level, f);
	}

	// FIXME!
	// WITH ONE DOOR IS BROKEN

	@Override
	public int getMinWidth() {
		return 9;
	}

	@Override
	public int getMinHeight() {
		return 9;
	}
}