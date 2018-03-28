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
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.item.Item;

public class Weapon extends Item {
	protected Body body;
	protected int damage = 1;
	protected float knockback = 10f;
	protected boolean penetrates = false;
	private boolean used = false;
	protected float added;

	@Override
	public void use() {
		super.use();

		if (this.owner instanceof Player) {
			((Player) this.owner).heat += 0.2f;
		}

		this.createHitbox();
	}

	@Override
	public void secondUse() {
		super.secondUse();

		this.createHitbox();
	}

	protected void createHitbox() {
		BodyDef def = new BodyDef();
		def.type = BodyDef.BodyType.DynamicBody;

		body = Dungeon.world.createBody(def);
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
		poly.dispose();
	}

	@Override
	public void endUse() {
		if (this.body != null) {
			this.body.getWorld().destroyBody(this.body);
			this.body = null;
		}

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

	@Override
	public StringBuilder buildInfo() {
		StringBuilder builder = super.buildInfo();

		builder.append('\n');
		builder.append(this.damage);
		builder.append(" damage");

		return builder;
	}
}