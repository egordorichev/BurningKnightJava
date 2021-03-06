package org.rexcellentgames.burningknight.entity.item.weapon.projectile;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.StatefulEntity;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.fx.HpFx;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.entities.fx.PoofFx;
import org.rexcellentgames.burningknight.physics.World;

public class Projectile extends StatefulEntity {
	public Creature owner;
	public int damage = 1;
	public boolean bad;
	public boolean crit;
	public float knockback = 2f;
	public boolean penetrates;

	protected boolean broke;
	public Body body;

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

	protected boolean didDie;
	public static boolean allDie;

	@Override
	public void update(float dt) {
		super.update(dt);

		if (allDie && bad) {
			broke = true;
		}

		if (broke) {
			if (!didDie) {
				didDie = true;
				this.death();
			}

			return;
		}

		if (this.body != null && !ignoreBodyPos) {
			this.x = this.body.getPosition().x;
			this.y = this.body.getPosition().y;
		}

		this.logic(dt);

		if (this.body != null && !this.ignoreVel) {
			//float a = (float) Math.atan2(this.velocity.y, this.velocity.x);
			//this.body.setLinearVelocity(((float) Math.cos(a)) * 128, ((float) Math.sin(a)) * 128);

			// this.body.setLinearVelocity(this.velocity.x, this.velocity.y);
		}
	}

	public boolean ignoreBodyPos;

	public void setPos(float x, float y) {
		this.x = x;
		this.y = y;

		if (this.body != null) {
			World.checkLocked(this.body).setTransform(x, y, this.body.getAngle());
		}
	}

	protected boolean ignoreVel;

	public void brak() {
		this.broke = true;
	}

	@Override
	public void onCollision(Entity entity) {
		if (this.broke) {
			return;
		}

		if (this.breaksFrom(entity)) {
			this.brak();
			return;
		}

		if (this.hit(entity)) {
			this.broke = !this.penetrates;

			if (this.owner != null && this.owner.penetrates) {
				this.broke = false;
			}
		}
	}

	public void remove() {
		broke = true;
	}

	public int i;

	protected boolean breaksFrom(Entity entity) {
		return false;
	}

	protected boolean hit(Entity entity) {
		return false;
	}

	protected boolean ignoreArmor = false;

	protected void doHit(Entity entity) {
		HpFx fx = ((Creature) entity).modifyHp(-this.damage, this, this.ignoreArmor);
		((Creature) entity).knockBackFrom(this.owner, this.knockback);
		Graphics.delay();

		if (fx != null) {
			if (this.crit) {
				fx.crit = true;
			}
		}

		this.onHit(entity);
	}

	protected void logic(float dt) {

	}

	protected void onHit(Entity entity) {

	}

	protected void death() {
		if (done) {
			return;
		}

		this.done = true;
		this.onDeath();

		if (!noPoof) {
			for (int i = 0; i < 3; i++) {
				PoofFx fx = new PoofFx();

				fx.x = this.x;
				fx.y = this.y;
				fx.t += 0.5f;

				Dungeon.area.add(fx);
			}
		}
	}

	public boolean noPoof;

	protected void onDeath() {

	}

	@Override
	public boolean shouldCollide(Object entity, Contact contact, Fixture fixture) {
		if (entity instanceof Level || (entity instanceof Projectile && ((Projectile) entity).bad == this.bad)) {
			return false;
		}

		return super.shouldCollide(entity, contact, fixture);
	}
}