package org.rexellentgames.dungeon.entity.level.rooms.secret;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.entities.chest.Chest;

public class ChestRoom extends SecretRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		Chest chest = Chest.random();

		chest.x = this.left * 16 + (this.getWidth() * 16 - chest.w) / 2;
		chest.y = this.top * 16 + (this.getHeight() * 16 - chest.h) / 2;
		chest.setItem(chest.generate());

		Dungeon.area.add(chest);
		Dungeon.level.addSaveable(chest);
	}
}