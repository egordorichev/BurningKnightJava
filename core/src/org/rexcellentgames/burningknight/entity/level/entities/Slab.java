package org.rexcellentgames.burningknight.entity.level.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.item.ItemHolder;

public class Slab extends SolidProp {
	{
		sprite = "props-slab_a";
		collider = new Rectangle(3, 10, 8, 1);
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

	@Override
	public boolean shouldCollide(Object entity, Contact contact, Fixture fixture) {
		if (entity instanceof ItemHolder) {
			((ItemHolder) entity).depth = 1;
			return false;
		}

		return super.shouldCollide(entity, contact, fixture);
	}
}