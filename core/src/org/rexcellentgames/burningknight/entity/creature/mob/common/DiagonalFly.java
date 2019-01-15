package org.rexcellentgames.burningknight.entity.creature.mob.common;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.level.entities.Door;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class DiagonalFly extends Fly {
	public static Animation animations = Animation.make("actor-fly", "-brown");
	public Animation getAnimation() {
		return animations;
	}

	{
		hpMax = 5;
		ignoreVel = true;
		mul = 1;
	}

	@Override
	public float getWeight() {
		return 0.5f;
	}

	protected boolean stop;
	private Vector2 lastVel;

	@Override
	protected void createBody() {
		body = World.createSimpleBody(this, 3, 3, w - 6, h - 6, BodyDef.BodyType.DynamicBody, false);
		body.getFixtureList().get(0).setRestitution(1f);
		body.setTransform(x, y, 0);

		float f = 32;

		this.velocity = new Point(f * (Random.chance(50) ? -1 : 1), f * (Random.chance(50) ? -1 : 1));

		body.setLinearVelocity(this.velocity);
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (this.body != null) {
			this.velocity.x = this.body.getLinearVelocity().x;
			this.velocity.y = this.body.getLinearVelocity().y;

			if (lastVel == null && stop) {
				lastVel = new Vector2(velocity.x, velocity.y);
			} else if (!stop && lastVel != null) {
				velocity.x = lastVel.x;
				velocity.y = lastVel.y;
				lastVel = null;
			}

			if (stop) {
				this.body.setLinearVelocity(0, 0);
			} else {
				float a = (float) Math.atan2(this.velocity.y, this.velocity.x);
				this.body.setLinearVelocity(((float) Math.cos(a)) * 32 * Mob.speedMod + knockback.x * 0.2f, ((float) Math.sin(a)) * 32 * Mob.speedMod + knockback.y * 0.2f);
			}
		}
	}

	@Override
	public boolean shouldCollide(Object entity, Contact contact, Fixture fixture) {
		return entity == null || entity instanceof Player || entity instanceof Door;
	}
}