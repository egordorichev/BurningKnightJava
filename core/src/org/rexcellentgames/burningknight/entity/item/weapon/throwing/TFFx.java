package org.rexcellentgames.burningknight.entity.item.weapon.throwing;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.level.entities.fx.PoofFx;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Random;

public class TFFx extends Entity {
	private Vector2 vel = new Vector2();
	private static TextureRegion region = Graphics.getTexture("item-bottle_a");
	private float a;
	private float va;
	private float z;
	private float yv;
	private Body body;

	public void to(float angle) {
		float s = 40f;

		this.vel.x = (float) Math.cos(angle) * s;
		this.yv = (float) Math.sin(angle) * s;
		this.vel.y = 80f;
		this.va = Random.newFloat(300, 400) * (this.vel.x >= 0 ? 1 : -1);
		this.z = 16;
	}

	@Override
	public void init() {
		super.init();

		this.body = World.createSimpleCentredBody(this, 0, 0, 10, 13, BodyDef.BodyType.DynamicBody, true);
		this.body.setTransform(this.x, this.y, 0);
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		this.x += this.vel.x * dt;
		this.z += this.vel.y * dt;
		this.y += this.yv * dt;

		this.vel.y -= dt * 160;

		this.a += this.va * dt;
		this.va *= 0.999f;

		if (this.z <= 0) {
			this.done = true;
			parts();
		}

		this.body.setTransform(this.x, this.y + this.z, (float) Math.toRadians(this.a));

		this.depth = this.z > 5f ? 11 : 0;
	}

	private void parts() {
		for (int i = 0; i < 10; i++) {
			PoofFx fx = new PoofFx();

			fx.x = this.x;
			fx.y = this.y;

			Dungeon.area.add(fx);
		}

		this.depth = 0;
	}

	@Override
	public void render() {
		Graphics.render(region, this.x, this.y + this.z, this.a, 5, 6, false, false);
	}

	@Override
	public void onCollision(Entity entity) {
		super.onCollision(entity);

		if (this.z <= 5f && entity == null) {
			done = true;
			parts();
		}
	}

	@Override
	public void destroy() {
		super.destroy();
		this.body = World.removeBody(this.body);
	}
}