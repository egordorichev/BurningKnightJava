package org.rexellentgames.dungeon.entity.creature.fx;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.creature.Creature;
import org.rexellentgames.dungeon.entity.creature.mob.Mob;
import org.rexellentgames.dungeon.entity.creature.player.Player;

import java.util.ArrayList;

public class FireRectFx extends Entity {
	{
		depth = -1;
		alwaysActive = true;
	}

	private float t;
	private Body body;
	private ArrayList<Creature> colliding = new ArrayList<Creature>();

	@Override
	public void init() {
		super.init();

		this.body = this.createBody(2, 2, 12, 12, BodyDef.BodyType.DynamicBody, true);
		this.body.setTransform(this.x, this.y, 0);
	}

	@Override
	public void update(float dt) {
		super.update(dt);
		this.t += dt;

		if (this.t >= 2.5f) {
			for (Creature creature : this.colliding) {
				if (creature instanceof Player) {
					creature.modifyHp(-10, true);
				} else if (creature instanceof Mob) {
					creature.modifyHp(5);
				}
			}

			this.done = true;
		}
	}

	@Override
	public void destroy() {
		this.body.getWorld().destroyBody(this.body);
	}

	@Override
	public void render() {
		// Graphics.render(Graphics.effects, (int) (9 + Math.floor(this.t)), this.x, this.y);
	}

	@Override
	public void onCollision(Entity entity) {
		if (entity instanceof Creature) {
			this.colliding.add((Creature) entity);
		}
	}

	@Override
	public void onCollisionEnd(Entity entity) {
		if (entity instanceof Creature) {
			this.colliding.remove((Creature) entity);
		}
	}
}