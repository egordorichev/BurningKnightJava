package org.rexellentgames.dungeon.entity.level.rooms.regular;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.entities.Statue;

public class StatueRoom extends RegularRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		this.row(this.left + 2);
		this.row(this.right - 2);
	}

	private void row(int x) {
		for (int y = this.top + 2; y < this.bottom - 1; y += 3) {
			Statue statue = new Statue();

			statue.x = x * 16;
			statue.y = y * 16;

			Dungeon.area.add(statue);
			Dungeon.level.addSaveable(statue);
		}
	}

	@Override
	public int getMinWidth() {
		return 7;
	}

	@Override
	public int getMinHeight() {
		return 7;
	}
}