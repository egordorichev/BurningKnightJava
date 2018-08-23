package org.rexcellentgames.burningknight;

import com.badlogic.gdx.physics.box2d.*;
import org.rexcellentgames.burningknight.entity.Entity;

public class Collisions implements ContactListener, ContactFilter {
	public static Fixture last;

	@Override
	public void beginContact(Contact contact) {
		Entity a = (Entity) contact.getFixtureA().getBody().getUserData();
		Entity b = (Entity) contact.getFixtureB().getBody().getUserData();

		if (a == null && contact.getFixtureA().getBody().isBullet()) {
			return;
		} else if (b == null && contact.getFixtureB().getBody().isBullet()) {
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

		if (a instanceof Entity && b instanceof Entity) {
			Entity ae = (Entity) a;
			Entity be = (Entity) b;

			if (!ae.shouldCollide(be, contact) || !be.shouldCollide(ae, contact)) {
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