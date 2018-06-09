package org.rexellentgames.dungeon.entity.level.rooms.regular;

import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.painters.Painter;
import org.rexellentgames.dungeon.util.geometry.Point;
import org.rexellentgames.dungeon.util.geometry.Rect;

public class CrossRoom extends RegularRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		Painter.fill(level, this, 1, Terrain.CHASM);

		byte f = Terrain.randomFloor();

		Painter.fill(level, new Rect(this.left + 1, this.top + this.getHeight() / 2 - 1, this.right, this.top + this.getHeight() / 2 + 1), f);
		Painter.fill(level, new Rect(this.left + this.getWidth() / 2 - 1, this.top + 1, this.left + this.getWidth() / 2 + 1, this.bottom), f);
	}

	@Override
	public boolean canConnect(Point p) {
		if (!(p.x == this.left + this.getWidth() / 2 && p.y == this.top)
			&& !(p.x == this.left + this.getWidth() / 2 && p.y == this.bottom)
			&& !(p.x == this.left && p.y == this.top + this.getHeight() / 2)
			&& !(p.x == this.right && p.y == this.top + this.getHeight() / 2)
			&& !(p.x == this.left + this.getWidth() / 2 - 1 && p.y == this.top)
			&& !(p.x == this.left + this.getWidth() / 2 - 1 && p.y == this.bottom)
			&& !(p.x == this.left && p.y == this.top + this.getHeight() / 2 - 1)
			&& !(p.x == this.right && p.y == this.top + this.getHeight() / 2 - 1)) {
			return false;
		}

		return super.canConnect(p);
	}

	@Override
	public int getMinWidth() {
		return 9;
	}

	@Override
	public int getMinHeight() {
		return 9;
	}
}