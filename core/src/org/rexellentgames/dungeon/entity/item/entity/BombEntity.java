package org.rexellentgames.dungeon.entity.item.entity;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.creature.Creature;
import org.rexellentgames.dungeon.entity.item.Explosion;
import org.rexellentgames.dungeon.game.input.Input;
import org.rexellentgames.dungeon.util.Animation;
import org.rexellentgames.dungeon.util.AnimationData;
import org.rexellentgames.dungeon.util.Random;
import org.rexellentgames.dungeon.util.geometry.Point;

public class BombEntity extends Entity {
	public static Animation animations = Animation.make("actor-bomb");
	private AnimationData animation = animations.get("idle");
	private Body body;
	private Point vel;
	private boolean flip = Random.chance(50);

	public BombEntity(float x, float y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public void init() {
		super.init();

		this.body = this.createBody(2, 2, 12, 12, BodyDef.BodyType.DynamicBody, true);
		this.body.setTransform(this.x, this.y, 0);
	}

	@Override
	public void destroy() {
		super.destroy();
		this.body.getWorld().destroyBody(this.body);
	}

	public BombEntity randomVel() {
		float a = Random.newFloat((float) (Math.PI * 2));
		this.vel = new Point((float) Math.cos(a) * 50f, (float) Math.sin(a) * 50f);

		this.x += Math.cos(a) * 5f;
		this.y += Math.sin(a) * 5f;

		return this;
	}

	public BombEntity toMouseVel() {
		return this.velTo(Input.instance.worldMouse.y - this.y - 8, Input.instance.worldMouse.x - this.x - 8);
	}

	public BombEntity velTo(float x, float y) {
		float a = (float) Math.atan2(y - this.y - 8, x - this.x - 8);
		this.vel = new Point((float) Math.cos(a) * 50f, (float) Math.sin(a) * 50f);

		this.x += Math.cos(a) * 5f;
		this.y += Math.sin(a) * 5f;

		return this;
	}

	@Override
	public void update(float dt) {
		this.x = this.body.getPosition().x;
		this.y = this.body.getPosition().y;

		this.vel.mul(0.9f);
		this.body.setLinearVelocity(this.vel);

		if (this.animation.update(dt)) {
			this.done = true;
			Dungeon.area.add(new Explosion(this.x + 8, this.y + 8));

			for (int i = 0; i < Dungeon.area.getEntities().size(); i++) {
				Entity entity = Dungeon.area.getEntities().get(i);

				if (entity instanceof Creature) {
					Creature creature = (Creature) entity;

					if (creature.getDistanceTo(this.x + 8, this.y + 8) < 24f) {
						creature.modifyHp(-50, true);
					}
				}
			}
		}
	}

	@Override
	public void render() {
		this.animation.render(this.x, this.y, this.flip);
	}
}