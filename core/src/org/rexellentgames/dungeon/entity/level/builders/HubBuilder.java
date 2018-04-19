package org.rexellentgames.dungeon.entity.level.builders;

import org.rexellentgames.dungeon.entity.level.rooms.Room;

import java.util.ArrayList;

public class HubBuilder extends RegularBuilder {
	@Override
	public ArrayList<Room> build(ArrayList<Room> init) {
		this.setupRooms(init);

		if (this.entrance == null) {
			return null;
		}

		this.entrance.setPos(0, 0);
		this.entrance.setSize();

		return init;
	}
}