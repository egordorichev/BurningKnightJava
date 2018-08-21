package org.rexcellentgames.burningknight.entity.level.rooms.special;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.entities.Button;

public class ButtonPuzzleRoom extends SpecialRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		Button button = new Button();

		button.x = this.left * 16 + 16;
		button.y = this.top * 16 + 16;

		Dungeon.area.add(button.add());
	}
}