package org.rexellentgames.dungeon.entity.level;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.creature.mob.BurningKnight;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.level.builders.Builder;
import org.rexellentgames.dungeon.entity.level.builders.RegularBuilder;
import org.rexellentgames.dungeon.entity.level.painters.Painter;
import org.rexellentgames.dungeon.entity.level.rooms.*;
import org.rexellentgames.dungeon.entity.level.rooms.regular.EntranceRoom;
import org.rexellentgames.dungeon.entity.level.rooms.regular.ExitRoom;
import org.rexellentgames.dungeon.entity.level.rooms.regular.RegularRoom;
import org.rexellentgames.dungeon.entity.level.rooms.special.SpecialRoom;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.Random;

import java.util.ArrayList;
import java.util.Collections;

public abstract class BetterLevel extends Level {
	protected ArrayList<Room> rooms;
	protected Room entrance;
	protected Room exit;

	@Override
	public void generate() {
		Builder builder = this.getBuilder();

		ArrayList<Room> rooms = this.createRooms();
		Collections.shuffle(rooms);

		do {
			Log.info("Generating...");

			for (Room room : rooms) {
				room.getConnected().clear();
				room.getNeighbours().clear();
			}

			this.rooms = builder.build((ArrayList<Room>) rooms.clone());

			if (this.rooms == null) {
				Log.info("Failed!");
			}
		} while (this.rooms == null);

		Log.info("Painting...");

		Painter painter = this.getPainter();

		painter.paint(this, this.rooms);
		this.tile();
		painter.draw(this, this.rooms);

		Log.info("Adding entities...");

		Player player = new Player();

		Dungeon.area.add(player);
		this.addPlayerSaveable(player);

		BurningKnight knight = new BurningKnight();

		Dungeon.area.add(knight);
		this.addPlayerSaveable(knight);

		Log.info("Done!");
	}

	protected ArrayList<Room> createRooms() {
		ArrayList<Room> rooms = new ArrayList<Room>();

		rooms.add(this.entrance = new EntranceRoom());
		rooms.add(this.exit = new ExitRoom());

		int regular = this.getNumRegularRooms();
		int special = this.getNumSpecialRooms();

		for (int i = 0; i < regular; i++) {
			org.rexellentgames.dungeon.entity.level.rooms.regular.RegularRoom room;

			do {
				room = RegularRoom.create();
			} while(!room.setSize(0, regular - i));

			i += room.getSize().roomValue - 1;
			rooms.add(room);
		}

		// todo: shop

		for (int i = 0; i < special; i++) {
			rooms.add(SpecialRoom.create());
		}

		// todo: secret

		return rooms;
	}

	protected Builder getBuilder() {
		return new RegularBuilder();
	}

	protected abstract Painter getPainter();

	protected int getNumRegularRooms() {
		return 8;
	}

	protected int getNumSpecialRooms() {
		return 0;
	}

	protected int getNumItems() {
		return 10 + Random.newInt(5);
	}
}