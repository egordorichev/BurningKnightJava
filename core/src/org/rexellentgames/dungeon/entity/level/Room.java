package org.rexellentgames.dungeon.entity.level;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.level.painter.Painter;
import org.rexellentgames.dungeon.entity.level.painter.RegularPainter;
import org.rexellentgames.dungeon.util.Rect;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class Room extends Rect {
	public enum Type {
		NULL(null),
		Regular(RegularPainter.class);

		private Method paint;

		private Type(Class<? extends Painter> painter) {
			try {
				paint = painter.getMethod("paint", Level.class, Room.class);
			} catch (Exception e) {
				paint = null;
			}
		}

		public void paint( Level level, Room room ) {
			try {
				paint.invoke(null, level, room);
			} catch (Exception e) {
				Dungeon.reportException(e);
			}
		}
	}

	private ArrayList<Room> neigbours = new ArrayList<Room>();
	private HashMap<Room, Door> connected = new HashMap<Room, Door>();
	private Type type;

	public Type getType() {
		return this.type;
	}

	public void connectTo(Room room) {
		if (!this.connected.containsKey(room)) {
			this.connected.put(room, null);
			room.connected.put(this, null);
		}
	}
}