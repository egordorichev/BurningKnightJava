package org.rexcellentgames.burningknight.entity.item.weapon.throwing;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.MassData;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.fx.Confetti;
import org.rexcellentgames.burningknight.entity.item.Explosion;
import org.rexcellentgames.burningknight.entity.item.Smoke;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Random;

public class CGFx extends Entity {
	private Body body;
	private TextureRegion region = Graphics.getTexture("item-confetti_grenade");
	private float a;
	private float va;
	public Vector2 vel = new Vector2();
	private float t;

	{
		w = 6;
		h = 10;
	}

	@Override
	public void init() {
		super.init();

		this.body = World.createSimpleCentredBody(this, 0, 0, w, h, BodyDef.BodyType.DynamicBody, false, 0.8f);
		MassData data = new MassData();
		data.mass = 0.1f;
		this.body.setMassData(data);
		this.body.setTransform(this.x, this.y, this.a);
		this.body.setLinearVelocity(this.vel.x, this.vel.y);

		this.va = Random.newFloat(-1f, 1f) * 360f;
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		this.x = this.body.getPosition().x;
		this.y = this.body.getPosition().y;

		this.a += this.va * dt;
		this.va *= 0.99f;

		this.vel.x = this.body.getLinearVelocity().x;
		this.vel.y = this.body.getLinearVelocity().y;

		this.vel.x *= 0.99f;
		this.vel.y *= 0.99f;

		this.body.setTransform(this.x, this.y, (float) Math.toRadians(this.a));
		this.body.setLinearVelocity(this.vel.x, this.vel.y);

		this.t += dt;

		if (this.t >= 5f) {
			this.done = true;

			float x = this.x + this.w / 2;
			float y = this.y + this.h / 2;

			for (int i = 0; i < 50; i++) {
				float an = Random.newFloat((float) (Math.PI * 2));

				Confetti fx = new Confetti();

				fx.x = x;
				fx.y = y;

				float f = Random.newFloat(40, 80f);

				fx.vel.x = (float) Math.cos(an) * f;
				fx.vel.y = (float) Math.sin(an) * f;

				Dungeon.area.add(fx);
			}

			Explosion explosion = new Explosion(x, y);
			Dungeon.area.add(explosion);

			Smoke smoke = new Smoke(x, y + 8);
			smoke.delay = 0.2f;
			Dungeon.area.add(smoke);
		}
	}

	@Override
	public void destroy() {
		super.destroy();
		this.body = World.removeBody(this.body);
	}

	@Override
	public void render() {
		float v = 0;

		Graphics.render(region, this.x, this.y, this.a, this.w / 2, this.h / 2, false, false,
			1f + v, 1f + v);
	}

	@Override
	public boolean shouldCollide(Entity entity, Contact contact) {
		if (entity instanceof Creature) {
			return false;
		}

		return super.shouldCollide(entity, contact);
	}
}