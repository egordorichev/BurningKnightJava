package org.rexellentgames.dungeon.entity.level.rooms.regular;

import org.rexellentgames.dungeon.entity.level.rooms.Room;
import org.rexellentgames.dungeon.util.Random;

public class RegularRoom extends Room {
	public enum Size {
		NORMAL(6, 10, 1),
		LARGE(10, 14, 2),
		GIANT(14, 18, 3);

		public final int minDim;
		public final int maxDim;
		public final int roomValue;

		Size(int min, int max, int val){
			this.minDim = min;
			this.maxDim = max;
			this.roomValue = val;
		}

		public int getConnectionWeight(){
			return this.roomValue * this.roomValue;
		}
	}

	protected Size size = Size.NORMAL;

	public RegularRoom() {
		super(Type.REGULAR);
	}

	public RegularRoom(Type type) {
		super(type);
	}

	public boolean setSize(int min, int max) {
		float[] chances = this.getSizeChance();
		Size[] sizes = Size.values();

		if (chances.length != sizes.length) {
			return false;
		}

		for (int i = 0; i < min; i++) {
			chances[i] = 0;
		}

		for (int i = max + 1; i < chances.length; i++) {
			chances[i] = 0;
		}

		int index = Random.chances(chances);

		if (index == -1) {
			this.size = sizes[index];
			return false;
		} else {
			return true;
		}
	}

	protected float[] getSizeChance() {
		return new float[] { 1, 0, 0 };
	}

	public Size getSize() {
		return this.size;
	}

	public static RegularRoom create() {
		return new RegularRoom();
	}

	@Override
	public int getMaxConnections(Connection side) {
		if (side == Connection.ALL) {
			return 16;
		}

		return 4;
	}

	@Override
	public int getMinConnections(Connection side) {
		if (side == Connection.ALL) {
			return 1;
		}

		return 0;
	}
}