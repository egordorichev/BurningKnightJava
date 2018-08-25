package org.rexcellentgames.burningknight.entity.item.tool;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.item.consumable.Consumable;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.game.input.Input;

public class LavaBucket extends Consumable {
	@Override
	public void use() {
		super.use();

		int x = Math.round((Input.instance.worldMouse.x - 8) / 16);
		int y = Math.round((Input.instance.worldMouse.y) / 16);

		for (int yy = -1; yy < 2; yy++) {
			for (int xx = -1; xx < 2; xx++) {
				if (Math.abs(yy) + Math.abs(xx) < 2) {
					int nx = x + xx;
					int ny = y + yy;

					if (!Dungeon.level.isValid(nx, ny) || !Dungeon.level.checkFor(nx, ny, Terrain.PASSABLE)) {
						continue;
					}

					Dungeon.level.set(nx, ny, Terrain.LAVA);
					Dungeon.level.updateTile(nx, ny);
				}
			}
		}
	}

	@Override
	public boolean canBeUsed() {
		int x = Math.round((Input.instance.worldMouse.x - 8) / 16);
		int y = Math.round((Input.instance.worldMouse.y) / 16);

		if (!Dungeon.level.isValid(x, y) || !Dungeon.level.checkFor(x, y, Terrain.PASSABLE)) {
			return false;
		}

		return super.canBeUsed();
	}
}