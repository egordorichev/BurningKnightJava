package org.rexellentgames.dungeon.entity.level.painters;

import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.rooms.Room;
import org.rexellentgames.dungeon.util.Log;

import java.util.ArrayList;

public class Painter {
	public void paint(Level level, ArrayList<Room> rooms) {
		int leftMost = Integer.MAX_VALUE, topMost = Integer.MAX_VALUE;

		for (Room r : rooms) {
			if (r.left < leftMost) leftMost = r.left;
			if (r.top < topMost) topMost = r.top;
		}

		//subtract 1 for padding
		leftMost--;
		topMost--;

		int rightMost = 0, bottomMost = 0;

		for (Room r : rooms) {
			r.shift(-leftMost, -topMost);
			if (r.right > rightMost) rightMost = r.right;
			if (r.bottom > bottomMost) bottomMost = r.bottom;
		}

		//add 1 for padding
		rightMost++;
		bottomMost++;

		//add 1 to account for 0 values
		level.setSize(rightMost + 1, bottomMost + 1);
		level.fill();

		for (Room room : rooms) {
			room.paint(level);
		}
	}
}