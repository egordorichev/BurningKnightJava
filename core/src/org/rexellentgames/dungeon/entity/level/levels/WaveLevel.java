package org.rexellentgames.dungeon.entity.level.levels;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.creature.Creature;
import org.rexellentgames.dungeon.entity.item.Item;
import org.rexellentgames.dungeon.entity.level.BetterLevel;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.builders.Builder;
import org.rexellentgames.dungeon.entity.level.builders.LineBuilder;
import org.rexellentgames.dungeon.entity.level.painters.HallPainter;
import org.rexellentgames.dungeon.entity.level.painters.Painter;
import org.rexellentgames.dungeon.entity.level.rooms.Room;
import org.rexellentgames.dungeon.entity.level.rooms.regular.FightRoom;
import org.rexellentgames.dungeon.entity.level.rooms.regular.RegularRoom;
import org.rexellentgames.dungeon.entity.level.rooms.regular.ladder.EntranceRoom;
import org.rexellentgames.dungeon.entity.level.rooms.regular.ladder.ExitRoom;
import org.rexellentgames.dungeon.net.Network;

import java.util.ArrayList;

public class WaveLevel extends BetterLevel {
	public WaveLevel() {
		if (!Network.SERVER) {
			Terrain.loadTextures(0);
		}
	}

	@Override
	protected ArrayList<Room> createRooms() {
		ArrayList<Room> rooms = new ArrayList<Room>();

		this.entrance = new EntranceRoom();
		this.exit = new ExitRoom();

		rooms.add(this.entrance);
		rooms.add(this.exit);

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
	protected ArrayList<Item> generateItems() {
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