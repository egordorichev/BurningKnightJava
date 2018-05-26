package org.rexellentgames.dungeon.entity.level.levels;

import org.rexellentgames.dungeon.entity.creature.Creature;
import org.rexellentgames.dungeon.entity.level.RegularLevel;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.builders.Builder;
import org.rexellentgames.dungeon.entity.level.builders.LineBuilder;
import org.rexellentgames.dungeon.entity.level.painters.HallPainter;
import org.rexellentgames.dungeon.entity.level.painters.Painter;
import org.rexellentgames.dungeon.entity.level.rooms.Room;
import org.rexellentgames.dungeon.entity.level.rooms.regular.FightRoom;
import org.rexellentgames.dungeon.entity.level.rooms.regular.RegularRoom;
import org.rexellentgames.dungeon.entity.level.rooms.ladder.EntranceRoom;

import java.util.ArrayList;

public class WaveLevel extends RegularLevel {
	public WaveLevel() {
		Terrain.loadTextures(0);
	}

	@Override
	protected ArrayList<Room> createRooms() {
		ArrayList<Room> rooms = new ArrayList<Room>();

		this.entrance = new EntranceRoom();
		rooms.add(this.exit = new EntranceRoom());
		((EntranceRoom) this.exit).exit = true;

		rooms.add(this.entrance);

		int regular = this.getNumRegularRooms();

		for (int i = 0; i < regular; i++) {
			RegularRoom room = new FightRoom();
			room.setSize();

			rooms.add(room);
		}

		return rooms;
	}

	@Override
	protected ArrayList<Creature> generateCreatures() {
		return new ArrayList<>();
	}

	@Override
	protected Painter getPainter() {
		return new HallPainter().setGrass(0.45f).setWater(0.45f);
	}

	@Override
	protected Builder getBuilder() {
		return new LineBuilder().setPathLength(1f, new float[]{0,1,0});
	}

	@Override
	protected int getNumRegularRooms() {
		return 1;
	}
}