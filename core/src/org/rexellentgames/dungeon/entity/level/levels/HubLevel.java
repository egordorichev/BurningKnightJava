package org.rexellentgames.dungeon.entity.level.levels;

import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.level.RegularLevel;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.builders.Builder;
import org.rexellentgames.dungeon.entity.level.builders.CastleBuilder;
import org.rexellentgames.dungeon.entity.level.painters.HallPainter;
import org.rexellentgames.dungeon.entity.level.painters.Painter;
import org.rexellentgames.dungeon.entity.level.rooms.Room;
import org.rexellentgames.dungeon.entity.level.rooms.TutorialRoom;
import org.rexellentgames.dungeon.entity.level.rooms.regular.ladder.ExitRoom;
import org.rexellentgames.dungeon.entity.level.rooms.regular.ladder.HubRoom;

import java.util.ArrayList;

public class HubLevel extends RegularLevel {
	public HubLevel() {
		Terrain.loadTextures(0);
		addLight = true;
	}

	@Override
	protected ArrayList<Room> createRooms() {
		ArrayList<Room> rooms = new ArrayList<>();

		rooms.add(new HubRoom());
		// rooms.add(new TutorialRoom());
		rooms.add(new ExitRoom());

		return rooms;
	}

	@Override
	protected Builder getBuilder() {
		return new CastleBuilder().setPathLength(0, new float[] { 0, 0, 0 });
	}

	@Override
	protected Painter getPainter() {
		return new HallPainter();
	}

	@Override
	public void destroy() {
		super.destroy();
		Player.instance = null;
	}
}