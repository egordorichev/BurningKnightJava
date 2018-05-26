package org.rexellentgames.dungeon.entity.pool;

import org.rexellentgames.dungeon.entity.level.rooms.regular.*;

public class RegularRoomPool extends Pool<RegularRoom> {
	public static RegularRoomPool instance = new RegularRoomPool();

	public RegularRoomPool() {
		add(RegularRoom.class, 1);
		add(GardenRoom.class, 1);
		add(FloodedRoom.class, 1);
		add(SpikedRoom.class, 1);
		add(MazeRoom.class, 0.05f);
		add(MazeFloorRoom.class, 1);
		add(ChestTrapRoom.class, 0.3f);
		add(StatueRoom.class, 1);
		add(TableRoom.class, 1);
		add(CenterTableRoom.class, 1);
		add(CaveRoom.class, 2);
		add(RectFloorRoom.class, 1);
		add(LineRoom.class, 1);
		add(CollumnRoom.class, 1);
		add(CollumnsRoom.class, 1);
		add(RollingSpikeRoom.class, 100);
	}
}