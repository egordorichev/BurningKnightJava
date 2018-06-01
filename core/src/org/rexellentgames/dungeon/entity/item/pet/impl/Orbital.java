package org.rexellentgames.dungeon.entity.item.pet.impl;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.physics.World;

import java.util.ArrayList;

public class Orbital extends PetEntity {
	private static ArrayList<Orbital> all = new ArrayList<>();

	private Body body;
	private float a;

	private void setPos() {
		this.a = (float) (((float) id) / ((float) all.size()) * Math.PI * 2 + Dungeon.time / 2f);
		float d = 32f;

		this.x = this.owner.x + this.owner.w / 2 + (float) Math.cos(a) * d;
		this.y = this.owner.y + this.owner.h / 2 + (float) Math.sin(a) * d;

		this.body.setTransform(this.x, this.y, a);
	}

	@Override
	public void init() {
		super.init();

		body = World.createCircleCentredBody(this, 0f, 0f, Math.max(region.getRegionWidth(), region.getRegionHeight()) / 2f, BodyDef.BodyType.DynamicBody, true);
		all.add(this);

		readIndex();
		setPos();
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
		Graphics.render(region, this.x, this.y, (float) Math.toDegrees(this.a), this.w / 2, this.h / 2, false, false);
	}
}