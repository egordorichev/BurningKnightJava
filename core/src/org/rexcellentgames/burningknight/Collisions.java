package org.rexcellentgames.burningknight;

import com.badlogic.gdx.physics.box2d.*;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.mob.hall.DashingKnight;
import org.rexcellentgames.burningknight.entity.level.Level;

public class Collisions implements ContactListener, ContactFilter {
	public static Fixture last;

	@Override
	public void beginContact(Contact contact) {
		Entity a = (Entity) contact.getFixtureA().getBody().getUserData();
		Entity b = (Entity) contact.getFixtureB().getBody().getUserData();

		if (a instanceof Level && !(b instanceof DashingKnight)) {
			return;
		} else if (b instanceof Level && !(a instanceof DashingKnight)) {
			return;
		}

		if (a != null) {
			last = contact.getFixtureB();
			a.onCollision(b);
		}

		if (b != null) {
			last = contact.getFixtureA();
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

		boolean should = true;

		if (a instanceof Entity && !((Entity) a).shouldCollide(b, contact, contact.getFixtureB())) {
			should = false;
		}

		if (b instanceof Entity && !((Entity) b).shouldCollide(a, contact, contact.getFixtureA())) {
			should = false;
		}

		if (!should) {
			contact.setEnabled(false);
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