package org.rexellentgames.dungeon.entity.level.levels;

import org.rexellentgames.dungeon.entity.creature.Creature;
import org.rexellentgames.dungeon.entity.item.Item;
import org.rexellentgames.dungeon.entity.level.RegularLevel;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.builders.Builder;
import org.rexellentgames.dungeon.entity.level.builders.LineBuilder;
import org.rexellentgames.dungeon.entity.level.painters.HallPainter;
import org.rexellentgames.dungeon.entity.level.painters.Painter;
import org.rexellentgames.dungeon.entity.level.rooms.Room;
import org.rexellentgames.dungeon.entity.level.rooms.connection.ConnectionRoom;
import org.rexellentgames.dungeon.entity.level.rooms.regular.BKRoom;
import org.rexellentgames.dungeon.entity.level.rooms.regular.FightRoom;
import org.rexellentgames.dungeon.entity.level.rooms.regular.LampRoom;
import org.rexellentgames.dungeon.entity.level.rooms.regular.RegularRoom;
import org.rexellentgames.dungeon.entity.level.rooms.regular.ladder.EntranceRoom;
import org.rexellentgames.dungeon.entity.level.rooms.regular.ladder.ExitRoom;

import java.util.ArrayList;

public class IntroLevel extends RegularLevel {
	public IntroLevel() {
		Terrain.loadTextures(0);
	}

	@Override
	protected Painter getPainter() {
		return new HallPainter().setGrass(0.5f);
	}

	@Override
	protected int getNumConnectionRooms() {
		return 0;
	}

	@Override
	protected int getNumRegularRooms() {
		return 0;
	}

	@Override
	protected ArrayList<Creature> generateCreatures() {
		return new ArrayList<>();
	}

	@Override
	protected Builder getBuilder() {
		return new LineBuilder();
	}

	protected ArrayList<Room> createRooms() {
		ArrayList<Room> rooms = new ArrayList<Room>();

		this.entrance = new EntranceRoom();
		this.exit = new ExitRoom();

		rooms.add(this.entrance);
		rooms.add(this.exit);
		rooms.add(new LampRoom());
		rooms.add(new BKRoom());
		rooms.add(new RegularRoom());
		rooms.add(new FightRoom());

		int regular = this.getNumRegularRooms();
		int connection = this.getNumConnectionRooms();

		for (int i = 0; i < regular; i++) {
			RegularRoom room;

			do {
				room = RegularRoom.create();
			} while (!room.setSize(0, regular - i));

			i += room.getSize().roomValue - 1;
			rooms.add(room);
		}

		for (int i = 0; i < connection; i++) {
			rooms.add(ConnectionRoom.create());
		}

		return rooms;
	}
}