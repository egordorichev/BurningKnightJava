package org.rexcellentgames.burningknight.entity.level.rooms.regular;

import org.rexcellentgames.burningknight.entity.level.features.Door;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Patch;
import org.rexcellentgames.burningknight.entity.level.features.Door;
import org.rexcellentgames.burningknight.util.BArray;
import org.rexcellentgames.burningknight.util.PathFinder;

public class PatchRoom extends RegularRoom {
	protected boolean[] patch;

	protected void setupPatch(Level level, float fill, int clustering, boolean ensurePath) {
		if (ensurePath) {
			PathFinder.setMapSize(this.getWidth() - 2, this.getHeight() - 2);
			boolean valid = false;

			for (int j = 0; j < 100; j++) {
				patch = Patch.generate(this.getWidth() - 2, this.getHeight() - 2, fill, clustering);

				int startPoint = 0;
				for (Door door : connected.values()) {
					if (door.x == left) {
						startPoint = xyToPatchCoords((int) door.x + 1, (int) door.y);
						patch[xyToPatchCoords((int) door.x + 1, (int) door.y)] = false;
						patch[xyToPatchCoords((int) door.x + 2, (int) door.y)] = false;
					} else if (door.x == right) {
						startPoint = xyToPatchCoords((int) door.x - 1, (int) door.y);
						patch[xyToPatchCoords((int) door.x - 1, (int) door.y)] = false;
						patch[xyToPatchCoords((int) door.x - 2, (int) door.y)] = false;
					} else if (door.y == top) {
						startPoint = xyToPatchCoords((int) door.x, (int) door.y + 1);
						patch[xyToPatchCoords((int) door.x, (int) door.y + 1)] = false;
						patch[xyToPatchCoords((int) door.x, (int) door.y + 2)] = false;
					} else if (door.y == bottom) {
						startPoint = xyToPatchCoords((int) door.x, (int) door.y - 1);
						patch[xyToPatchCoords((int) door.x, (int) door.y - 1)] = false;
						patch[xyToPatchCoords((int) door.x, (int) door.y - 2)] = false;
					}
				}

				PathFinder.buildDistanceMap(startPoint, BArray.not(patch, null));

				valid = true;

				for (int i = 0; i < patch.length; i++) {
					if (!patch[i] && PathFinder.distance[i] == Integer.MAX_VALUE) {
						valid = false;
						break;
					}
				}

				if (valid) {
					break;
				}
			}

			if (!valid) {
				setupPatch(level, fill / 2, clustering, ensurePath);
			}

			PathFinder.setMapSize(Level.getWidth(), Level.getHeight());
		} else {
			patch = Patch.generate(this.getWidth() - 2, this.getHeight() - 2, fill, clustering);
		}
	}

	protected void cleanDiagonalEdges() {
		if (patch == null) return;

		int pWidth = this.getWidth() - 2;

		for (int i = 0; i < patch.length - pWidth; i++) {
			if (!patch[i]) continue;

			if (i % pWidth != 0) {
				if (patch[i - 1 + pWidth] && !(patch[i - 1] || patch[i + pWidth])) {
					patch[i - 1 + pWidth] = false;
				}
			}

			if ((i + 1) % pWidth != 0) {
				if (patch[i + 1 + pWidth] && !(patch[i + 1] || patch[i + pWidth])) {
					patch[i + 1 + pWidth] = false;
				}
			}

		}
	}

	protected int xyToPatchCoords(int x, int y) {
		return (x - left - 1) + ((y - top - 1) * (this.getWidth() - 2));
	}
}