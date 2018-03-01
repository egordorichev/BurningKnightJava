package org.rexellentgames.dungeon.entity.item.weapon;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.creature.Creature;
import org.rexellentgames.dungeon.entity.creature.mob.Mob;
import org.rexellentgames.dungeon.entity.item.Item;

public class Weapon extends Item {
	protected Body body;
	protected int damage = 1;
	protected float knockback = 10f;
	protected boolean penetrates = false;
	private boolean used = false;

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

	@Override
	public void update(float dt) {
		super.update(dt);

		if (this.body != null) {
			this.body.setTransform(this.owner.x + (this.owner.isFlipped() ? -8 : 8), this.owner.y - 8, 0);
		}
	}

	protected void createHitbox() {
		BodyDef def = new BodyDef();
		def.type = BodyDef.BodyType.DynamicBody;

		body = Dungeon.world.createBody(def);
		PolygonShape poly = new PolygonShape();

		int w = (int) this.owner.w;
		int h = (int) (this.owner.h * 2);

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

		this.body.setTransform(this.owner.x + (this.owner.isFlipped() ? -this.owner.w / 2 : this.owner.w / 2), this.owner.y - this.owner.h / 2, 0);
	}

	@Override
	public void endUse() {
		this.body.getWorld().destroyBody(this.body);
		this.body = null;
		this.used = false;
	}

	@Override
	public void onCollision(Entity entity) {
		if (entity instanceof Creature && entity != this.owner) {
			if (this.used && !this.penetrates) {
				return;
			}

			this.used = true;

			Creature creature = (Creature) entity;

			if (creature.isDead() || ((creature instanceof Mob && this.owner instanceof Mob))) {
				return;
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