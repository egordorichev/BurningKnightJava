package org.rexellentgames.dungeon.entity.level.rooms.regular;

import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.painters.Painter;
import org.rexellentgames.dungeon.util.geometry.Rect;

public class SmileRoom extends RegularRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		Painter.fillEllipse(level, this, 2, Terrain.LAVA);
		Painter.fillEllipse(level, this, 3, Terrain.FLOOR_A);

		float w = this.getWidth();
		float h = this.getHeight();

		Rect r = new Rect(
			this.left + 1, (int) Math.floor(this.top + h / 2),
			(int) (this.left + w - 1), (int) (Math.floor(h / 2) + Math.ceil(this.top + h / 2) - 1)
		);

		Painter.fill(level, r, Terrain.FLOOR_A);

		Painter.fill(level, r, 1, Terrain.LAVA);

		Painter.fill(level, new Rect(
			this.left + (int) Math.floor(w / 2) - 1, this.top + (int) Math.floor(h / 2) + 1,
			this.left + (int) Math.ceil(w / 2) + 1, this.bottom - 1
		), Terrain.FLOOR_A);
	}

	@Override
	public int getMinHeight() {
		return 7;
	}

	@Override
	public int getMinWidth() {
		return 9;
	}
}