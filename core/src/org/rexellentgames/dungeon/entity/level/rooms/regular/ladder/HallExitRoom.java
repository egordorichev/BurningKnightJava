package org.rexellentgames.dungeon.entity.level.rooms.regular.ladder;

public class HallExitRoom extends ExitRoom {
	@Override
	public int getMinWidth() {
		return 5;
	}

	@Override
	public int getMaxWidth() {
		return 6;
	}

	@Override
	public int getMinHeight() {
		return 5;
	}

	@Override
	public int getMaxHeight() {
		return 6;
	}
}