package org.rexellentgames.dungeon.entity.level;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.level.builders.Builder;
import org.rexellentgames.dungeon.entity.level.builders.CastleBuilder;
import org.rexellentgames.dungeon.entity.level.builders.LineBuilder;
import org.rexellentgames.dungeon.entity.level.builders.LoopBuilder;
import org.rexellentgames.dungeon.entity.level.painters.HallPainter;
import org.rexellentgames.dungeon.entity.level.painters.Painter;
import org.rexellentgames.dungeon.entity.level.rooms.Room;
import org.rexellentgames.dungeon.entity.level.rooms.special.TowerBaseRoom;
import org.rexellentgames.dungeon.util.Random;

import java.util.ArrayList;

public class HallLevel extends BetterLevel {
	@Override
	protected ArrayList<Room> createRooms() {
		ArrayList<Room> rooms = super.createRooms();

		if (Dungeon.depth == 0) {
			for (int i = 0; i < 4; i++) {
				rooms.add(new TowerBaseRoom());
			}
		}

		return rooms;
	}

	@Override
	protected Painter getPainter() {
		return new HallPainter();
	}

	@Override
	protected Builder getBuilder() {
		return new CastleBuilder();
	}

	@Override
	protected int getNumRegularRooms() {
		return 10;
	}
}