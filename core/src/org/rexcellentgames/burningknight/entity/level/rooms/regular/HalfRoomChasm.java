package org.rexcellentgames.burningknight.entity.level.rooms.regular;

import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.features.Door;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.entity.level.rooms.Room;
import org.rexcellentgames.burningknight.util.geometry.Rect;

public class HalfRoomChasm extends RegularRoom {
  @Override
  public void paint(Level level) {
    for (Door door: this.connected.values()) {
      door.setType(Door.Type.REGULAR);
    }

    byte f = Terrain.randomFloor();

    Painter.fill(level, this, Terrain.WALL);
    Painter.fill(level, this, 1, Terrain.CHASM);

    Rect rect = null;

		/*
		switch (Random.newInt(4)) {
			case 0: default: rect = new Rect(this.left + 1, this.top + 1, this.right - 1, this.bottom - 1 - this.getHeight() / 2); break;
			case 1: rect = new Rect(this.left + 1, this.top + 1 + this.getHeight() / 2, this.right - 1, this.bottom - 1);	 break;
			case 2:	rect = new Rect(this.left + 1 + this.getWidth() / 2, this.top + 1, this.right - 1, this.bottom - 1); break;
			case 3:	rect = new Rect(this.left + 1, this.top + 1, this.right - 1 - this.getWidth() / 2, this.bottom - 1); break;
		}
		 */

    if (this.getCurrentConnections(Room.Connection.TOP) > 0) {
      rect = new Rect(this.left + 1, this.top + 1, this.right, this.bottom - this.getHeight() / 2);
    } else if (this.getCurrentConnections(Room.Connection.BOTTOM) > 0) {
      rect = new Rect(this.left + 1, this.top + 1 + this.getHeight() / 2, this.right, this.bottom);
    } else if (this.getCurrentConnections(Room.Connection.RIGHT) > 0) {
      rect = new Rect(this.left + 1 + this.getWidth() / 2, this.top + 1, this.right, this.bottom);
    } else if (this.getCurrentConnections(Room.Connection.LEFT) > 0) {
      rect = new Rect(this.left + 1, this.top + 1, this.right - this.getWidth() / 2, this.bottom);
    }

    if (rect != null) {
      this.paintTunnel(level, f);
      Painter.fill(level, rect, f);
    }
  }
}