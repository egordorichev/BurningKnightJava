package org.rexcellentgames.burningknight.entity.item.weapon;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.fx.HpFx;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.ItemHolder;
import org.rexcellentgames.burningknight.entity.level.entities.Door;
import org.rexcellentgames.burningknight.entity.level.entities.Entrance;
import org.rexcellentgames.burningknight.entity.level.entities.SolidProp;
import org.rexcellentgames.burningknight.entity.trap.RollingSpike;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class Weapon extends WeaponBase {
	protected Body body;
	private boolean used = false;
	protected float added;

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
		fixture.isSensor = false;
		fixture.filter.categoryBits = 0x0002;
		fixture.filter.groupIndex = -1;
		fixture.filter.maskBits = -1;

		body.createFixture(fixture);
		body.setUserData(this);
		body.setBullet(true);
		body.setTransform(this.owner.x, this.owner.y, 0);
		poly.dispose();

		played = false;
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

	private boolean played;

	protected float getAngle(Entity entity) {
		if (entity == null) {
			Point aim = this.owner.getAim();
			return this.owner.getAngleTo(aim.x, aim.y);
		}

		return this.owner.getAngleTo(entity.x + entity.w / 2, entity.y + entity.h / 2);
	}

	protected void knockFrom(Entity entity) {
		float a = (float) (getAngle(entity) + Math.PI);

		float knockbackMod = this.owner.getStat("knockback");
		float force = 30f;

		this.owner.knockback.x += Math.cos(a) * force * knockbackMod;
		this.owner.knockback.y += Math.sin(a) * force * knockbackMod;
	}

	@Override
	public void onCollision(Entity entity) {
		if (!played) {
			if (entity == null) {
				this.owner.playSfx("clink_1");
				played = true;
				this.knockFrom(entity);
				return;
			} else if (entity instanceof RollingSpike || entity instanceof SolidProp || entity instanceof Entrance) {
				this.owner.playSfx("clink_2");
				played = true;
				this.knockFrom(entity);
				return;
			}
		}

		if (entity instanceof Creature && entity != this.owner) {
			if (this.used && (!this.penetrates && !this.owner.penetrates)) {
				return;
			}

			Creature creature = (Creature) entity;

			if (creature.isDead() || creature.isUnhittable()) {
				return;
			}

			creature.knockBackFrom(this.owner, 2);

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
		}
	}


	@Override
	public boolean shouldCollide(Object entity, Contact contact, Fixture fixture) {
		if ((entity == null && fixture.getBody().isBullet()) || entity == owner || entity instanceof Door || entity instanceof ItemHolder) {
			return false;
		}

		return super.shouldCollide(entity, contact, fixture);
	}
}