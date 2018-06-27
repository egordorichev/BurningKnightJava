package org.rexcellentgames.burningknight.entity.level.rooms.secret;

import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.entities.chest.Chest;
import org.rexcellentgames.burningknight.entity.level.save.LevelSave;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.entities.chest.Chest;
import org.rexcellentgames.burningknight.entity.level.save.LevelSave;

public class ChestRoom extends SecretRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		Chest chest = Chest.random();

		chest.x = this.left * 16 + (this.getWidth() * 16 - chest.w) / 2;
		chest.y = this.top * 16 + (this.getHeight() * 16 - chest.h) / 2;
		chest.setItem(chest.generate());

		Dungeon.area.add(chest);
		LevelSave.add(chest);
	}
}