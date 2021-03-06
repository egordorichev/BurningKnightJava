package org.rexcellentgames.burningknight.entity.creature.fx;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import org.rexcellentgames.burningknight.Settings;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.MathUtils;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;


public class GoreFx extends Entity {
	public TextureRegion texture;
	public boolean menu;
	private float a;
	private float va;
	private Point vel;
	private float z = 8;
	private Body body;
	private float al = 1f;

	{
		alwaysActive = true;
	}

	private float startX;
	private float hz;
	private float wz;

	@Override
	public void init() {
		super.init();

		this.startX = x;
		this.depth = -1;
		this.a = Random.newFloat(360);
		this.va = Random.newFloat(-20f, 20f);

		float hz = Random.newFloat(-1f, 1f);
		float wz = Random.newFloat(-1f, 1f);
		this.vel = new Point((Random.chance(50) ? -1 : 1) * (Math.abs(this.wz) + 1f), 1.5f + hz);
		this.hz = hz * 16f;
		this.wz = wz * 32f;

		if (!this.menu) {
			this.body = World.createSimpleCentredBody(this, 0, 0, this.texture.getRegionWidth(), this.texture.getRegionHeight(), BodyDef.BodyType.DynamicBody, false);
			World.checkLocked(this.body).setTransform(this.x + this.texture.getRegionWidth() / 2, this.y + this.texture.getRegionHeight() / 2, 0);
			this.body.setLinearVelocity(this.vel.x, this.vel.y);
			this.body.setBullet(true);
		}
	}

	private float t;

	@Override
	public void update(float dt) {
		super.update(dt);

		t += dt;

		if (Settings.quality == 0 && this.t >= 5f) {
			this.al -= dt;

			if (this.al <= 0) {
				this.done = true;
			}
		}

		/*if (this.t > 5f && !this.tweened) {
			this.tweened = true;
			Tween.to(new Tween.Task(0, 1f, Tween.Type.QUAD_IN) {
				@Override
				public float getValue() {
					return al;
				}

				@Override
				public void setValue(float value) {
					al = value;
				}

				@Override
				public void onEnd() {
					done = true;
				}
			});
		}*/

		if (this.body != null) {
			this.vel.x = this.body.getLinearVelocity().x;
			this.vel.y = this.body.getLinearVelocity().y;
		}

		//this.va *= (this.z == 0 ? 0.5f : 0.98f);
		this.a += this.va * dt * 60;

		this.x = MathUtils.clamp(this.startX - this.wz, this.startX + this.wz, this.x + this.vel.x * dt * 60);
		this.vel.y -= dt * 8f;

		if (!menu) {
			this.z = Math.max(hz, this.z + this.vel.y * dt * 60);
		} else {
			this.z += this.vel.y * dt * 60;
			if (this.y + this.z < 0) {
				this.done = true;
			}
		}

		this.a += this.va * dt * 60;


		this.va -= this.va * Math.min(1, dt * 3);

		if (Math.abs(this.vel.x) <= 0.1f || this.z == hz) {
			this.vel.x = 0;
			this.va = 0;

			if (this.body != null) {
				this.body = World.removeBody(this.body);
			}
		}

		if (this.body != null) {
			this.body.setLinearVelocity(this.vel.x, this.vel.y);
			World.checkLocked(this.body).setTransform(this.x, this.y + this.z, 0);
		}
	}

	@Override
	public void destroy() {
		super.destroy();
		this.body = World.removeBody(this.body);
	}

	@Override
	public void render() {
		Graphics.batch.setColor(1, 1, 1, this.al);
		Graphics.render(this.texture, this.x, this.y + this.z, this.a, this.texture.getRegionWidth() / 2, this.texture.getRegionHeight() / 2, false, false);
		Graphics.batch.setColor(1, 1, 1, 1);
	}

	@Override
	public boolean shouldCollide(Object entity, Contact contact, Fixture fixture) {
		if (entity != null) {
			return false;
		}

		return super.shouldCollide(entity, contact, fixture);
	}
}