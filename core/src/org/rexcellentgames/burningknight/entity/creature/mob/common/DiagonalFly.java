package org.rexcellentgames.burningknight.entity.creature.mob.common;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Animation;

public class DiagonalFly extends Fly {
	public static Animation animations = Animation.make("actor-fly", "-brown");

	{
		hpMax = 5;
		ignoreVel = true;
		mul = 1;
	}

	@Override
	public float getWeight() {
		return 0.5f;
	}

	public Animation getAnimation() {
		return animations;
	}

	@Override
	protected void createBody() {
		body = World.createSimpleBody(this, 6, 6, w - 12, h - 6, BodyDef.BodyType.DynamicBody, false);
		body.getFixtureList().get(0).setRestitution(1f);
		body.setTransform(x, y, 0);

		float f = 32;

		body.setLinearVelocity(new Vector2(-f, f));
	}

	@Override
	public boolean shouldCollide(Object entity, Contact contact, Fixture fixture) {
		return entity == null || entity instanceof Player;
	}
}