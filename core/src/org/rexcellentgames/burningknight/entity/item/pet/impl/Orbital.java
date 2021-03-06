package org.rexcellentgames.burningknight.entity.item.pet.impl;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.Projectile;
import org.rexcellentgames.burningknight.physics.World;

import java.util.ArrayList;

public class Orbital extends PetEntity {
	private static ArrayList<Orbital> all = new ArrayList<>();
	public static float speed = 1f;
	public static float count;

	protected float sx = 1;
	protected float sy = 1;
	private Body body;
	private float a;

	public static float orbitalTime;

	public static void updateTime(float dt) {
		orbitalTime += dt * speed;
	}

	{
		noTp = false;
	}

	private void setPos() {
		this.a = (float) (((float) id) / (count) * Math.PI * 2 + orbitalTime);
		float d = 28f; //  + (float) Math.cos(Dungeon.time * 2f) * 4f;

		this.x = this.owner.orbitalRing.x + (float) Math.cos(a) * d;
		this.y = this.owner.orbitalRing.y + (float) Math.sin(a) * d;

		World.checkLocked(this.body).setTransform(this.x, this.y, a);
	}


	@Override
	public void init() {
		super.init();

		this.w = this.region.getRegionWidth();
		this.h = this.region.getRegionHeight();

		body = World.createCircleCentredBody(this, 0f, 0f, Math.min(region.getRegionWidth(), region.getRegionHeight()) / 2f, BodyDef.BodyType.DynamicBody, true);
		body.setSleepingAllowed(false);
		all.add(this);

		readIndex();
		setPos();
	}

	@Override
	public void onCollision(Entity entity) {
		super.onCollision(entity);
		this.onHit(entity);
	}

	@Override
	public void renderShadow() {
		Graphics.shadow(this.x - this.w / 2, this.y - this.h / 2, this.w, this.h, 5);
	}

	protected void onHit(Entity entity) {
		if (entity instanceof Projectile && ((Projectile) entity).bad) {
			((Projectile) entity).remove();
		}
	}

	protected void readIndex() {
		this.id = all.indexOf(this);
	}

	private int id;

	@Override
	public void update(float dt) {
		super.update(dt);

		setPos();
		count += (all.size() - count) * dt;
	}

	@Override
	public void destroy() {
		super.destroy();

		body = World.removeBody(this.body);
		all.remove(this);

		for (Orbital o : all) {
			o.readIndex();
		}
	}

	@Override
	public void render() {
		Graphics.render(region, this.x, this.y, 0, this.w / 2, this.h / 2, false, false, this.sx, this.sy);
	}
}