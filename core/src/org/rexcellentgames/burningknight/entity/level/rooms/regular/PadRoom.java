package org.rexcellentgames.burningknight.entity.level.rooms.regular;

import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;
import org.rexcellentgames.burningknight.util.geometry.Rect;

public class PadRoom extends RegularRoom {
	private int topRightW;
	private int topRightH;
	private int topLeftW;
	private int topLeftH;
	private int bottomRightW;
	private int bottomRightH;
	private int bottomLeftW;
	private int bottomLeftH;

	@Override
	public Rect resize(int w, int h) {
		Rect rect = super.resize(w, h);

		int min = 3;
		int maxW = this.getWidth() / 3 + 2;
		int maxH = this.getHeight() / 3 + 2;

		topRightW = Random.newInt(min, maxW);
		topRightH = Random.newInt(min, maxH);
		topLeftW = Random.newInt(min, maxW);
		topLeftH = Random.newInt(min, maxH);
		bottomRightW = Random.newInt(min, maxW);
		bottomRightH = Random.newInt(min, maxH);
		bottomLeftW = Random.newInt(min, maxW);
		bottomLeftH = Random.newInt(min, maxH);

		return rect;
	}

	@Override
	public void paint(Level level) {
		super.paint(level);

		boolean below = Random.chance(50);

		Painter.fill(level, this, 1, Terrain.CHASM);

		if (below) {
			this.paintTunnel(level, Terrain.randomFloor());
		}

		Painter.fill(level, new Rect(this.left + 1, this.top + 1, this.left + 1 + topLeftW, this.top + 1 + topLeftH), Terrain.randomFloor());
		Painter.fill(level, new Rect(this.right - topRightW, this.top + 1, this.right, this.top + 1 + topRightH), Terrain.randomFloor());

		Painter.fill(level, new Rect(this.left + 1, this.bottom - this.bottomLeftH, this.left + 1 + bottomLeftW, this.bottom), Terrain.randomFloor());
		Painter.fill(level, new Rect(this.right - bottomRightW, this.bottom - this.bottomRightH, this.right, this.bottom), Terrain.randomFloor());

		Rect rect = new Rect(this.left + Math.min(this.topLeftW, this.bottomLeftW),
			this.top + Math.min(this.topLeftH, this.topRightH),
			this.right - Math.min(this.topRightW, this.bottomRightW) + 1,
			this.bottom - Math.min(this.bottomLeftH, this.bottomRightH) + 1);

		Painter.fill(level, rect, Terrain.randomFloor());

		if (Random.chance(50)) {
			Painter.fillEllipse(level, rect, Random.newInt(1, 3), Terrain.randomFloor());
		} else {
			Painter.fill(level, rect, Random.newInt(1, 3), Terrain.randomFloor());
		}
		
		if (!below) {
			this.paintTunnel(level, Terrain.randomFloor());
		}
	}

	@Override
	protected Point getDoorCenter() {
		return getCenter();
	}
}