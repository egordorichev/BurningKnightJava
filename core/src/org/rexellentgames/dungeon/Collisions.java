package org.rexellentgames.dungeon;

import com.badlogic.gdx.physics.box2d.*;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.level.entities.Door;
import org.rexellentgames.dungeon.util.Log;

public class Collisions implements ContactListener {
	@Override
	public void beginContact(Contact contact) {
		Object a = contact.getFixtureA().getBody().getUserData();
		Object b = contact.getFixtureB().getBody().getUserData();

		if (a instanceof Door && b instanceof Entity) {
			((Door) a).setOpen(true);
		} else if (b instanceof Door && a instanceof Entity) {
			((Door) b).setOpen(true);
		}
	}

	@Override
	public void endContact(Contact contact) {
		Object a = contact.getFixtureA().getBody().getUserData();
		Object b = contact.getFixtureB().getBody().getUserData();

		if (a instanceof Door) {
			((Door) a).setOpen(false);
		} else if (b instanceof Door) {
			((Door) b).setOpen(false);
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {

	}
}