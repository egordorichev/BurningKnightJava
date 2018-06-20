package org.rexcellentgames.burningknight.entity.item.weapon;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.fx.HpFx;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.physics.World;

public class Weapon extends WeaponBase {
	protected Body body;
	protected boolean penetrates = false;
	private boolean used = false;
	protected float added;

	{
		identified = true;
	}

	public boolean isBlocking() {
		return false;
	}

	@Override
	public void use() {
		super.use();

		if (this.body != null) {
			this.body = World.removeBody(this.body);
		}

		this.createHitbox();
	}

	@Override
	public void secondUse() {
		super.secondUse();
	}

	protected void createHitbox() {
		BodyDef def = new BodyDef();
		def.type = BodyDef.BodyType.DynamicBody;

		body = World.world.createBody(def);
		PolygonShape poly = new PolygonShape();

		int w = this.region.getRegionWidth();
		int h = this.region.getRegionHeight();

		poly.set(new Vector2[]{
			new Vector2((float) Math.floor((double) -w / 2), 0), new Vector2((float) Math.ceil((double) w / 2), 0),
			new Vector2((float) Math.floor((double) -w / 2), h), new Vector2((float) Math.ceil((double) w / 2), h)
		});

		FixtureDef fixture = new FixtureDef();

		fixture.shape = poly;
		fixture.friction = 0;
		fixture.isSensor = true;

		body.createFixture(fixture);
		body.setUserData(this);
		body.setBullet(true);
		poly.dispose();
	}

	@Override
	public void endUse() {
		this.body = World.removeBody(this.body);
		this.used = false;
	}

	@Override
	public void destroy() {
		super.destroy();
		this.body = World.removeBody(this.body);
	}

	public void setAdded(float added) {
		this.added = added;
	}

	public void onHit(Creature creature) {

	}

	@Override
	public void onCollision(Entity entity) {
		if (entity instanceof Creature && entity != this.owner) {
			if (this.used && (!this.penetrates && !this.owner.penetrates)) {
				return;
			}

			Creature creature = (Creature) entity;

			if (creature.isDead()) {
				return;
			}

			float dx = creature.x + creature.w / 2 - this.owner.x - this.owner.w / 2;
			float dy = creature.y + creature.h / 2 - this.owner.y - this.owner.h / 2;
			double a = Math.atan2(dy, dx);

			creature.vel.x += Math.cos(a) * this.knockback * 50 * creature.knockbackMod;
			creature.vel.y += Math.sin(a) * this.knockback * 50 * creature.knockbackMod;

			if (this.isBlocking()) {
				return;
			}

			if (creature.isDead() || ((creature instanceof Mob && this.owner instanceof Mob && !((Mob) this.owner).stupid))) {
				return;
			}

			this.used = true;
			this.onHit(creature);

			int damage = -Math.max(creature.getDefense() + 1, this.rollDamage());

			if (this.modifier != null) {
				damage = this.modifier.modDamage(damage);
			}

			HpFx fx = creature.modifyHp(damage, this.owner);

			if (fx != null && lastCrit) {
				fx.crit = true;
			}

			if (this.modifier != null) {
				this.modifier.onHit((Player) this.owner, creature, damage);
			}
		} else if (entity instanceof Weapon) {
			if (this.isBlocking()) {
				Weapon weapon = ((Weapon) entity);

				weapon.used = true;

				Creature creature = this.owner;

				float dx = creature.x + creature.w / 2 - this.owner.x - this.owner.w / 2;
				float dy = creature.y + creature.h / 2 - this.owner.y - this.owner.h / 2;
				double a = Math.atan2(dy, dx);

				creature.vel.x += Math.cos(a) * this.knockback * 50 * creature.knockbackMod;
				creature.vel.y += Math.sin(a) * this.knockback * 50 * creature.knockbackMod;
			}
		}
	}

	@Override
	public StringBuilder buildInfo() {
		StringBuilder builder = super.buildInfo();

		builder.append("\n[orange]");
		builder.append(this.damage);
		builder.append(" damage[gray]");

		if (this.critChance + this.owner.critChance != 4f) {
			builder.append("\n[orange]");
			builder.append((int) Math.floor(this.critChance + this.owner.critChance));
			builder.append("% crit chance[gray]");
		}

		if (this.modifier != null) {
			this.modifier.apply(builder);
		}

		if (this.penetrates || this.owner.penetrates) {
			builder.append("\n[green]Can hit multiple targets[gray]");
		}

		return builder;
	}
}