package org.rexellentgames.dungeon.entity.creature.fx;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.creature.buff.BurningBuff;
import org.rexellentgames.dungeon.entity.creature.mob.Mob;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.item.weapon.Weapon;
import org.rexellentgames.dungeon.entity.plant.Plant;
import org.rexellentgames.dungeon.util.Animation;
import org.rexellentgames.dungeon.util.Random;

import java.util.ArrayList;

public class Note extends Entity {
	public static Animation animations = Animation.make("note");
	private TextureRegion region;
	public float a;
	private Vector2 vel;
	private Body body;
	public boolean bad = true;
	private float t;

	@Override
	public void init() {
		super.init();

		this.vel = new Vector2();

		this.y -= 4;

		vel.x = (float) (Math.cos(this.a) * 60);
		vel.y = (float) (Math.sin(this.a) * 60);

		this.body = this.createBody(0, 0, 10, 10, BodyDef.BodyType.DynamicBody, true);
		this.body.setBullet(true);
		this.body.setTransform(this.x, this.y, 0);

		ArrayList<Animation.Frame> frames = animations.getFrames("idle");
		region = frames.get(Random.newInt(frames.size())).frame;
	}

	@Override
	public void onCollision(Entity entity) {
		if (entity instanceof Mob && !this.bad && !((Mob) entity).isDead()) {
			((Mob) entity).modifyHp(-6, true);
			this.done = true;
			// ((Mob) entity).addBuff(new BurningBuff().setDuration(3f));
		} else if (entity instanceof Player && this.bad) {
			((Player) entity).modifyHp(-6, true);
			this.done = true;
			// ((Player) entity).addBuff(new BurningBuff().setDuration(3f));
		} else if (entity instanceof Weapon && this.bad) {
			if (((Weapon) entity).getOwner() instanceof Player) {
				this.done = true;
			}
		} else if (entity == null) {
			this.done = true; // Wall
		}
	}

	@Override
	public void destroy() {
		super.destroy();
		this.body.getWorld().destroyBody(this.body);
	}

	@Override
	public void update(float dt) {
		this.t += dt;

		super.update(dt);

		this.x = this.body.getPosition().x;
		this.y = this.body.getPosition().y;

		this.body.setLinearVelocity(this.vel);

		if (this.t >= 5f) {
			this.done = true;
		}
	}

	@Override
	public void render() {
		Graphics.render(region, this.x, this.y);
	}
}