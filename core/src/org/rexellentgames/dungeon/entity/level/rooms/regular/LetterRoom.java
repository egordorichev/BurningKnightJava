package org.rexellentgames.dungeon.entity.level.rooms.regular;

import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.painters.Painter;
import org.rexellentgames.dungeon.util.Random;
import org.rexellentgames.dungeon.util.geometry.Point;

public class LetterRoom extends RegularRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		byte f = Random.chance(50) ? Terrain.WALL : Terrain.CHASM;
		float r = Random.newFloat();

		if (r < 0.25f) {
			// Letter X

			Painter.drawLine(level, new Point(this.left + 2, this.top + 2), new Point(this.right - 2, this.bottom - 2), f);
			Painter.drawLine(level, new Point(this.left + 2, this.bottom - 2), new Point(this.right - 2, this.top + 2), f);
		} else if (r < 0.5f) {
			// Letter I

			Painter.drawLine(level, new Point(this.left + this.getWidth() / 2, this.bottom - 2),
				new Point(this.left + this.getWidth() / 2, this.top + 2), f);
		} else if (r < 0.75f) {
			// Letter T

			Painter.drawLine(level, new Point(this.left + this.getWidth() / 2, this.bottom - 2),
				new Point(this.left + this.getWidth() / 2, this.top + 2), f);

			Painter.drawLine(level, new Point(this.left + 2, this.bottom - 2), new Point(this.right - 2, this.bottom - 2), f);
		} else {
			Painter.drawLine(level, new Point(this.left + 2, this.bottom - 2),
				new Point(this.left + 2, this.top + 2), f);

			Painter.drawLine(level, new Point(this.left + 2, this.top + 2), new Point(this.right - 2, this.top + 2), f);
			Painter.drawLine(level, new Point(this.left + 2, this.top + getHeight() / 2), new Point(this.right - 2, this.top + getHeight() / 2), f);
			Painter.drawLine(level, new Point(this.left + 2, this.bottom - 2), new Point(this.right - 2, this.bottom - 2), f);
		}
	}

	@Override
	public int getMinHeight() {
		return 8;
	}

	@Override
	public int getMinWidth() {
		return 8;
	}
}