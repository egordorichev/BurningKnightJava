package org.rexcellentgames.burningknight.entity.level.rooms.regular;

import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class SmallAdditionRoom extends RegularRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		Painter.drawLine(level, new Point(this.left + this.getWidth() / 2 - 2, this.top + 1), new Point(this.left + this.getWidth() / 2 - 2, this.bottom - 1), Terrain.WALL);
		Painter.drawLine(level, new Point(this.left + this.getWidth() / 2 + 2, this.top + 1), new Point(this.left + this.getWidth() / 2 + 2, this.bottom - 1), Terrain.WALL);
		Painter.fill(level, this, 2, Terrain.randomFloor());
	}

	@Override
	public boolean canConnect(Point p) {
		if ((p.x == this.left + this.getWidth() / 2 - 2 && p.y == this.top)
			|| (p.x == this.left + this.getWidth() / 2 - 2 && p.y == this.bottom)
			|| (p.x == this.left + this.getWidth() / 2 + 2 && p.y == this.top)
			|| (p.x == this.left + this.getWidth() / 2 + 2 && p.y == this.bottom)) {

			return false;
		}

		return super.canConnect(p);
	}

	@Override
	public int getMinWidth() {
		return 10;
	}

	@Override
	public int getMinHeight() {
		return 10;
	}
}