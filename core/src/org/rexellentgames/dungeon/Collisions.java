package org.rexellentgames.dungeon;

import com.badlogic.gdx.physics.box2d.*;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.creature.Creature;
import org.rexellentgames.dungeon.entity.item.ItemHolder;
import org.rexellentgames.dungeon.entity.item.weapon.Weapon;
import org.rexellentgames.dungeon.entity.item.weapon.ranged.Arrow;
import org.rexellentgames.dungeon.entity.item.weapon.ranged.ArrowEntity;
import org.rexellentgames.dungeon.entity.level.entities.Door;

public class Collisions implements ContactListener, ContactFilter {
	@Override
	public void beginContact(Contact contact) {
		Entity a = (Entity) contact.getFixtureA().getBody().getUserData();
		Entity b = (Entity) contact.getFixtureB().getBody().getUserData();

		if (a != null) {
			a.onCollision(b);
		}

		if (b != null) {
			b.onCollision(a);
		}
	}

	@Override
	public void endContact(Contact contact) {
		Entity a = (Entity) contact.getFixtureA().getBody().getUserData();
		Entity b = (Entity) contact.getFixtureB().getBody().getUserData();

		if (a != null) {
			a.onCollisionEnd(b);
		}

		if (b != null) {
			b.onCollisionEnd(a);
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		Object a = contact.getFixtureA().getBody().getUserData();
		Object b = contact.getFixtureB().getBody().getUserData();

		if (a == null && b instanceof ArrowEntity) {
			((ArrowEntity) b).done = true;
		} else if (b == null && a instanceof ArrowEntity) {
			((ArrowEntity) a).done = true;
		} else if (a instanceof Creature && b instanceof Creature) {
			contact.setEnabled(false);
		} else if ((a instanceof Creature && b instanceof ItemHolder) || (b instanceof Creature && a instanceof ItemHolder)) {
			contact.setEnabled(false);
		} else if (a instanceof Creature && b instanceof Weapon) {
			Weapon weapon = (Weapon) b;

			if (weapon.getOwner() == a) {
				contact.setEnabled(false);
			}
		} else if (b instanceof Creature && a instanceof Weapon) {
			Weapon weapon = (Weapon) a;

			if (weapon.getOwner() == b) {
				contact.setEnabled(false);
			}
		} else if (a instanceof Door) {
			if (!((Door) a).lock) {
				contact.setEnabled(false);
			}
		} else if (b instanceof Door) {
			if (!((Door) b).lock) {
				contact.setEnabled(false);
			}
		} else if (a instanceof ArrowEntity) {
			if (((ArrowEntity) a).owner == b) {
				contact.setEnabled(false);
			}
		} else if (b instanceof ArrowEntity) {
			if (((ArrowEntity) b).owner == a) {
				contact.setEnabled(false);
			}
		}
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {

	}

	@Override
	public boolean shouldCollide(Fixture fixtureA, Fixture fixtureB) {
		return true;
	}
}