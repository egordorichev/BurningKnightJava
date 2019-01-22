package org.rexcellentgames.burningknight.entity.fx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

import java.util.ArrayList;

public class Lighting extends Laser {
	private ArrayList<Point> shift = new ArrayList<>();
	private int num;
	public double an;
	public Point target;

	@Override
	public void init() {
		super.init();

		this.an = Math.toRadians(this.a + 90);
		this.num = (int) (this.w / 16f);

		shade = new Color(0, 1, 1, 1);
		mod = Random.newFloat(0.01f, 0.05f);
		target = new Point(x + (float) Math.cos(this.an) * this.w, y + (float) Math.sin(this.an) * this.w);
	}

	private float last;
	private float mod;


	public void updatePos() {
		if (removing) {
			return;
		}

		if (this.body != null) {
			World.removeBody(body);
			BodyDef def = new BodyDef();
			def.type = BodyDef.BodyType.StaticBody;

			body = World.world.createBody(def);
			PolygonShape poly = new PolygonShape();

			float x = 0f;
			float w = huge ? 12f : 6f;
			float h = this.w;
			float y = 0f;

			poly.set(new Vector2[] {
				new Vector2(x - w / 2, y), new Vector2(x + w / 2, y),
				new Vector2(x - w / 2, y + h), new Vector2(x + w / 2, y + h)
			});

			FixtureDef fixture = new FixtureDef();

			fixture.shape = poly;
			fixture.friction = 0;
			fixture.isSensor = true;

			fixture.filter.categoryBits = 0x0002;
			fixture.filter.groupIndex = -1;
			fixture.filter.maskBits = -1;

			body.createFixture(fixture);
			body.setUserData(this);
			poly.dispose();

			World.checkLocked(this.body).setTransform(this.x, this.y, (float) an);
		}
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		this.last += dt;

		if (this.last >= mod) {
			mod = Random.newFloat(0.01f, 0.05f);
			this.last = 0;
			this.shift.clear();
		}
	}

	@Override
	public void render() {
		int v = (int) (Math.ceil(this.w / 16) - 1);

		Point last = new Point(this.x, this.y);
		Graphics.startAlphaShape();

		float dx = target.x - last.x;
		float dy = target.y - last.y;

		for (int i = 0; i < v + 1; i++) {
			float pp = (i) / (v + 1f);

			Point point;

			if (v == i) {
				point = target;
			} else {
				if (this.shift.size() <= i) {
					this.shift.add(new Point(Random.newFloat(-5, 5), Random.newFloat(-5, 5)));
				}

				Point p = this.shift.get(i);
				point = new Point(x + dx * pp + p.x, y + dy * pp + p.y);
			}

			renderFrom(last, point);
			last = point;
		}

		Graphics.endAlphaShape();
	}
}