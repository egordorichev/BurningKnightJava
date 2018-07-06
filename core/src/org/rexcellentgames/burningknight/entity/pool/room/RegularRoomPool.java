package org.rexcellentgames.burningknight.entity.pool.room;

import org.rexcellentgames.burningknight.entity.level.rooms.regular.*;
import org.rexcellentgames.burningknight.entity.pool.Pool;

public class RegularRoomPool extends Pool<RegularRoom> {
	public static RegularRoomPool instance = new RegularRoomPool();

	public RegularRoomPool() {
		this.add(CircleRoom.class, 3);
		add(GardenRoom.class, 1f);
		add(FloodedRoom.class, 0.3f);
		add(SpikedRoom.class, 1f);
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
		add(CircleLineRoom.class, 3);
		add(LavaLakeRoom.class, 1);
		add(SmileRoom.class, 0.4f);
		add(CavyChasmRoom.class, 2f);
		add(SideChasmsRoom.class, 1f);
		add(HalfRoomChasm.class, 1f);
		add(BigHoleRoom.class, 2f);
		add(CrossRoom.class, 2f);
		add(RectCornerRoom.class, 2f);
		add(CenterWallRoom.class, 1f);
		add(SmallMazeRoom.class, 2f);
		add(SmallAdditionRoom.class, 1f);
		add(LetterRoom.class, 1f);
		add(SpikeTrapRoom.class, 1f);
		add(VerticalSpikeTrapRoom.class, 1f);
		add(PrisonRoom.class, 1f);
		add(TriangleRoom.class, 2f);
		add(AnticrossRoom.class, 2f);
		add(PadRoom.class, 2f);
		add(CenterStructRoom.class, 2);
		add(RombRoom.class, 2f);
		add(FilledRombRoom.class, 3f);

		add(LineMazeRoom.class, 10000000f);
		add(VerticalMazeRoom.class, 2f);
	}
}