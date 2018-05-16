package org.rexellentgames.dungeon.entity.level.rooms.regular;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.features.Door;
import org.rexellentgames.dungeon.entity.level.painters.Painter;
import org.rexellentgames.dungeon.entity.level.rooms.RectFloorRoom;
import org.rexellentgames.dungeon.entity.level.rooms.Room;
import org.rexellentgames.dungeon.entity.level.rooms.WellRoom;
import org.rexellentgames.dungeon.util.Random;
import org.rexellentgames.dungeon.util.geometry.Rect;

import java.util.ArrayList;
import java.util.Arrays;

public class RegularRoom extends Room {
	public enum Size {
		NORMAL(10, 14, 1),
		LARGE(14, 18, 2),
		GIANT(18, 24, 3);

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
		Painter.fill(level, this, 1, Terrain.FLOOR);

		for (Door door : this.connected.values()) {
			door.setType(Door.Type.REGULAR);
		}
	}

	protected Size size = Size.NORMAL;

	public boolean setSize(int min, int max) {
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
		return new float[]{1,0,0};
	}

	public Size getSize() {
		return this.size;
	}

	private static ArrayList<Class<? extends RegularRoom>> rooms = new ArrayList<Class<? extends RegularRoom>>(Arrays.asList(
		RegularRoom.class, GardenRoom.class, FloodedRoom.class, SpikedRoom.class,
		MazeRoom.class, MazeFloorRoom.class, ChestTrapRoom.class, StatueRoom.class,
		TableRoom.class, CenterTableRoom.class, CaveRoom.class, RectFloorRoom.class,
		WellRoom.class
	));

	private static float[] chances = new float[] {
		5, 3, 1, 1,
		0.05f, 1, 0.3f, 1,
		1, 1, 2, 1,
		3
	};

	public static RegularRoom create() {
		if (Dungeon.depth < 1 || Dungeon.depth == 4) {
			return new RegularRoom();
		}

		try {
			return rooms.get(Random.chances(chances)).newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return new RegularRoom();
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