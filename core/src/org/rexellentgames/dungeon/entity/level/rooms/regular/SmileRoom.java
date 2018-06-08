package org.rexellentgames.dungeon.entity.level.rooms.regular;

import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.painters.Painter;
import org.rexellentgames.dungeon.util.Random;
import org.rexellentgames.dungeon.util.geometry.Rect;

public class SmileRoom extends RegularRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		byte fill = Random.chance(50) ? Terrain.CHASM : Terrain.LAVA;
		byte f = Terrain.randomFloor();
		Painter.fillEllipse(level, this, 2, fill);
		Painter.fillEllipse(level, this, 3, f);

		float w = this.getWidth();
		float h = this.getHeight();

		Rect r = new Rect(
			this.left + 1, (int) Math.floor(this.top + h / 2),
			(int) (this.left + w - 1), (int) (Math.floor(h / 2) + Math.ceil(this.top + h / 2) - 1)
		);

		Painter.fill(level, r, f);
		Painter.fill(level, r, 1, fill);

		Painter.fill(level, new Rect(
			this.left + (int) Math.floor(w / 2) - 1, this.top + (int) Math.floor(h / 2) + 1,
			this.left + (int) Math.ceil(w / 2) + 1, this.bottom - 1
		), f);
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