package org.rexcellentgames.burningknight.entity.level.rooms.regular;

import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class RombRoom extends RegularRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		double h = getHeight();
		double w = getWidth();
		int hh = getHeight() / 2;
		int ww = getWidth() / 2;

		byte s = Terrain.CHASM;

		Painter.drawLine(level, new Point(this.left + 1, this.top + (int) Math.floor(h / 2) - 1),
			new Point(this.left + (int) Math.floor(w / 2) - 1, this.top + 1), s);
		Painter.drawLine(level, new Point(this.left + 1, this.top + (int) Math.ceil(h / 2) + 1),
			new Point(this.left + (int) Math.floor(w / 2) - 1, this.bottom - 1), s);

		Painter.drawLine(level, new Point(this.right - 1, this.top + (int) Math.floor(h / 2) - 1),
			new Point(this.right - (int) Math.floor((w - 0.5) / 2) + 1, this.top + 1), s);
		Painter.drawLine(level, new Point(this.right - 1, this.top + (int) Math.ceil(h / 2) + 1),
			new Point(this.right - (int) Math.floor((w - 0.5) / 2) + 1, this.bottom - 1), s);
	}

	@Override
	public boolean canConnect(Point p) {
		if (!(p.x == this.left + this.getWidth() / 2 && p.y == this.top)
			&& !(p.x == this.left + this.getWidth() / 2 && p.y == this.bottom)
			&& !(p.x == this.left && p.y == this.top + this.getHeight() / 2)
			&& !(p.x == this.right && p.y == this.top + this.getHeight() / 2)) {
			return false;
		}

		return super.canConnect(p);
	}
}