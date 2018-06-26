package org.rexcellentgames.burningknight.entity.pool.room;

import org.rexcellentgames.burningknight.entity.level.rooms.special.*;
import org.rexcellentgames.burningknight.entity.pool.ClosingPool;

public class SpecialRoomPool extends ClosingPool<SpecialRoom> {
  public static SpecialRoomPool instance = new SpecialRoomPool();

  public SpecialRoomPool() {
    add(TreasureRoom.class, 1f);
    add(WellRoom.class, 1f);
    add(ShopRoom.class, 1f);
    add(WeaponAltarRoom.class, 100f);
  }
}