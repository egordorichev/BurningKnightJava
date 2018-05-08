package org.rexellentgames.dungeon.entity.creature.fx;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.physics.World;
import org.rexellentgames.dungeon.util.Random;
import org.rexellentgames.dungeon.util.Tween;
import org.rexellentgames.dungeon.util.geometry.Point;


public class GoreFx extends Entity {
	public TextureRegion texture;
	public boolean menu;
	private float a;
	private float va;
	private Point vel;
	private float z = 8;
	private Body body;
	private float t;
	private boolean tweened;
	private float al = 1f;

	@Override
	public void init() {
		super.init();

		this.depth = -1;
		this.a = Random.newFloat(360);
		this.va = Random.newFloat(-20f, 20f);

		this.vel = new Point(Random.newFloat(-3f, 3f), 3f);

		if (!this.menu) {
			this.body = World.createSimpleCentredBody(this, 0, 0, this.texture.getRegionWidth(), this.texture.getRegionHeight(), BodyDef.BodyType.DynamicBody, false);
			this.body.setTransform(this.x + this.texture.getRegionWidth() / 2, this.y + this.texture.getRegionHeight() / 2, 0);
			this.body.setLinearVelocity(this.vel.x, this.vel.y);
			this.body.setBullet(true);
		}
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		this.t += dt;

		if (this.t > 5f && !this.tweened) {
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
		}

		if (this.body != null) {
			this.vel.x = this.body.getLinearVelocity().x;
			this.vel.y = this.body.getLinearVelocity().y;
		}

		this.vel.x *= (this.z == 0 ? 0.5f : 0.98f);
		this.va *= (this.z == 0 ? 0.5f : 0.98f);
		this.a += this.va;

		this.x += this.vel.x;
		this.vel.y -= 0.1f;

		if (!menu) {
			this.z = Math.max(0, this.z + this.vel.y);
		} else {
			this.z += this.vel.y;
			if (this.y + this.z < 0) {
				this.done = true;
			}
		}

		this.vel.y -= 0.05;

		this.a += this.va;
		this.va *= 0.95f;

		this.vel.x *= 0.97f;

		if (this.vel.x <= 0.1f && this.z == 0) {
			this.vel.x = 0;

			if (this.body != null) {
				this.body = World.removeBody(this.body);
			}
		}

		if (this.body != null) {
			this.body.setLinearVelocity(this.vel.x, this.vel.y);
			this.body.setTransform(this.x, this.y + this.z, 0);
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

		if (!menu) {
			Graphics.startShadows();
			Graphics.render(this.texture, this.x, this.y - 4, this.a, this.texture.getRegionWidth() / 2, this.texture.getRegionHeight() / 2, false, false, 1f, -1f);
			Graphics.endShadows();
		}

		Graphics.render(this.texture, this.x, this.y + this.z, this.a, this.texture.getRegionWidth() / 2, this.texture.getRegionHeight() / 2, false, false);
		Graphics.batch.setColor(1, 1, 1, 1);
	}
}