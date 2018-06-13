package org.rexcellentgames.burningknight.entity.level.levels.hall;

import org.rexcellentgames.burningknight.entity.level.builders.Builder;
import org.rexcellentgames.burningknight.entity.level.builders.LineBuilder;
import org.rexcellentgames.burningknight.entity.level.rooms.Room;
import org.rexcellentgames.burningknight.entity.level.rooms.ladder.EntranceRoom;
import org.rexcellentgames.burningknight.entity.level.rooms.regular.HallBossRoom;
import org.rexcellentgames.burningknight.entity.level.builders.Builder;
import org.rexcellentgames.burningknight.entity.level.builders.LineBuilder;
import org.rexcellentgames.burningknight.entity.level.rooms.Room;
import org.rexcellentgames.burningknight.entity.level.rooms.ladder.EntranceRoom;
import org.rexcellentgames.burningknight.entity.level.rooms.regular.HallBossRoom;

import java.util.ArrayList;

public class HallBossLevel extends HallLevel {
	@Override
	protected ArrayList<Room> createRooms() {
		ArrayList<Room> rooms = new ArrayList<>();

		rooms.add(this.entrance = new EntranceRoom());
		rooms.add(new HallBossRoom());
		rooms.add(this.exit = new EntranceRoom());
		((EntranceRoom) this.exit).exit = true;

		return rooms;
	}

	@Override
	protected Builder getBuilder() {
		return new LineBuilder();
	}
}