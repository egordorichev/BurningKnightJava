package org.rexellentgames.dungeon.entity.level.levels;

import org.rexellentgames.dungeon.entity.creature.Creature;
import org.rexellentgames.dungeon.entity.item.Item;
import org.rexellentgames.dungeon.entity.level.builders.Builder;
import org.rexellentgames.dungeon.entity.level.builders.LineBuilder;
import org.rexellentgames.dungeon.entity.level.rooms.Room;
import org.rexellentgames.dungeon.entity.level.rooms.regular.ladder.EntranceRoom;
import org.rexellentgames.dungeon.entity.level.rooms.regular.HallBossRoom;
import org.rexellentgames.dungeon.entity.level.rooms.regular.ladder.HallExitRoom;
import org.rexellentgames.dungeon.entity.level.rooms.special.HealthBlockRoom;

import java.util.ArrayList;

public class HallBossLevel extends HallLevel {
	@Override
	protected ArrayList<Item> generateItems() {
		return new ArrayList<>();
	}

	@Override
	protected ArrayList<Creature> generateCreatures() {
		return new ArrayList<>();
	}

	@Override
	protected ArrayList<Room> createRooms() {
		ArrayList<Room> rooms = new ArrayList<>();

		rooms.add(this.entrance = new EntranceRoom());
		rooms.add(new HallBossRoom());
		rooms.add(new HallExitRoom());
		rooms.add(new HealthBlockRoom());

		return rooms;
	}

	@Override
	protected Builder getBuilder() {
		return new LineBuilder();
	}
}