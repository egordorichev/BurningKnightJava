package org.rexcellentgames.burningknight.entity.pool.room;

import org.rexcellentgames.burningknight.entity.level.rooms.regular.*;
import org.rexcellentgames.burningknight.entity.pool.Pool;

public class LampRoomPool extends Pool<RegularRoom> {
  public static LampRoomPool instance = new LampRoomPool();

  public LampRoomPool() {
    add(GardenRoom.class, 1f);
    add(SpikedRoom.class, 1f);
    add(MazeFloorRoom.class, 0.3f);
    add(StatueRoom.class, 1);
    add(RectFloorRoom.class, 0.3f);
    add(LineRoom.class, 1);
    add(BrokeLineRoom.class, 1);
    add(CollumnsRoom.class, 1);
    add(CircleLineRoom.class, 1);
    // add(SmileRoom.class, 0.4f);
    add(SideChasmsRoom.class, 1f);
  }
}