package org.rexellentgames.dungeon.entity.item.weapon;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.creature.Creature;
import org.rexellentgames.dungeon.entity.item.Item;

public class Weapon extends Item {
	protected Body body;
	protected int damage = 1;
	protected float knockback = 10f;

	@Override
	public void use() {
		super.use();

		this.createHitbox();
	}

	@Override
	public void secondUse() {
		super.secondUse();

		this.createHitbox();
	}

	protected void createHitbox() {
		World world = this.owner.getArea().getState().getWorld();

		BodyDef def = new BodyDef();
		def.type = BodyDef.BodyType.DynamicBody;

		body = world.createBody(def);
		PolygonShape poly = new PolygonShape();

		int w = 16;
		int h = 24;

		poly.set(new Vector2[]{
			new Vector2(0, 0), new Vector2(w, 0),
			new Vector2(0, h), new Vector2(w, h)
		});

		FixtureDef fixture = new FixtureDef();

		fixture.shape = poly;
		fixture.friction = 0;
		fixture.isSensor = true;

		body.createFixture(fixture);
		body.setUserData(this);
		poly.dispose();

		this.body.setTransform(this.owner.x + (this.owner.isFlipped() ? -8 : 8), this.owner.y - 8, 0);
	}

	@Override
	public void endUse() {
		this.body.getWorld().destroyBody(this.body);
		this.body = null;
	}

	@Override
	public void onCollision(Entity entity) {
		if (entity instanceof Creature && entity != this.owner) {
			Creature creature = (Creature) entity;

			if (creature.isDead()) {

			}

			creature.modifyHp(-this.damage);

			float dx = creature.x + creature.w / 2 - this.owner.x - this.owner.w / 2;
			float dy = creature.y + creature.h / 2 - this.owner.y - this.owner.h / 2;
			double a = Math.atan2(dy, dx);

			creature.vel.x += Math.cos(a) * this.knockback * 50;
			creature.vel.y += Math.sin(a) * this.knockback * 50;
		}
	}
}