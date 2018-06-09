package org.rexellentgames.dungeon;

import com.badlogic.gdx.physics.box2d.*;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.creature.Creature;
import org.rexellentgames.dungeon.entity.creature.fx.GoreFx;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.item.Bomb;
import org.rexellentgames.dungeon.entity.item.Gold;
import org.rexellentgames.dungeon.entity.item.ItemHolder;
import org.rexellentgames.dungeon.entity.item.pet.impl.PetEntity;
import org.rexellentgames.dungeon.entity.item.weapon.Weapon;
import org.rexellentgames.dungeon.entity.item.weapon.bow.arrows.ArrowEntity;
import org.rexellentgames.dungeon.entity.item.weapon.gun.bullet.BulletEntity;
import org.rexellentgames.dungeon.entity.item.weapon.gun.bullet.Shell;
import org.rexellentgames.dungeon.entity.item.weapon.yoyo.Yoyo;
import org.rexellentgames.dungeon.entity.level.entities.Door;
import org.rexellentgames.dungeon.entity.level.entities.Slab;
import org.rexellentgames.dungeon.entity.level.entities.SolidProp;

public class Collisions implements ContactListener, ContactFilter {
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

		if (a == null && contact.getFixtureA().getBody().isBullet() && b instanceof Player && ((Player) b).flying) {
			contact.setEnabled(false);
		} else if (b == null && contact.getFixtureB().getBody().isBullet() && a instanceof Player && ((Player) a).flying) {
			contact.setEnabled(false);
		} else if (a == null && b instanceof ArrowEntity) {
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
		} else if ((a instanceof BulletEntity || a instanceof Shell) && b != null) {
			contact.setEnabled(false);
		} else if ((b instanceof BulletEntity || b instanceof Shell) && a != null) {
			contact.setEnabled(false);
		} else if ((a instanceof GoreFx) && b != null) {
			contact.setEnabled(false);
		} else if ((b instanceof GoreFx) && a != null) {
			contact.setEnabled(false);
		} else if (a instanceof Slab && b instanceof ItemHolder) {
			contact.setEnabled(false);
			((ItemHolder) b).depth = 1;
		} else if (b instanceof Slab && a instanceof ItemHolder) {
			contact.setEnabled(false);
			((ItemHolder) a).depth = 1;
		} else if ((a instanceof Bomb) && b != null) {
			contact.setEnabled(false);
		} else if ((b instanceof Bomb) && a != null) {
			contact.setEnabled(false);
		} else if (a instanceof PetEntity && (b instanceof Creature || b instanceof Door || b instanceof SolidProp || (b instanceof ItemHolder && !(((ItemHolder) b).getItem() instanceof Gold)))) {
			contact.setEnabled(false);
		} else if (b instanceof PetEntity && (a instanceof Creature || a instanceof Door || a instanceof SolidProp || (a instanceof ItemHolder && !(((ItemHolder) a).getItem() instanceof Gold)))) {
			contact.setEnabled(false);
		} else if (a instanceof Yoyo && b instanceof ItemHolder) {
			contact.setEnabled(false);
		} else if (b instanceof Yoyo && a instanceof ItemHolder) {
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