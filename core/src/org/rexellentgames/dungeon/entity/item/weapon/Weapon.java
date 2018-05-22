package org.rexellentgames.dungeon.entity.item.weapon;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.creature.Creature;
import org.rexellentgames.dungeon.entity.creature.mob.Mob;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.physics.World;
import org.rexellentgames.dungeon.util.Random;

public class Weapon extends WeaponBase {
	protected Body body;
	protected boolean penetrates = false;
	private boolean used = false;
	protected float added;

	public Weapon() {
		minDamage = damage / 3 * 2;
	}

	{
		identified = true;
	}

	public boolean isBlocking() {
		return false;
	}

	@Override
	public void use() {
		super.use();

		if (this.owner instanceof Player) {
			((Player) this.owner).heat += 0.2f;
		}

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
			if (this.used && !this.penetrates) {
				return;
			}

			Creature creature = (Creature) entity;

			if (creature.isDead()) {
				return;
			}

			float dx = creature.x + creature.w / 2 - this.owner.x - this.owner.w / 2;
			float dy = creature.y + creature.h / 2 - this.owner.y - this.owner.h / 2;
			double a = Math.atan2(dy, dx);

			creature.vel.x += Math.cos(a) * this.knockback * 50;
			creature.vel.y += Math.sin(a) * this.knockback * 50;

			if (this.isBlocking()) {
				return;
			}

			if (creature.isDead() || ((creature instanceof Mob && this.owner instanceof Mob && !((Mob) this.owner).stupid))) {
				return;
			}

			this.used = true;
			this.onHit(creature);

			creature.modifyHp(-Math.max(creature.getDefense() + 1, Math.round(Random.newFloat(this.minDamage, this.damage))), this.owner);
		} else if (entity instanceof Weapon) {
			if (this.isBlocking()) {
				Weapon weapon = ((Weapon) entity);

				weapon.used = true;

				Creature creature = this.owner;

				float dx = creature.x + creature.w / 2 - this.owner.x - this.owner.w / 2;
				float dy = creature.y + creature.h / 2 - this.owner.y - this.owner.h / 2;
				double a = Math.atan2(dy, dx);

				creature.vel.x += Math.cos(a) * this.knockback * 50;
				creature.vel.y += Math.sin(a) * this.knockback * 50;
			}
		}
	}

	@Override
	public StringBuilder buildInfo() {
		StringBuilder builder = super.buildInfo();

		builder.append("\n[orange]");
		builder.append(this.damage);
		builder.append(" damage[gray]");

		if (this.penetrates) {
			builder.append("\n[green]Can hit multiple targets[gray]");
		}

		return builder;
	}
}