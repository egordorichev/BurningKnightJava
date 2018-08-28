package org.rexcellentgames.burningknight.entity.creature.fx;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.buff.PoisonBuff;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.Random;

import java.util.ArrayList;

public class PoisonFx extends Entity {
	private static ArrayList<Animation.Frame> animations = Animation.make("poison").getFrames("idle");
	private float a;
	private float r;
	private float g;
	private float b;
	private TextureRegion region;
	private float al;
	private Body body;
	private float t;

	{
		depth = -9;
	}

	@Override
	public void init() {
		super.init();

		this.r = Random.newFloat(0f, 0.05f);
		this.a = Random.newFloat(360);
		this.g = Random.newFloat(0.4f, 0.8f);
		this.b = Random.newFloat(0, 0.05f);
		this.region = animations.get(Random.newInt(animations.size())).frame;

		this.body = World.createCircleCentredBody(this, 0, 0, 12, BodyDef.BodyType.StaticBody, true);
		this.body.setTransform(this.x + 16, this.y + 16, 0);
	}

	@Override
	public void destroy() {
		super.destroy();
		this.body = World.removeBody(this.body);
	}

	@Override
	public void onCollision(Entity entity) {
		super.onCollision(entity);

		if (entity instanceof Creature && !((Creature) entity).isFlying()) {
			((Creature) entity).addBuff(new PoisonBuff().setDuration(2f));
		}
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		this.t += dt;

		if (this.t >= 10f) {
			this.al = Math.max(0f, this.al - dt);

			if (this.al == 0) {
				this.done = true;
			}
		} else if (this.al < 1f) {
			this.al = Math.min(1f, this.al + dt * 3f);
		}
	}

	@Override
	public void render() {
		Graphics.batch.setColor(this.r, this.g, this.b, 0.8f);
		Graphics.render(region, this.x + 16, this.y + 16, this.a, 16, 16, false, false, this.al, this.al);
		Graphics.batch.setColor(1, 1, 1, 1);
	}
}