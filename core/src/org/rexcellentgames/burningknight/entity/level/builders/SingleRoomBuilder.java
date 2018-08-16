package org.rexcellentgames.burningknight.entity.level.builders;

import org.rexcellentgames.burningknight.entity.level.rooms.Room;

import java.util.ArrayList;

public class SingleRoomBuilder extends Builder {
	@Override
	public ArrayList<Room> build(ArrayList<Room> init) {
		Room room = init.get(0);

		room.setSize();
		room.setPos(0, 0);

		return init;
	}
}