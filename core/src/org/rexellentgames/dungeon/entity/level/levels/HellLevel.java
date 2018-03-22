package org.rexellentgames.dungeon.entity.level.levels;

import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.level.BetterLevel;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.builders.Builder;
import org.rexellentgames.dungeon.entity.level.builders.CastleBuilder;
import org.rexellentgames.dungeon.entity.level.painters.HellPainter;
import org.rexellentgames.dungeon.entity.level.painters.Painter;
import org.rexellentgames.dungeon.entity.level.rooms.Room;
import org.rexellentgames.dungeon.entity.level.rooms.regular.CaveRoom;
import org.rexellentgames.dungeon.entity.level.rooms.regular.ladder.EntranceRoom;
import org.rexellentgames.dungeon.entity.level.rooms.regular.ladder.ExitRoom;
import org.rexellentgames.dungeon.entity.level.rooms.regular.RegularRoom;
import org.rexellentgames.dungeon.net.Network;
import org.rexellentgames.dungeon.util.Random;

import java.util.ArrayList;

public class HellLevel extends BetterLevel {
	public HellLevel() {
		if (!Network.SERVER) {
			Terrain.loadTextures(4);
		}
	}

	@Override
	protected Builder getBuilder() {
		return new CastleBuilder();
	}

	@Override
	protected ArrayList<Room> createRooms() {
		ArrayList<Room> rooms = new ArrayList<Room>();

		rooms.add(this.exit = new ExitRoom());
		rooms.add(this.entrance = new EntranceRoom());

		int regular = this.getNumRegularRooms();

		for (int i = 0; i < regular; i++) {
			RegularRoom room;

			do {
				room = Random.chance(50) ? new CaveRoom() : RegularRoom.create();
			} while (!room.setSize(1, regular - i));

			rooms.add(room);
			i += room.getSize().roomValue - 1;
		}

		return rooms;
	}

	@Override
	protected int getNumRegularRooms() {
		return 15;
	}

	@Override
	protected Painter getPainter() {
		return new HellPainter();
	}
}