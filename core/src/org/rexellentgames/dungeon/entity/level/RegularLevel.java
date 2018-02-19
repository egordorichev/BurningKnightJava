package org.rexellentgames.dungeon.entity.level;

import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.Rect;

import java.util.ArrayList;

public class RegularLevel extends Level {
	protected ArrayList<Room> rooms = new ArrayList<Room>();

	protected Room entrance;
	protected Room exit;

	@Override
	public boolean generate() {
		if (!this.initRooms()) {
			return false;
		}

		return true;
	}

	protected boolean initRooms() {
		this.rooms.clear();
		this.split(new Rect(0, 0, WIDTH - 1, HEIGHT - 1));

		if (this.rooms.size() < 8) {
			Log.error("Not enough rooms generated");
			return false;
		}

		for (int i = 0; i < this.rooms.size() - 1; i++) {
			for (int j = i + 1; j < this.rooms.size(); j++) {
				rooms.get(i).connectTo(rooms.get(j));
			}
		}

		return true;
	}

	private void split(Rect room) {

	}
}