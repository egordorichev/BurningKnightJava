package org.rexellentgames.dungeon.entity.level.entities;

import com.badlogic.gdx.math.Rectangle;
import org.rexellentgames.dungeon.Dungeon;

public class Slab extends SolidProp {
	{
		sprite = "prop (slab A)";
		collider = new Rectangle(1, 10, 12, 1);
	}

	private boolean s;

	@Override
	public void update(float dt) {
		super.update(dt);

		if (!s) {
			s = true;
			Dungeon.level.setPassable((int) (this.x / 16), (int) ((this.y + 8) / 16), false);
		}
	}
}