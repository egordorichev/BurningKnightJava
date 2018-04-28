package org.rexellentgames.dungeon.entity.item.weapon.gun.bullet;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.creature.Creature;
import org.rexellentgames.dungeon.util.geometry.Point;

public class BulletEntity extends Entity {
	public TextureRegion sprite;
	public float a;
	public Point vel;
	private Body body;
	private float ra;
	public int damage;

	@Override
	public void init() {
		this.ra = (float) Math.toRadians(this.a);

		this.body = this.createCentredBody(0, 0, sprite.getRegionWidth(), sprite.getRegionHeight(), BodyDef.BodyType.DynamicBody, true);
		this.body.setTransform(this.x, this.y, ra);
		this.body.setBullet(true);
	}

	@Override
	public void onCollision(Entity entity) {
		if (entity == null) {
			this.done = true; // todo: better anim
		} else if (entity instanceof Creature) {
			((Creature) entity).modifyHp(-this.damage);
		}
	}

	@Override
	public void destroy() {
		body.getWorld().destroyBody(body);
		this.body = null;
	}

	@Override
	public void render() {
		Graphics.render(sprite, this.x, this.y, this.a, sprite.getRegionWidth() / 2, sprite.getRegionHeight() / 2, false, false);
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		this.x += this.vel.x;
		this.y += this.vel.y;

		this.body.setTransform(this.x, this.y, this.ra);
		this.body.setLinearVelocity(this.vel);
	}
}