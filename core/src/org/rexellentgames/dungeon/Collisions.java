package org.rexellentgames.dungeon;

import com.badlogic.gdx.physics.box2d.*;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.creature.Creature;

public class Collisions implements ContactListener, ContactFilter {
	@Override
	public void beginContact(Contact contact) {
		Entity a = (Entity) contact.getFixtureA().getBody().getUserData();
		Entity b = (Entity) contact.getFixtureB().getBody().getUserData();

		if (a != null && b != null) {
			a.onCollision(b);
			b.onCollision(a);
		}
	}

	@Override
	public void endContact(Contact contact) {
		Entity a = (Entity) contact.getFixtureA().getBody().getUserData();
		Entity b = (Entity) contact.getFixtureB().getBody().getUserData();

		if (a != null && b != null) {
			a.onCollisionEnd(b);
			b.onCollisionEnd(a);
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {

	}

	@Override
	public boolean shouldCollide(Fixture fixtureA, Fixture fixtureB) {
		Object a = fixtureA.getBody().getUserData();
		Object b = fixtureB.getBody().getUserData();

		return !(a instanceof Creature && b instanceof Creature);
	}
}