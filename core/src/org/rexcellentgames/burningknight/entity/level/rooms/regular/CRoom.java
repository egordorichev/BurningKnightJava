package org.rexcellentgames.burningknight.entity.level.rooms.regular;

import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;
import org.rexcellentgames.burningknight.util.geometry.Rect;

public class CRoom extends RegularRoom {
	private enum Type {
		TOP, RIGHT, LEFT, BOTTOM
	}

	private Type type;

	public CRoom() {
		type = Type.values()[Random.newInt(4)];
	}

	@Override
	public void paint(Level level) {
		super.paint(level);

		Rect rect = new Rect();
		rect.resize(Random.newInt(this.getWidth() / 4, this.getWidth() / 3 * 2), Random.newInt(this.getHeight() / 4, this.getHeight() / 3 * 2));

		switch (this.type) {
			case TOP:
					rect.setPos(Random.newInt(this.left + 2, this.right - 1 - rect.getWidth()), this.top + 1);
				break;
			case BOTTOM:
				rect.setPos(Random.newInt(this.left + 2, this.right - 1 - rect.getWidth()), this.bottom - rect.getHeight());
				break;
			case LEFT:
				rect.setPos(this.left + 1, Random.newInt(this.top + 2, this.bottom - 1 - rect.getHeight()));
				break;
			case RIGHT:
				rect.setPos(this.right - rect.getWidth(), Random.newInt(this.top + 2, this.bottom - 1 - rect.getHeight()));
				break;
		}

		if (Random.chance(50)) {
			Painter.fill(level, this, 2 + Random.newInt(3), Terrain.CHASM);
		}

		if (Random.chance(50)) {
			paintTunnel(level, Terrain.randomFloor());
		}

		boolean wall = Random.chance(50);
		Painter.fill(level, rect, wall ? Terrain.WALL : Terrain.CHASM);

		if (Random.chance(50)) {
			Painter.fill(level, rect, 1 + Random.newInt(2), wall ? Terrain.CHASM : Terrain.WALL);
		}

		if (Random.chance(10)) {
			paintTunnel(level, Terrain.randomFloor());
		}
	}

	@Override
	public boolean canConnect(Point p) {
		if (this.type == Type.TOP && p.y == this.top) {
			return false;
		}

		if (this.type == Type.BOTTOM && p.y == this.bottom) {
			return false;
		}

		if (this.type == Type.LEFT && p.x == this.left) {
			return false;
		}

		if (this.type == Type.RIGHT && p.x == this.right) {
			return false;
		}

		return super.canConnect(p);
	}
}