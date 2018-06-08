package org.rexellentgames.dungeon.entity.item.pet.impl;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.creature.fx.Fireball;
import org.rexellentgames.dungeon.entity.item.weapon.gun.bullet.BulletEntity;
import org.rexellentgames.dungeon.physics.World;

import java.util.ArrayList;

public class Orbital extends PetEntity {
	private static ArrayList<Orbital> all = new ArrayList<>();
	public static float speed = 1f;

	protected float sx = 1;
	protected float sy = 1;
	private Body body;
	private float a;

	{
		noTp = false;
	}

	private void setPos() {
		this.a = (float) (((float) id) / ((float) all.size()) * Math.PI * 2 + Dungeon.time / 2f * speed);
		float d = 28f + (float) Math.cos(Dungeon.time * 2f) * 4f;

		this.x = this.owner.x + this.owner.w / 2 + (float) Math.cos(a) * d;
		this.y = this.owner.y + this.owner.h / 2 + (float) Math.sin(a) * d;

		this.body.setTransform(this.x, this.y, a);
	}

	@Override
	public void init() {
		super.init();

		this.w = this.region.getRegionWidth();
		this.h = this.region.getRegionHeight();

		body = World.createCircleCentredBody(this, 0f, 0f, Math.min(region.getRegionWidth(), region.getRegionHeight()) / 2f, BodyDef.BodyType.DynamicBody, true);
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
		if (entity instanceof BulletEntity && ((BulletEntity) entity).bad) {
			((BulletEntity) entity).remove = true;
		} else if (entity instanceof Fireball) {
			((Fireball) entity).delete();
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