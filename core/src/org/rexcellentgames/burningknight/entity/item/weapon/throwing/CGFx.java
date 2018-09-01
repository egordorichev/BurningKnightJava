package org.rexcellentgames.burningknight.entity.item.weapon.throwing;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.fx.Confetti;
import org.rexcellentgames.burningknight.entity.item.Explosion;
import org.rexcellentgames.burningknight.entity.item.Smoke;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.BulletProjectile;
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
		World.checkLocked(this.body).setTransform(this.x, this.y, this.a);
		this.body.setLinearVelocity(this.vel.x, this.vel.y);

		this.va = Random.newFloat(-1f, 1f) * 360f;
	}

	@Override
	public void onCollision(Entity entity) {
		super.onCollision(entity);

		if (entity instanceof Mob && !((Mob) entity).isFlying()) {
			this.doExplode = true;
		}
	}

	private boolean doExplode;

	@Override
	public void update(float dt) {
		if (this.doExplode) {
			this.explode();
			return;
		}

		super.update(dt);

		this.x = this.body.getPosition().x;
		this.y = this.body.getPosition().y;

		this.a += this.va * dt;
		this.va *= 0.99f;

		this.vel.x = this.body.getLinearVelocity().x;
		this.vel.y = this.body.getLinearVelocity().y;

		this.vel.x *= 0.99f;
		this.vel.y *= 0.99f;

		World.checkLocked(this.body).setTransform(this.x, this.y, (float) Math.toRadians(this.a));
		this.body.setLinearVelocity(this.vel.x, this.vel.y);

		this.t += dt;

		if (this.t >= 5f) {
			explode();
		}
	}

	private void explode() {
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

		for (int i = 0; i < 16; i++) {
			BulletProjectile bullet = new BulletProjectile();

			float f = 60;
			float a = (float) (i * (Math.PI / 8));

			bullet.letter = "a";
			bullet.x = (float) (this.x + Math.cos(a) * 8);
			bullet.y = (float) (this.y + Math.sin(a) * 8);
			bullet.velocity.x = (float) (Math.cos(a) * f);
			bullet.velocity.y = (float) (Math.sin(a) * f);

			Dungeon.area.add(bullet);
		}
	}

	@Override
	public void destroy() {
		super.destroy();
		this.body = World.removeBody(this.body);
	}

	@Override
	public void render() {
		float sx = (float) (Math.cos(this.t * 16) / 4) + 1;
		float sy = (float) (Math.cos(this.t * 16 + Math.PI) / 5) + 1;

		Graphics.render(region, this.x, this.y, this.a, this.w / 2, this.h / 2, false, false,
			sx, sy);
	}

	@Override
	public boolean shouldCollide(Object entity, Contact contact, Fixture fixture) {
		if (entity instanceof Creature) {
			return false;
		}

		return super.shouldCollide(entity, contact, fixture);
	}
}