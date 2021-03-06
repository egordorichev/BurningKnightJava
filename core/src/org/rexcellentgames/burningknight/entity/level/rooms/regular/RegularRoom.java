package org.rexcellentgames.burningknight.entity.level.rooms.regular;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.features.Door;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.entity.level.rooms.Room;
import org.rexcellentgames.burningknight.entity.level.save.GameSave;
import org.rexcellentgames.burningknight.entity.pool.room.RegularRoomPool;
import org.rexcellentgames.burningknight.util.Random;

public class RegularRoom extends Room {
	public enum Size {
		NORMAL(8, 12, 1),
		LARGE(12, 14, 2),
		GIANT(14, 16, 3);

		public final int minDim;
		public final int maxDim;
		public final int roomValue;

		Size(int min, int max, int val) {
			this.minDim = min;
			this.maxDim = max;
			this.roomValue = val;
		}

		public int getConnectionWeight() {
			return this.roomValue * this.roomValue;
		}
	}

	@Override
	public void paint(Level level) {
		Painter.fill(level, this, Terrain.WALL);
		Painter.fill(level, this, 1, Terrain.randomFloor());

		if (Random.chance(50)) {
			Painter.fillEllipse(level, this, 1, Terrain.randomFloor());
		}

		if (Random.chance(50)) {
			Painter.fill(level, this, Random.newInt(2, 6), Terrain.randomFloor());
		}

		for (Door door : this.connected.values()) {
			door.setType(Door.Type.ENEMY);
		}
	}

	protected Size size = Size.NORMAL;

	public boolean setSize(int min, int max) {
		if (GameSave.runId == 0) {
			this.size = Size.NORMAL;
			return true;
		}

		float[] chances = this.getSizeChance();
		Size[] sizes = Size.values();

		if (chances.length != sizes.length) {
			return false;
		}

		for (int i = 0; i < min; i++) {
			chances[i] = 0;
		}

		for (int i = max + 1; i < chances.length; i++) {
			chances[i] = 0;
		}

		int index = Random.chances(chances);

		if (index == -1) {
			this.size = sizes[0];
			return false;
		} else {
			return true;
		}
	}

	protected float[] getSizeChance() {
		return new float[] {1, 0, 0};
	}

	public Size getSize() {
		return this.size;
	}

	public static RegularRoom create() {
		if (Dungeon.depth < 1) {
			return new RegularRoom();
		}

		return RegularRoomPool.instance.generate();
	}

	@Override
	public int getMaxConnections(Connection side) {
		if (side == Connection.ALL) {
			return 16;
		}

		return 4;
	}

	@Override
	public int getMinConnections(Connection side) {
		if (side == Connection.ALL) {
			return 1;
		}

		return 0;
	}
}