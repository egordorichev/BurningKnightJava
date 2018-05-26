package org.rexellentgames.dungeon.entity.pool;

import org.rexellentgames.dungeon.entity.level.rooms.regular.FourSideTurretRoom;
import org.rexellentgames.dungeon.entity.level.rooms.regular.*;

public class RegularRoomPool extends Pool<RegularRoom> {
	public static RegularRoomPool instance = new RegularRoomPool();

	public RegularRoomPool() {
		add(RegularRoom.class, 0.5f);
		add(GardenRoom.class, 0.5f);
		add(FloodedRoom.class, 0.3f);
		add(SpikedRoom.class, 1);
		add(MazeRoom.class, 0.05f);
		add(MazeFloorRoom.class, 0.3f);
		add(ChestTrapRoom.class, 0.3f);
		add(StatueRoom.class, 1);
		add(TableRoom.class, 0.2f);
		add(CenterTableRoom.class, 0.4f);
		add(CaveRoom.class, 0.1f);
		add(RectFloorRoom.class, 0.3f);
		add(LineRoom.class, 1);
		add(CollumnRoom.class, 0.5f);
		add(CollumnsRoom.class, 1);
		add(RollingSpikeRoom.class, 1);
		add(TurretRoom.class, 1);
		add(FourSideTurretRoom.class, 1);
		add(RotatingTurretRoom.class, 1);
	}
}