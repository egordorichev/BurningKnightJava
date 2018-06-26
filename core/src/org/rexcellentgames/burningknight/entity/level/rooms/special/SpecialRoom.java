package org.rexcellentgames.burningknight.entity.level.rooms.special;

import org.rexcellentgames.burningknight.entity.level.rooms.Room;
import org.rexcellentgames.burningknight.entity.pool.room.SpecialRoomPool;

public class SpecialRoom extends Room {
  public static void init() {
    SpecialRoomPool.instance.reset();
  }

  public static SpecialRoom create() {
    return SpecialRoomPool.instance.generate();
  }

  @Override
  public int getMinWidth() {
    return 5;
  }

  public int getMaxWidth() {
    return 10;
  }

  @Override
  public int getMinHeight() {
    return 5;
  }

  public int getMaxHeight() {
    return 10;
  }

  @Override
  public int getMaxConnections(Connection side) {
    return 1;
  }

  @Override
  public int getMinConnections(Connection side) {
    if (side == Connection.ALL) {
      return 1;
    }

    return 0;
  }
}