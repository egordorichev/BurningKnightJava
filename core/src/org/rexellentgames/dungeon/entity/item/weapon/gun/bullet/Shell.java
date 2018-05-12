package org.rexellentgames.dungeon.entity.item.weapon.gun.bullet;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.physics.World;
import org.rexellentgames.dungeon.util.Animation;
import org.rexellentgames.dungeon.util.Random;
import org.rexellentgames.dungeon.util.Tween;
import org.rexellentgames.dungeon.util.geometry.Point;

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

		this.va = Random.newFloat(this.vel.x * 10);

		ArrayList<Animation.Frame> frames = animations.getFrames("idle");
		this.sprite = frames.get(Random.newInt(frames.size())).frame;
		this.body = World.createSimpleCentredBody(this, 0, 0, this.sprite.getRegionWidth(), this.sprite.getRegionHeight(), BodyDef.BodyType.DynamicBody, false);
		this.body.setTransform(this.x + this.sprite.getRegionWidth() / 2, this.y + this.sprite.getRegionHeight() / 2, 0);
		this.body.setLinearVelocity(this.vel.x, this.vel.y);
		this.body.setBullet(true);
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
				});
			}
		}

		this.vel.x *= (this.z == 0 ? 0.5f : 0.98f);
		this.va *= (this.z == 0 ? 0.5f : 0.98f);
		this.a += this.va;

		if (this.vel.x <= 0.1f && this.z == 0) {
			this.vel.x = 0;

			if (this.body != null) {
				this.body = World.removeBody(this.body);
			}
		}

		this.x += this.vel.x;
		this.z = Math.max(0, this.z + this.vel.y);
		this.vel.y -= 0.1f;

		if (this.body != null) {
			this.body.setLinearVelocity(this.vel.x, this.vel.y);
			this.body.setTransform(this.x, this.y + this.z, 0);
		}
	}

	@Override
	public void render() {
		Graphics.batch.setColor(1, 1, 1, this.al);
		Graphics.startShadows();
		Graphics.render(this.sprite, this.x, this.y - this.sprite.getRegionHeight(), -this.a, this.sprite.getRegionWidth() / 2, this.sprite.getRegionHeight() / 2, false, false);
		Graphics.endShadows();
		Graphics.render(this.sprite, this.x, this.y + this.z, this.a, this.sprite.getRegionWidth() / 2, this.sprite.getRegionHeight() / 2, false, false);
		Graphics.batch.setColor(1, 1, 1, 1);
	}
}