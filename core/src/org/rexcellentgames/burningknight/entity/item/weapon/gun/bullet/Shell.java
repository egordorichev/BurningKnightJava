package org.rexcellentgames.burningknight.entity.item.weapon.gun.bullet;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import org.rexcellentgames.burningknight.Settings;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.Tween;
import org.rexcellentgames.burningknight.util.geometry.Point;

import java.util.ArrayList;

public class Shell extends Entity {
	private static Animation animations = Animation.make("fx-shell");
	private TextureRegion sprite;
	public Point vel;
	private float z = 10f;
	private Body body;
	private float va;
	private float a;
	private float al = 1f;
	private float t;
	private boolean tweened;

	@Override
	public void init() {
		super.init();

		this.depth = -1;
		this.va = Random.newFloat(this.vel.x * 10);

		ArrayList<Animation.Frame> frames = animations.getFrames("idle");
		this.sprite = frames.get(Random.newInt(frames.size())).frame;

		if (Settings.quality > 1) {
			this.body = World.createSimpleCentredBody(this, 0, 0, this.sprite.getRegionWidth(), this.sprite.getRegionHeight(), BodyDef.BodyType.DynamicBody, false);
			World.checkLocked(this.body).setTransform(this.x + this.sprite.getRegionWidth() / 2, this.y + this.sprite.getRegionHeight() / 2, 0);
			this.body.setLinearVelocity(this.vel.x, this.vel.y);
			this.body.setBullet(true);
		}
	}

	@Override
	public void destroy() {
		super.destroy();
		this.body = World.removeBody(this.body);
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (this.body != null) {
			this.vel.x = this.body.getLinearVelocity().x;
			this.vel.y = this.body.getLinearVelocity().y;
		} else {
			this.t += dt;

			if (this.t >= 10f && !this.tweened) {
				this.tweened = true;
				Tween.to(new Tween.Task(0, 3f, Tween.Type.QUAD_IN) {
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
						super.onEnd();
						done = true;
					}
				});
			}
		}


		this.vel.x -= this.vel.x * Math.min(1, this.z == 0 ? dt * 2 : dt * 3);
		this.va -= va * Math.min(1, z == 0 ? dt * 2 : dt * 3);
		this.a += this.va * dt * 60;

		if (this.vel.x <= 0.1f && this.z == 0) {
			this.vel.x = 0;

			if (this.body != null) {
				playSfx("shell");
				this.body = World.removeBody(this.body);
			}
		}

		this.x += this.vel.x;
		this.z = Math.max(0, this.z + this.vel.y * dt * 60);
		this.vel.y -= dt * 5;

		if (this.body != null) {
			this.body.setLinearVelocity(this.vel.x, this.vel.y);
			World.checkLocked(this.body).setTransform(this.x, this.y + this.z, 0);
		}
	}

	@Override
	public void render() {
		Graphics.batch.setColor(1, 1, 1, this.al);
		Graphics.render(this.sprite, this.x, this.y + this.z, this.a, this.sprite.getRegionWidth() / 2, this.sprite.getRegionHeight() / 2, false, false);
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