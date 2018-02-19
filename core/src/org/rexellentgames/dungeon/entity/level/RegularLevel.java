package org.rexellentgames.dungeon.entity.level;

import org.rexellentgames.dungeon.util.Graph;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.Random;
import org.rexellentgames.dungeon.util.Rect;

import java.util.ArrayList;

public class RegularLevel extends Level {
	private static int MIN_ROOM_SIZE = 9;
	private static int MAX_ROOM_SIZE = 11;

	protected ArrayList<Room> rooms = new ArrayList<Room>();

	protected Room entrance;
	protected Room exit;

	@Override
	public boolean generate() {
		if (!this.initRooms()) {
			return false;
		}

		int distance;
		int retry = 0;
		int minDistance = (int) Math.sqrt(this.rooms.size());

		do {
			// Make sure that the entrance is big enough
			do {
				this.entrance = this.rooms.get(Random.newInt(this.rooms.size()));
			} while (this.entrance.getWidth() < 4 && this.exit.getHeight() < 4);

			// Make sure that the exit is big enough
			do {
				this.exit = this.rooms.get(Random.newInt(this.rooms.size()));
			} while (this.exit.getWidth() < 4 && this.exit.getHeight() < 4);

			Graph.buildDistanceMap(this.rooms, this.exit);
			distance = this.entrance.getDistance();

			if (retry++ > 10) {
				Log.error("To many attempts");
				return false;
			}
		} while (distance < minDistance);

		Log.info("The distance is " + distance);

		this.entrance.setType(Room.Type.ENTRANCE);
		this.exit.setType(Room.Type.EXIT);

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

	private void split(Rect rect) {
		int w = rect.getWidth();
		int h = rect.getHeight();

		if (w > MAX_ROOM_SIZE && h < MIN_ROOM_SIZE) {
			int vw = Random.newInt(rect.left + 3, rect.right - 3);

			this.split(new Rect(rect.left, rect.top, vw, rect.bottom));
			this.split(new Rect(vw, rect.top, rect.right, rect.bottom));
		} else if (h > MAX_ROOM_SIZE && w < MIN_ROOM_SIZE) {
			int vh = Random.newInt(rect.top + 3, rect.bottom - 3);

			this.split(new Rect(rect.left, rect.top, rect.right, vh));
			this.split(new Rect(rect.left, vh, rect.right, rect.bottom));
		} else if ((Math.random() <= (MIN_ROOM_SIZE * MIN_ROOM_SIZE /
			(rect.getWidth() * rect.getHeight())) && w <= MAX_ROOM_SIZE && h <= MAX_ROOM_SIZE)
			|| w < MIN_ROOM_SIZE || h < MIN_ROOM_SIZE) {

			this.rooms.add((Room) new Room().set(rect));
		} else {
			if (Random.newFloat() < (float) (w - 2) / (w + h - 4)) {
				int vw = Random.newInt(rect.left + 3, rect.right - 3);

				this.split(new Rect(rect.left, rect.top, vw, rect.bottom));
				this.split(new Rect(vw, rect.top, rect.right, rect.bottom));
			} else {
				int vh = Random.newInt(rect.top + 3, rect.bottom - 3);

				this.split(new Rect(rect.left, rect.top, rect.right, vh));
				this.split(new Rect(rect.left, vh, rect.right, rect.bottom));
			}
		}
	}
}