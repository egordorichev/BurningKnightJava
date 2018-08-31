package org.rexcellentgames.burningknight.entity.item.plant.seed;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.item.consumable.Consumable;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.game.input.Input;

public class Seed extends Consumable {
	@Override
	public void use() {
		super.use();
		int x = Math.round((Input.instance.worldMouse.x - 8) / 16);
		int y = Math.round((Input.instance.worldMouse.y) / 16);

		Dungeon.level.liquidData[Level.toIndex(x, y)] = Terrain.HIGH_GRASS;
		Dungeon.level.updateTile(x, y);
	}

	@Override
	public boolean canBeUsed() {
		int x = Math.round((Input.instance.worldMouse.x - 8) / 16);
		int y = Math.round((Input.instance.worldMouse.y) / 16);

		if (!Dungeon.level.isValid(x, y) || (Dungeon.level.liquidData[Level.toIndex(x, y)] != Terrain.DIRT && Dungeon.level.liquidData[Level.toIndex(x, y)] != Terrain.GRASS)) {
			return false;
		}

		return super.canBeUsed();
	}
}