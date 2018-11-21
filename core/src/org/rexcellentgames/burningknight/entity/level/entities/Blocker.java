package org.rexcellentgames.burningknight.entity.level.entities;

import com.badlogic.gdx.math.Rectangle;
import org.rexcellentgames.burningknight.entity.creature.player.Player;

public class Blocker extends SolidProp {
	{
		collider = new Rectangle(0, 10, 29, 5);
		sprite = "props-blocker";
		w = 32;
		h = 16;
	}

	@Override
	public void init() {
		super.init();
		Player.ladder = this;
	}
}