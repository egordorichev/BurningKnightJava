package org.rexcellentgames.burningknight;

import com.badlogic.gdx.physics.box2d.*;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.fx.GoreFx;
import org.rexcellentgames.burningknight.entity.creature.fx.HeartFx;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.Bomb;
import org.rexcellentgames.burningknight.entity.item.Gold;
import org.rexcellentgames.burningknight.entity.item.ItemHolder;
import org.rexcellentgames.burningknight.entity.item.pet.impl.PetEntity;
import org.rexcellentgames.burningknight.entity.item.weapon.Weapon;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.bullet.Shell;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.BulletProjectile;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.Projectile;
import org.rexcellentgames.burningknight.entity.item.weapon.yoyo.Yoyo;
import org.rexcellentgames.burningknight.entity.level.entities.Door;
import org.rexcellentgames.burningknight.entity.level.entities.Slab;
import org.rexcellentgames.burningknight.entity.level.entities.SolidProp;

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

		if (a == null && contact.getFixtureA().getBody().isBullet() && b instanceof Player && ((Player) b).flying) {
			contact.setEnabled(false);
		} else if (b == null && contact.getFixtureB().getBody().isBullet() && a instanceof Player && ((Player) a).flying) {
			contact.setEnabled(false);
		} else if (a == null && contact.getFixtureA().getBody().isBullet() && b instanceof Projectile) {
			contact.setEnabled(false);
		} else if (b == null && contact.getFixtureB().getBody().isBullet() && a instanceof Projectile) {
			contact.setEnabled(false);
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
		} else if ((a instanceof BulletProjectile || a instanceof Shell) && b != null) {
			contact.setEnabled(false);
		} else if ((b instanceof BulletProjectile || b instanceof Shell) && a != null) {
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
		} else if (a instanceof HeartFx && !(b instanceof Player) && b != null) {
			contact.setEnabled(false);
		} else if (b instanceof HeartFx && !(a instanceof Player) && a != null) {
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