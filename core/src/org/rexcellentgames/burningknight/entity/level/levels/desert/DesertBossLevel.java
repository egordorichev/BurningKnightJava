package org.rexcellentgames.burningknight.entity.level.levels.desert;

import org.rexcellentgames.burningknight.entity.level.builders.Builder;
import org.rexcellentgames.burningknight.entity.level.builders.LineBuilder;
import org.rexcellentgames.burningknight.entity.level.rooms.Room;
import org.rexcellentgames.burningknight.entity.level.rooms.ladder.EntranceRoom;
import org.rexcellentgames.burningknight.entity.level.rooms.regular.DesertBossRoom;
import org.rexcellentgames.burningknight.entity.level.builders.Builder;
import org.rexcellentgames.burningknight.entity.level.builders.LineBuilder;
import org.rexcellentgames.burningknight.entity.level.rooms.Room;
import org.rexcellentgames.burningknight.entity.level.rooms.ladder.EntranceRoom;
import org.rexcellentgames.burningknight.entity.level.rooms.regular.DesertBossRoom;

import java.util.ArrayList;

public class DesertBossLevel extends DesertLevel {
	@Override
	protected ArrayList<Room> createRooms() {
		ArrayList<Room> rooms = new ArrayList<>();

		rooms.add(this.entrance = new EntranceRoom());
		rooms.add(new DesertBossRoom());
		rooms.add(this.exit = new EntranceRoom());
		((EntranceRoom) this.exit).exit = true;

		return rooms;
	}

	@Override
	protected Builder getBuilder() {
		return new LineBuilder();
	}
}