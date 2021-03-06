package org.rexcellentgames.burningknight.entity.level.levels.library;

import org.rexcellentgames.burningknight.entity.level.builders.Builder;
import org.rexcellentgames.burningknight.entity.level.builders.LineBuilder;
import org.rexcellentgames.burningknight.entity.level.rooms.Room;
import org.rexcellentgames.burningknight.entity.level.rooms.entrance.EntranceRoom;
import org.rexcellentgames.burningknight.entity.level.rooms.regular.boss.LibraryBossRoom;

import java.util.ArrayList;

public class LibraryBossLevel extends LibraryLevel {
	@Override
	protected ArrayList<Room> createRooms() {
		ArrayList<Room> rooms = new ArrayList<>();

		rooms.add(this.entrance = new EntranceRoom());
		rooms.add(new LibraryBossRoom());
		rooms.add(this.exit = new EntranceRoom());
		((EntranceRoom) this.exit).exit = true;

		return rooms;
	}

	@Override
	protected Builder getBuilder() {
		return new LineBuilder();
	}
}