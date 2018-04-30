package org.rexellentgames.dungeon.entity.item.weapon.bow;

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
import org.rexellentgames.dungeon.entity.creature.fx.BloodFx;
import org.rexellentgames.dungeon.entity.creature.mob.Mob;
import org.rexellentgames.dungeon.entity.item.Item;
import org.rexellentgames.dungeon.entity.item.ItemHolder;
import org.rexellentgames.dungeon.entity.item.weapon.gun.bullet.Part;
import org.rexellentgames.dungeon.physics.World;
import org.rexellentgames.dungeon.util.Random;

public class ArrowEntity extends Entity {
	public float a;
	private Body body;
	private Vector2 vel;
	public Creature owner;
	public int damage;
	private boolean noDrop;
	public Class<? extends Arrow> type;
	public TextureRegion sprite;

	@Override
	public void destroy() {
		super.destroy();
		this.body = World.removeBody(this.body);
	}

	@Override
	public void init() {
		super.init();

		int w = sprite.getRegionWidth();
		int h = sprite.getRegionHeight();

		this.body = World.createSimpleCentredBody(this, 0, 0, w, h, BodyDef.BodyType.DynamicBody, true);
		this.body.setBullet(true);
		this.body.setTransform(this.x, this.y, this.a);

		// todo: charge == high speed
		float s = 5f;
		this.vel = new Vector2((float) Math.cos(this.a) * s, (float) Math.sin(this.a) * s);
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (this.done) {
			for (int i = 0; i < 20; i++) {
				Part part = new Part();

				part.x = this.x - this.vel.x;
				part.y = this.y - this.vel.y;

				Dungeon.area.add(part);
			}

			if (!noDrop && Random.chance(50)) {
				try {
					Item arrow = this.type.newInstance();

					ItemHolder holder = new ItemHolder();

					holder.auto = true;
					holder.setItem(arrow);

					holder.x = this.x - 8 - this.vel.x;
					holder.y = this.y - 4 - this.vel.y;

					Dungeon.area.add(holder);
					Dungeon.level.addSaveable(holder);
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
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
			Creature creature = ((Creature) entity);

			creature.vel.x += this.vel.x;
			creature.vel.y += this.vel.y;

			creature.modifyHp(-this.damage);
			this.done = true;
			this.noDrop = true;

			BloodFx.add(entity, 10);
		}
	}

	@Override
	public void render() {
		Graphics.startShadows();
		Graphics.render(sprite, this.x, this.y - 8, (float) Math.toDegrees(this.a), sprite.getRegionWidth() / 2, sprite.getRegionHeight() / 2, false, false);
		Graphics.endShadows();
		Graphics.render(sprite, this.x, this.y, (float) Math.toDegrees(this.a), sprite.getRegionWidth() / 2, sprite.getRegionHeight() / 2, false, false);
	}
}