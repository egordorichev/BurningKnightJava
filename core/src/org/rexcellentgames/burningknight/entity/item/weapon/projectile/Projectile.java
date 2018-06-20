package org.rexcellentgames.burningknight.entity.item.weapon.projectile;

import com.badlogic.gdx.physics.box2d.Body;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.NetworkedEntity;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.fx.BloodFx;
import org.rexcellentgames.burningknight.entity.creature.fx.HpFx;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.physics.World;

public class Projectile extends NetworkedEntity {
	public Creature owner;
	public int damage;
	public boolean bad;
	public boolean crit;
	public float knockback = 200f;

	protected boolean broke;
	protected Body body;

	@Override
	public void init() {
		super.init();
		this.bad = this.owner instanceof Mob;
	}

	@Override
	public void destroy() {
		super.destroy();
		this.body = World.removeBody(this.body);
	}

	private boolean didDie;

	@Override
	public void update(float dt) {
		super.update(dt);

		if (broke) {
			if (!didDie) {
				didDie = true;
				this.death();
			}

			return;
		}

		if (this.body != null) {
			this.x = this.body.getPosition().x;
			this.y = this.body.getPosition().y;
		}

		this.logic(dt);

		if (this.body != null) {
			this.body.setLinearVelocity(this.vel.x, this.vel.y);
		}
	}

	@Override
	public void onCollision(Entity entity) {
		if (this.broke) {
			return;
		}

		if (this.breaksFrom(entity)) {
			this.broke = true;
			return;
		}

		if (this.hit(entity)) {
			this.broke = true;
		}
	}

	protected boolean breaksFrom(Entity entity) {
		return false;
	}

	protected boolean hit(Entity entity) {
		return false;
	}

	protected void doHit(Entity entity) {
		HpFx fx = ((Creature) entity).modifyHp(-this.damage, this.owner);
		((Creature) entity).knockBack(this.owner, this.knockback);

		if (fx != null) {
			if (this.crit) {
				fx.crit = true;
			}

			BloodFx.add(entity, 10);
			this.onHit(entity);
		}
	}

	protected void logic(float dt) {

	}

	protected void onHit(Entity entity) {

	}

	protected void death() {
		this.done = true;
	}
}