package org.rexellentgames.dungeon.entity.item.weapon.ranged;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.creature.Creature;
import org.rexellentgames.dungeon.entity.creature.mob.Mob;
import org.rexellentgames.dungeon.entity.item.Item;
import org.rexellentgames.dungeon.entity.item.ItemHolder;
import org.rexellentgames.dungeon.util.Random;

public class ArrowEntity extends Entity {
	private static TextureRegion sprite = Graphics.getTexture("item (arrow)");
	public float a;
	private Body body;
	private Vector2 vel;
	public Creature owner;
	public int damage;
	private boolean noDrop;

	@Override
	public void destroy() {
		super.destroy();

		this.body.getWorld().destroyBody(this.body);
	}

	@Override
	public void init() {
		super.init();

		BodyDef def = new BodyDef();
		def.type = BodyDef.BodyType.DynamicBody;

		body = Dungeon.world.createBody(def);
		PolygonShape poly = new PolygonShape();

		int w = sprite.getRegionWidth();
		int h = sprite.getRegionHeight();

		poly.set(new Vector2[]{
			new Vector2((float) Math.floor((double) -w / 2), -h / 2), new Vector2((float) Math.ceil((double) w / 2), -h / 2),
			new Vector2((float) Math.floor((double) -w / 2), h / 2), new Vector2((float) Math.ceil((double) w / 2), h / 2)
		});

		FixtureDef fixture = new FixtureDef();

		fixture.shape = poly;
		fixture.friction = 0;

		body.createFixture(fixture);
		body.setUserData(this);
		body.setBullet(true);
		poly.dispose();

		this.body.setTransform(this.x, this.y, this.a);

		// todo: charge == high speed
		float s = 5f;
		this.vel = new Vector2((float) Math.cos(this.a) * s, (float) Math.sin(this.a) * s);
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (this.done) {
			if (!noDrop && Random.chance(50)) {
				Item arrow = new Arrow();

				ItemHolder holder = new ItemHolder();
				holder.setItem(arrow);

				holder.x = this.x - 8 - this.vel.x * 4;
				holder.y = this.y - 4 - this.vel.y * 4;

				Dungeon.area.add(holder);
				Dungeon.level.addSaveable(holder);
			}
		}

		this.x += this.vel.x;
		this.y += this.vel.y;

		this.body.setLinearVelocity(this.vel);
		this.body.setTransform(this.x, this.y, this.a);
	}

	@Override
	public void onCollision(Entity entity) {
		if (entity instanceof Mob) {
			((Mob) entity).modifyHp(-this.damage);
			this.done = true;
			this.noDrop = true;
		}
	}

	@Override
	public void render() {
		Graphics.render(sprite, this.x, this.y, (float) Math.toDegrees(this.a), sprite.getRegionWidth() / 2, sprite.getRegionHeight() / 2, false, false);
	}
}