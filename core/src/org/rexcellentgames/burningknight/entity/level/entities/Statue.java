package org.rexcellentgames.burningknight.entity.level.entities;

import com.badlogic.gdx.math.Rectangle;
import org.rexcellentgames.burningknight.Dungeon;

public class Statue extends SolidProp {
	{
		sprite = "prop (statue A)";
		collider = new Rectangle(4, 10, 7, 10);
	}

	private boolean s;

	@Override
	public void update(float dt) {
		super.update(dt);

		if (!s) {
			s = true;
			Dungeon.level.setPassable((int) (this.x / 16), (int) ((this.y + 8) / 16), false);
			Dungeon.level.setPassable((int) (this.x / 16), (int) ((this.y + 24) / 16), false);
		}
	}
}