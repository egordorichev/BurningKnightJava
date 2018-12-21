package org.rexcellentgames.burningknight.entity.level.entities;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.item.entity.BombEntity;

public class DoorListener extends Entity {
	public Door door;

	@Override
	public boolean shouldCollide(Object entity, Contact contact, Fixture fixture) {
		if (entity instanceof BombEntity || entity instanceof Mob) {
			return true;
		}

		return false;
	}
}