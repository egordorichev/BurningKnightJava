package org.rexellentgames.dungeon.entity.pool.room;

import org.rexellentgames.dungeon.entity.level.rooms.regular.FourSideTurretRoom;
import org.rexellentgames.dungeon.entity.level.rooms.regular.*;
import org.rexellentgames.dungeon.entity.pool.Pool;

public class RegularRoomPool extends Pool<RegularRoom> {
	public static RegularRoomPool instance = new RegularRoomPool();

	public RegularRoomPool() {
		add(RegularRoom.class, 0.5f);
		add(GardenRoom.class, 0.5f);
		add(FloodedRoom.class, 0.3f);
		add(SpikedRoom.class, 1);
		add(MazeRoom.class, 0.1f);
		add(MazeFloorRoom.class, 0.3f);
		add(StatueRoom.class, 1);
		add(TableRoom.class, 0.2f);
		add(CenterTableRoom.class, 0.4f);
		add(CaveRoom.class, 1f);
		add(RectFloorRoom.class, 0.3f);
		add(LineRoom.class, 1);
		add(BrokeLineRoom.class, 1);
		add(CollumnRoom.class, 1f);
		add(CollumnsRoom.class, 1);
		add(RollingSpikeRoom.class, 3);
		add(TurretRoom.class, 1);
		add(FourSideTurretRoom.class, 1);
		add(RotatingTurretRoom.class, 1);
		add(CircleLineRoom.class, 1);
		add(LavaLakeRoom.class, 1);
		add(SmileRoom.class, 0.4f);
		add(CavyChasmRoom.class, 2f);
		add(SideChasmsRoom.class, 1f);
		add(HalfRoomChasm.class, 1f);
		add(BigHoleRoom.class, 2f);
		add(BigHoleWithRectRoom.class, 1f);
	}
}